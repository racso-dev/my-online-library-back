package com.marketpay.job.parsing.n43;

import com.marketpay.exception.FundingDateException;
import com.marketpay.job.parsing.ParsingJob;
import com.marketpay.persistence.entity.JobHistory;
import com.marketpay.persistence.entity.Operation;
import com.marketpay.persistence.entity.Shop;
import com.marketpay.persistence.repository.JobHistoryRepository;
import com.marketpay.persistence.repository.OperationRepository;
import com.marketpay.persistence.repository.ShopRepository;
import com.marketpay.references.JOB_STATUS;
import com.marketpay.references.OPERATION_SENS;
import com.marketpay.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class ParsingN43Job extends ParsingJob {

    // Récupération de la createDate apartir du nom du fichier
    private final String CREATE_DATE_REGEX = "^\\d{2}(\\d{6})"; // Sur 20170826 on ne récupére que 170826

    // Identifié sur les lignes commençant par 11
    private final String BU_LINE_INFORMATION = "11";
    private final String FINANCING_DATE_REGEX = "^.{20}(\\d{6})"; // Groupe 1 format JJMMAA

    // Identifié sur les lignes commençant par 22
    private final String TRANSACTION_LINE_INFORMATION_WITH_GROSSAMOUNT = "^22.{20}12\\d{3}";
    private final String TRANSACTION_LINE_INFORMATION_WITH_COMMISION = "^22.{20}17\\d{3}";
    private final String CONTRACT_NUMBER_REGEX = "^.{42}(\\d{10})"; // Groupe 1
    private final String TRANSACTION_DATE_REGEX = "^.{16}(\\d{6})"; // Groupe 1
    private final String OPERATION_SENS_REGEX = "^.{22}12(\\d{3})(\\d{1})"; // Groupe 1 : opération Groupe 2 : Sens
    private final String GROSS_AMOUNT_REGEX = "^.{22}12\\d{3}\\d{1}(\\d{14})"; // Groupe 1
    private final String COMMISION_REGEX = "^.{22}17\\d{3}\\d{1}(\\d{14})"; // Groupe 1
    private final String COMMISSION_TYPE_REGEX = "^.{22}17(\\d{3})\\d{1}"; // Groupe 1
    private final String COMMISSION_SENS = "^.{22}17(\\d{3})(\\d{1})"; // Groupe 2 : Sens
    private final String OPERATION_REF_REGEX = ".{52}(.{12})"; // Groupe 1 utilisé uniquement pour les opérations 126 / 127

    private final Integer RECLAMATION_TYPE = 126;
    private final Integer VENTE_TYPE = 125;
    private final Integer ANNULATION_TYPE = 127;
    private final List<Integer> VENTE_COMMISSION_TYPE = Arrays.asList(205,208,210);
    private final List<Integer> RECLAMMATION_COMMISSION_TYPE = Arrays.asList(206);
    private final List<Integer> ANNULATION_COMMISSION_TYPE = Arrays.asList(207);

    private final Logger LOGGER = LoggerFactory.getLogger(ParsingN43Job.class);

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private JobHistoryRepository jobHistoryRepository;

    @Override
    public void parsing(String filePath, JobHistory jobHistory) throws IOException {
        FileReader file = new FileReader(filePath);
        BufferedReader buffer = new BufferedReader(file);

        List<JobHistory> jobHistoryList = jobHistoryRepository.findByFilenameOrderByDateDesc(jobHistory.getFilename());

        // Si le fichier a déjà été parsé on supprime les opérations associés
        if(jobHistoryList.size() > 0) {
            LOGGER.info("Le fichier : " + filePath + " a déjà été parsé, on supprime les opérations et on le reparse");
            JobHistory oldJobHistory = jobHistoryList.get(0);
            // Si on a déjà parser le fichier on supprime les operations associé pour les reparser
            List<Operation> operationList = operationRepository.findByIdJobHistory(oldJobHistory.getId());
            operationRepository.delete(operationList);
        }

        String[] filepathList = filePath.split("/");
        String filename = filepathList[filepathList.length - 1];
        String createDateString = matchFromRegex(filename, CREATE_DATE_REGEX, 1);
        LocalDate createDate = DateUtils.convertStringToLocalDate(DATE_FORMAT_N43, createDateString);

        try {
            String line;
            List<Operation> operationList = new ArrayList<>();
            LocalDate fundingDate = null;
            while ((line = buffer.readLine()) != null) {
                if (line.startsWith(BU_LINE_INFORMATION)) {
                    String foundingDateString = getFundingDate(line);
                    fundingDate = DateUtils.convertStringToLocalDate(DATE_FORMAT_N43, foundingDateString);
                } else if (matchFromRegex(line, TRANSACTION_LINE_INFORMATION_WITH_GROSSAMOUNT, 0) != null) {
                    Operation newOperation = new Operation();
                    newOperation.setIdJobHistory(jobHistory.getId());

                    if (fundingDate != null) {
                        newOperation.setFundingDate(fundingDate);
                    }
                    newOperation.setCreateDate(createDate);
                    newOperation.setSens(getSens(line));
                    OPERATION_SENS operationSens = OPERATION_SENS.getByCode(newOperation.getSens());
                    Integer operationType = getOperationType(line);
                    newOperation.setOperationType(operationType);

                    if(operationType.equals(RECLAMATION_TYPE) || operationType.equals(ANNULATION_TYPE)) {
                        newOperation.setReference(getOperationRef(line));
                    }

                    newOperation.setContractNumber(getContractNumber(line));
                    newOperation.setGrossAmount(getGrossAmount(line, operationSens));
                    newOperation.setNetAmount(newOperation.getGrossAmount());
                    String dateString = getTransactionDate(line);
                    newOperation.setTradeDate(DateUtils.convertStringToLocalDate(DATE_FORMAT_N43, dateString));
                    Optional<Shop> shopOpt = shopRepository.findByContractNumber(newOperation.getContractNumber());
                    if (shopOpt.isPresent()) {
                        newOperation.setNameShop(shopOpt.get().getName());
                        newOperation.setIdShop(shopOpt.get().getId());
                    } else {
                        LOGGER.warn("impossible de relié le contract number : " + newOperation.getContractNumber() + " avec un magasin");
                        jobHistory.setStatus(JOB_STATUS.MISSING_MATCHING_SHOP.getCode());
                        jobHistory.addError("impossible de relié le contract number : " + newOperation.getContractNumber() + " avec un magasin");
                    }

                    if (!operationList.isEmpty()) {
                        Operation lastOrder = operationList.get(operationList.size() - 1);
                        if (shouldCombine(lastOrder, newOperation)) {
                            // On agrége les transactions puis on remplace la dernière transaction par la transaction agrégée
                            newOperation = combineTransaction(lastOrder, newOperation);
                            operationList.remove(lastOrder);
                        }
                    }
                    operationList.add(newOperation);
                } else if (matchFromRegex(line, TRANSACTION_LINE_INFORMATION_WITH_COMMISION, 0) != null) {
                    operationList = addCommissionToOperation(line, operationList);
                }
            }

            for (Operation operation : operationList) {
                operationRepository.save(operation);
            }
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            errorBlock(e, null, jobHistory);
        } finally {
            buffer.close();
            file.close();
        }
    }

    /**
     * Permet d'ajouter la commission a une opération
     * @param line
     * @param operationList
     * @return
     */
    public List<Operation> addCommissionToOperation(String line, List<Operation> operationList) {
        // On récupére la date de transaction de la commission & son contract number
        String dateString = getTransactionDate(line);
        LocalDate transactionDate = DateUtils.convertStringToLocalDate(DATE_FORMAT_N43, dateString);
        String contractNumber = getContractNumber(line);
        Integer commissionType = getCommisionType(line);
        List<Operation> finalOperationList = new ArrayList();
        String commissionRef = null;

        if(RECLAMMATION_COMMISSION_TYPE.contains(commissionType) || ANNULATION_COMMISSION_TYPE.contains(commissionType)) {
            commissionRef = getOperationRef(line);
        }

        for(Operation operation: operationList) {
            // On cherche l'opération associé a la commission
            if(operation.getTradeDate().equals(transactionDate) && operation.getContractNumber().equals(contractNumber) && matchingCommissionOperation(operation.getOperationType(), commissionType)) {

                if((commissionRef != null && operation.getReference() != null && commissionRef.equals(operation.getReference())) || (commissionRef == null)) {
                    Integer commission = getCommission(line);
                    operation.setNetAmount(operation.getNetAmount() + commission);
                }
            }

            finalOperationList.add(operation);
        }

        return finalOperationList;
    }

    /**
     * Vérifie qu'on ajoute la commission a la bonne opération
     * @param typeOperation
     * @param typeCommission
     * @return
     */
    public Boolean matchingCommissionOperation(Integer typeOperation, Integer typeCommission) {

        if(typeOperation == VENTE_TYPE && VENTE_COMMISSION_TYPE.contains(typeCommission)) {
            return true;
        }

        if(typeOperation == RECLAMATION_TYPE && RECLAMMATION_COMMISSION_TYPE.contains(typeCommission)) {
            return true;
        }

        if(typeOperation == ANNULATION_TYPE && ANNULATION_COMMISSION_TYPE.contains(typeCommission)) {
            return true;
        }

        return false;
    }

    public String getFundingDate(String line) throws FundingDateException {
        try {
            return matchFromRegex(line, FINANCING_DATE_REGEX, 1);
        } catch (Exception e) {
            throw new FundingDateException(e.getMessage(), e.getCause(), line);
        }
    }

    public String getContractNumber(String line) {
        String contractNumber = matchFromRegex(line, CONTRACT_NUMBER_REGEX, 1);
        // On convertie en long pour enlever les premiers 0
        return Long.valueOf(contractNumber).toString();
    }

    public Integer getCommisionType(String line) {
        String commisionTypeString = matchFromRegex(line, COMMISSION_TYPE_REGEX, 1);
        return Integer.parseInt(commisionTypeString);
    }

    public String getTransactionDate(String line) {
        return matchFromRegex(line, TRANSACTION_DATE_REGEX, 1);
    }

    public Integer getOperationType(String line) {
        String operationString = matchFromRegex(line, OPERATION_SENS_REGEX, 1);
        return convertStringToInt(operationString);
    }

    public Integer getSens(String line) {
        return convertStringToInt(matchFromRegex(line, OPERATION_SENS_REGEX, 2))%2;
    }

    public Integer getCommissionSens(String line) {
        return convertStringToInt(matchFromRegex(line, COMMISSION_SENS, 2))%2;
    }

    public Integer getGrossAmount(String line, OPERATION_SENS sens) {
        String amount = matchFromRegex(line, GROSS_AMOUNT_REGEX, 1);
        Integer value = convertStringToInt(amount);

        return sens == OPERATION_SENS.DEBIT ? -value : value;
    }

    /**
     * Récupération du numéro de référence de la transaction
     * utilisé uniquement pour les operations type 126 et 127
     * @param line
     * @return
     */
    public String getOperationRef(String line) {
        return matchFromRegex(line, OPERATION_REF_REGEX, 1);
    }

    public Integer getCommission(String line) {
        String amount = matchFromRegex(line, COMMISION_REGEX, 1);
        Integer value = convertStringToInt(amount);
        OPERATION_SENS commissionSens = OPERATION_SENS.getByCode(getCommissionSens(line));
        return commissionSens == OPERATION_SENS.DEBIT ? -value : value;
    }

    /**
     * Permet de savoir si on doit ou non agréger deux transactions
     * @param firstTransaction
     * @param secondTransaction
     * @return Bool
     */
    public Boolean shouldCombine(Operation firstTransaction, Operation secondTransaction) {
        return  firstTransaction.getOperationType() == secondTransaction.getOperationType() && // si même type d'opération
                firstTransaction.getOperationType() != RECLAMATION_TYPE && firstTransaction.getOperationType() != ANNULATION_TYPE && // on aggrége tout sauf les annulation et les réclamations
                firstTransaction.getContractNumber().equals(secondTransaction.getContractNumber()) &&
                firstTransaction.getTradeDate().isEqual(secondTransaction.getTradeDate()); // On vérifie que les deux dates de financement sont identiques
    }

    /**
     * Permet d'agrégé deux lignes N43 qui ont le même type d'opération
     * @param firstTransaction
     * @param secondTransaction
     * @return Une seule transaction
     */
    public Operation combineTransaction(Operation firstTransaction, Operation secondTransaction) {
        Operation combinedTransaction;

        // On ajoute les montants
        combinedTransaction = firstTransaction;
        Long combineNetAmount = firstTransaction.getNetAmount() + secondTransaction.getNetAmount();
        combinedTransaction.setNetAmount(combineNetAmount);

        Long combineGrossAmount = firstTransaction.getGrossAmount() + secondTransaction.getGrossAmount();
        combinedTransaction.setGrossAmount(combineGrossAmount);

        if(combineGrossAmount < 0 ) {
            combinedTransaction.setSens(1);
        } else {
            combinedTransaction.setSens(0);
        }

        return combinedTransaction;
    }

    @Override
    protected void errorBlock(Exception e, List<String> block, JobHistory jobHistory) {
        // Si il y a une erreur sur une ligne on invalid le fichier N43
        LOGGER.error("Une erreur s'est produit pendant le parsing du block N43 : ", e);
        jobHistory.setStatus(JOB_STATUS.FAIL.getCode());
        jobHistory.addError(e.getMessage());
        jobHistoryRepository.save(jobHistory);
    }
}

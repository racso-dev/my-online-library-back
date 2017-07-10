package com.marketpay.job.parsing.coda;

import com.marketpay.job.parsing.ParsingJob;
import com.marketpay.persistence.entity.*;
import com.marketpay.persistence.repository.*;
import com.marketpay.references.JOB_STATUS;
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
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ParsingCODAJob extends ParsingJob {

    private final String ENDBLOCK_REGEX = "^9 .*";
    private final Logger LOGGER = LoggerFactory.getLogger(ParsingCODAJob.class);

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private JobHistoryRepository jobHistoryRepository;
    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    /**
     * Permet de découper le fichier en block de n relevés
     * @param filePath : path du fichier à parser
     */
    @Override
    public void parsing(String filePath, JobHistory jobHistory) throws IOException {
        FileReader input = new FileReader(filePath);
        BufferedReader buffer = new BufferedReader(input);
        List<String> block = new ArrayList<>();
        String line;
        Pattern endBlockPattern = Pattern.compile(ENDBLOCK_REGEX);
        while ((line = buffer.readLine()) != null) {
            block.add(line);
            Matcher matcher = endBlockPattern.matcher(line);
            if (matcher.find()) {
                parsingCodaBlock(block, jobHistory);
                // Ecrasement de la liste
                block.clear();
            }
        }
    }

    @Override
    protected void errorBlock(Exception e, List<String> block, JobHistory jobHistory) {
        //On sauvegarde le block en erreur dans la table block avec un status d'erreur
        Block codaBlock = new Block();

        if(block.size() > 3) {
            String headerRecipientLine = block.get(0);
            String clientId = getClientId(headerRecipientLine);
            Optional<BusinessUnit> businessUnitOpt = businessUnitRepository.findFirstByClientId(clientId);
            String centralisationLine1 = block.get(2);
            String foundingDate = getFoundingDate(centralisationLine1);
            //codaBlock.setFundingDate(DateUtils.convertStringToLocalDate(DATE_FORMAT_FILE, foundingDate));
            if(businessUnitOpt.isPresent()) {
                codaBlock.setIdBu(businessUnitOpt.get().getId());
            }
        }


        codaBlock.setContent(String.join("\\n", block));
        codaBlock.setStatus(JOB_STATUS.BLOCK_FAIL.getCode());
        blockRepository.save(codaBlock);

        // On met à jour le status du job et la liste d'erreur
        jobHistory.setStatus(JOB_STATUS.BLOCK_FAIL.getCode());
        jobHistory.addError(e.getMessage());
        jobHistoryRepository.save(jobHistory);

    }

    /**
     * Permet de parser chacun des blocks coda et enregistre le block en base
     * @param block
     * @param jobHistory
     */
    public void parsingCodaBlock(List<String> block, JobHistory jobHistory) {

        try {
            String headerRecipientLine = block.get(0);
            String centralisationLine1 = block.get(2);

            // Récupération de la BU
            String clientId = getClientId(headerRecipientLine);
            Optional<BusinessUnit> businessUnitOpt = businessUnitRepository.findFirstByClientId(clientId);
            // Récupération de la date de création
            String foundingDateString = getFoundingDate(centralisationLine1);
            LocalDate foundingDate = DateUtils.convertStringToLocalDate(DATE_FORMAT_FILE,  foundingDateString);

            Block codaBlock = new Block();
            codaBlock.setContent(String.join("\\n", block));
            codaBlock.setFundingDate(foundingDate);
            if (businessUnitOpt.isPresent()) {
                codaBlock.setIdBu(businessUnitOpt.get().getId());
            } else {
                jobHistory.setStatus(JOB_STATUS.MISSING_MATCHING_BU.getCode());
            }
            blockRepository.save(codaBlock);

            for (int i = 4; i < (block.size() - 2); i = i + 2) {
                String detailLine1 = block.get(i);
                String detailLine2 = block.get(i+1);
                if (!(detailLine1.startsWith("21") && detailLine2.startsWith("23"))) {
                    Operation operation = parsingDetailLines(block.get(i), block.get(i + 1));
                    operation.setFundingDate(foundingDate);
                    Optional<Store> storeOpt = storeRepository.findFirstByContractNumber(operation.getContractNumber());
                    if(storeOpt.isPresent()) {
                        operation.setNameStore(storeOpt.get().getName());
                    } else {
                        jobHistory.setStatus(JOB_STATUS.MISSING_MATCHING_STORE.getCode());
                    }
                    operationRepository.save(operation);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Une erreur s'est produit pendant le parsing du block CODA", e);
            errorBlock(e, block, jobHistory);
        }

    }

    /**
     * Parse les lignes de transactions et les enregistres en base
     * @param detailLine1
     * @param detailLine2
     */
    public Operation parsingDetailLines(String detailLine1, String detailLine2) {
        Operation operation = new Operation();
        operation.setSens(getSens(detailLine1));
        operation.setNetAmount(getNetAmount(detailLine1));
        operation.setContractNumber(getContractNumber(detailLine1));
        operation.setCardType(getCardType(detailLine1));
        operation.setGrossAmount(getGrossAmount(detailLine2));
        String dateString = getTransactionDate(detailLine1);
        operation.setTradeDate(DateUtils.convertStringToLocalDate(DATE_FORMAT_FILE, dateString));

        return operation;
    }

    /**
     * Récupération de l'id du client
     * @param firstLine Entête destinataire du fichier CODA
     * @return String
     */
    public String getClientId(String firstLine) {
        return firstLine.substring(71, 82).trim();
    }

    /**
     *
     * @param centralisationLine
     * @return
     */
    public String getFoundingDate(String centralisationLine) {
       return centralisationLine.substring(47, 53);
    }

    /**
     * Retourne le numéro du compte
     * @param line
     * @return numéro du compte
     */
    public String getCompteNumber(String line) {
        return line.substring(5, 17);
    }

    /**
     * Récupére le total des transactions
     * @param lastLine
     * @return total des transactions
     */
    public Integer getTotalAmount(String lastLine) {
        String totalAmountString = lastLine.substring(37, 52);
        return convertStringToInt(totalAmountString);
    }

    /**
     * Récupération du type de carte
     * @param line
     * @return le type de carte
     */
    public String getCardType(String line) {
        return line.substring(74, 77);
    }

    /**
     * Récupère le type de transaction 0 = Crédit 1 = Débit
     * @param line
     * @return le type de transaction
     */
    public Integer getSens(String line) {
        return convertStringToInt(line.substring(31, 32));
    }

    /**
     * Récupération du net amount
     * @param line
     * @return valeur du net amount
     */
    public Integer getNetAmount(String line) {
        String netAmountString = line.substring(32, 47);
        return convertStringToInt(netAmountString);
    }

    /**
     * Récupération du contract number
     * @param line
     * @return contract number
     */
    public String getContractNumber(String line) {
        return line.substring(62, 69);
    }

    /**
     * Récupération du gross amount
     * @param line
     * @return valeur du gross amount
     */
    public Integer getGrossAmount(String line) {
        String grossAmountString = line.substring(17, 33);
        return convertStringToInt(grossAmountString);
    }

    /**
     * Récupération de la date de transaction
     * @param line
     * @return date de transaction
     */
    public String getTransactionDate(String line) {
        return line.substring(78, 84);
    }
}

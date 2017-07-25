package com.marketpay.job.parsing.repositoryshop;

import com.marketpay.exception.EntityNotFoundException;
import com.marketpay.exception.ParsingException;
import com.marketpay.job.parsing.ParsingJob;
import com.marketpay.job.parsing.repositoryshop.resource.ShopCsvResource;
import com.marketpay.persistence.entity.BusinessUnit;
import com.marketpay.persistence.entity.JobHistory;
import com.marketpay.persistence.entity.Operation;
import com.marketpay.persistence.entity.Shop;
import com.marketpay.persistence.repository.BusinessUnitRepository;
import com.marketpay.persistence.repository.JobHistoryRepository;
import com.marketpay.persistence.repository.OperationRepository;
import com.marketpay.persistence.repository.ShopRepository;
import com.marketpay.references.JOB_STATUS;
import com.marketpay.references.LOCATION;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by etienne on 24/07/17.
 */
@Component
public class ParsingRepositoryShopJob extends ParsingJob {

    private final Logger LOGGER = LoggerFactory.getLogger(ParsingRepositoryShopJob.class);
    private int newShop;
    private int newBU;

    @Autowired
    private JobHistoryRepository jobHistoryRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    @Autowired
    private OperationRepository operationRepository;

    @Override
    public void parsing(String filePath, JobHistory jobHistory) throws IOException {
        //On récupère le fichier csv et on initialise le reader
        ICsvBeanReader csvBeanReader = null;
        try {
            //On récupère la location
            LOCATION location = getLocation(filePath);

            csvBeanReader = new CsvBeanReader(new FileReader(filePath), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

            //On récupère le header
            final String[] header = csvBeanReader.getHeader(true);

            ShopCsvResource shopCsv;
            newShop = 0;
            newBU = 0;
            while ((shopCsv = csvBeanReader.read(ShopCsvResource.class, header)) != null) {
                //Pour chaque ligne on met à jour la table shop et business_unit
                updateShopAndBU(shopCsv, location);
            }

            LOGGER.info("Ajout de " + newBU + " BU et de " + newShop + " shop");
        } catch (Exception e) {
            LOGGER.error("Une erreur s'est produit pendant le parsing du referentiel", e);
            errorBlock(e, null, jobHistory);
        } finally {
            if(csvBeanReader != null){
                csvBeanReader.close();
            }
        }
    }

    /**
     * Méthode de mise à jour des shop et des BU à partir d'un ShopCsv
     * @param shopCsv
     * @param location
     * @throws EntityNotFoundException
     */
    private void updateShopAndBU(ShopCsvResource shopCsv, LOCATION location) throws EntityNotFoundException {
        //On récupère le shop s'il existe déjà
        Optional<Shop> shopOpt = shopRepository.findByContractNumber(shopCsv.getNum_Contrat());

        //S'il existe on le met à jour ainsi que sa BU
        if(shopOpt.isPresent()){
            //Mise à jour shop
            shopOpt.get().setName(shopCsv.getNom_AL());
            shopRepository.save(shopOpt.get());

            //Mise à jour BU
            BusinessUnit businessUnit = businessUnitRepository.findOne(shopOpt.get().getIdBu()).orElseThrow(() ->
                new EntityNotFoundException(shopOpt.get().getIdBu(), "business_unit")
            );

            businessUnit.setName(shopCsv.getNom_BU());
            businessUnitRepository.save(businessUnit);
        }
        //Sinon on le créé
        else {
            //On récupère la BU si elle existe
            BusinessUnit businessUnit;
            Optional<BusinessUnit> businessUnitOpt = businessUnitRepository.findByCodeBu(shopCsv.getCode_BU());

            //Si elle existe on la met à jour
            if(businessUnitOpt.isPresent()){
                businessUnitOpt.get().setName(shopCsv.getNom_BU());
                businessUnit = businessUnitRepository.save(businessUnitOpt.get());
            }
            //Sinon on la créé
            else {
                businessUnit = new BusinessUnit();
                businessUnit.setCodeBu(shopCsv.getCode_BU());
                businessUnit.setCif(shopCsv.getCIF());
                businessUnit.setName(shopCsv.getNom_BU());
                businessUnit.setLocation(location.getCode());
                businessUnit = businessUnitRepository.save(businessUnit);
                newBU++;
            }

            //On créé le shop
            Shop shop = new Shop();
            shop.setIdBu(businessUnit.getId());
            shop.setAtica(shopCsv.getATICA());
            shop.setCodeAl(shopCsv.getCode_AL());
            shop.setContractNumber(shopCsv.getNum_Contrat());
            shop.setGln(shopCsv.getGLN());
            shop.setName(shopCsv.getNom_AL());
            shopRepository.save(shop);
            newShop++;

            //On met à jour les opérations appartenant à ce shop
            updateOperationShop(shop);
        }

    }

    /**
     * Retourne la location à partir du filePath
     * TODO ETI certainement à revoir quand on aura la nomenclature définitive des fichiers référentiel
     * @param filePath
     * @return
     * @throws ParsingException
     */
    private LOCATION getLocation(String filePath) throws ParsingException {
        if(filePath == null){
            throw new ParsingException("filePath null", filePath, "referentiel");
        }

        String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
        String codeLocation = fileName.split("_")[1];

        LOCATION location = LOCATION.getByCode(codeLocation);

        if(location == null){
            throw new ParsingException("Aucune location trouvé", filePath, "referentiel");
        }

        return location;
    }

    /**
     * Méthode qui met à jour les opérations avec le contract number du shop mais non encore associées
     * @param shop
     */
    private void updateOperationShop(Shop shop){
        //On récupère les operations avec le contractNumber du shop et qui n'ont pas d'association à un shop
        List<Operation> operationList = operationRepository.findByContractNumberAndIdShopIsNull(shop.getContractNumber());

        //Pour chaque operation on l'a met à jour avec le shop
        for(Operation operation: operationList){
            operation.setIdShop(shop.getId());
            operation.setNameShop(shop.getName());
            operationRepository.save(operation);
        }

        if(!operationList.isEmpty()) {
            LOGGER.info("Mise à jour de " + operationList.size() + " operation avec le shop " + shop.getId());
        }
    }

    @Override
    protected void errorBlock(Exception e, List<String> block, JobHistory jobHistory) {
        // S'il y a une erreur on invalid le fichier referentiel
        LOGGER.error("Une erreur s'est produite pendant le parsing du fichier referentielle : ", e);
        jobHistory.setStatus(JOB_STATUS.FAIL.getCode());
        jobHistory.addError((e.getMessage()));
        jobHistoryRepository.save(jobHistory);
    }
}

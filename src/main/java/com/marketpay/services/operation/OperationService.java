package com.marketpay.services.operation;

import com.marketpay.persistence.entity.Block;
import com.marketpay.persistence.entity.BusinessUnit;
import com.marketpay.persistence.entity.Operation;
import com.marketpay.persistence.entity.Shop;
import com.marketpay.persistence.repository.BlockRepository;
import com.marketpay.persistence.repository.BusinessUnitRepository;
import com.marketpay.persistence.repository.OperationRepository;
import com.marketpay.persistence.repository.ShopRepository;
import com.marketpay.references.LANGUAGE;
import com.marketpay.utils.I18nUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Created by tchekroun on 10/07/2017.
 */
@Component
public class OperationService {
    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private PdfOperationService pdfOperationService;

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private I18nUtils i18nUtils;

    /**
     * Permet de récupérer la liste d'opération effectué dans x magasins à un instant T
     * @param localDate
     * @param shopIdList
     * @return List d'opération
     */
    public List<Operation> getOperationFromShopIdListAndLocalDate(LocalDate localDate, List<Long> shopIdList) {
        return operationRepository.findOperationsByIdShopInAndFundingDate(shopIdList, localDate);
    }

    /**
     * Service de récupération des block CODA
     * @param fundingDate
     * @param idBu
     * @return
     */
    public String getCodaBlockFromIdBuAndFundingDate(LocalDate fundingDate, Long idBu) {
        //On récupère les block CODA
        List<Block> codaBlockList = blockRepository.findBlockByIdBuAndFundingDate(idBu, fundingDate);

        //On les concatène
        String fileContent = "";
        for(Block block: codaBlockList) {
            fileContent += block.getContent();
        }

        return fileContent;
    }

    /**
     * Crée un fichier pdf apartir d'une funding date, shopId & language
     * @param fundingDate : Date de financement
     * @param shopIdList : Liste des shops dont on veut récupérer les opérations
     * @param language : utilisé pour la traduction i18n
     * @return
     */
    public PDDocument getPdfFileFromTable(LocalDate fundingDate, List<Long> shopIdList, LANGUAGE language) {
        List<Operation> operationList = getOperationFromShopIdListAndLocalDate(fundingDate, shopIdList);
        pdfOperationService.setOperationList(operationList);
        String shopName = i18nUtils.getMessage("pdfOperationService.allShop", null, language);
        String buName = "";

        Optional<Shop> shopOptional = shopRepository.findOne(shopIdList.get(0));
        if(shopOptional.isPresent()) {
            // Si on n'a qu'un seul shop on récupère son nom
            if(shopIdList.size() == 1) {
                shopName = shopOptional.get().getName();
            }

            // On récupère la BU
            Optional<BusinessUnit> businessUnitOptional = businessUnitRepository.findOne(shopOptional.get().getIdBu());
            if(businessUnitOptional.isPresent()) {
                buName = businessUnitOptional.get().getName();
            }
        }

        return pdfOperationService.getPdfDocument(language, buName, shopName);
    }



}

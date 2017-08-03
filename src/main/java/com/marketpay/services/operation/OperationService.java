package com.marketpay.services.operation;

import com.marketpay.persistence.entity.Block;
import com.marketpay.persistence.entity.Operation;
import com.marketpay.persistence.repository.BlockRepository;
import com.marketpay.persistence.repository.OperationRepository;
import com.marketpay.references.LANGUAGE;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

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

    private final Logger LOGGER = LoggerFactory.getLogger(OperationService.class);

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

    public PDDocument getPdfFileFromTable(LocalDate fundingDate, List<Long> shopIdList, LANGUAGE language) {
        List<Operation> operationList = getOperationFromShopIdListAndLocalDate(fundingDate, shopIdList);
        pdfOperationService.setOperationList(operationList);
        return pdfOperationService.getPdfDocument(language);
    }



}

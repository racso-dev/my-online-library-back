package com.marketpay.api.operation;

import com.marketpay.annotation.Profile;
import com.marketpay.api.MarketPayController;
import com.marketpay.api.RequestContext;
import com.marketpay.api.operation.response.LastFundingDateResponse;
import com.marketpay.api.operation.response.OperationCodaBlockResponse;
import com.marketpay.api.operation.response.OperationListResponse;
import com.marketpay.exception.MarketPayException;
import com.marketpay.persistence.entity.Shop;
import com.marketpay.references.USER_PROFILE;
import com.marketpay.services.operation.OperationService;
import com.marketpay.services.shop.ShopService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tchekroun on 10/07/2017.
 */
@RestController
@RequestMapping(value = "/operation")
public class OperationController extends MarketPayController {

    private final Logger LOGGER = LoggerFactory.getLogger(OperationController.class);
    private final String FILENAME_PREFIX_PDF = "Operation_";

    @Autowired
    private OperationService operationService;

    @Autowired
    private ShopService shopService;

    /**
     * Renvoie la liste des opérations associé à un utilisateur à un instant T
     * @param localDate : Date des opérations
     * @param idShop
     * @return OperationListResponse
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @Profile({})
    public @ResponseBody
    OperationListResponse getOperationListByDate(@RequestParam(value = "localDate") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate localDate, @RequestParam(value="idShop", required = false) Long idShop, @RequestParam(value="idBu", required = false) Long idBu,
                                                 @RequestParam(value = "createDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate createDate) throws MarketPayException {
        //Si on passe un idShop, on vérifie que le user à le droit d'accès à ce shop
        List<Long> shopIdList = getAuthoriseShop(idShop, idBu);
        return operationService.getOperationListResponseFromShopIdListAndLocalDateAndCreateDate(localDate, shopIdList, createDate);
    }

    /**
     * WS de récupération d'un block CODA
     * @param fundingDate
     * @return
     */
    @RequestMapping(value = "/block", method = RequestMethod.GET)
    @Profile({})
    public @ResponseBody
    OperationCodaBlockResponse getCodaBlock(@RequestParam(value = "fundingDate")  @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fundingDate,
                                            @RequestParam(value = "createDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate createDate) {
        OperationCodaBlockResponse operationCodaBlockResponse = new OperationCodaBlockResponse();

        operationCodaBlockResponse.setFileContent(operationService.getCodaBlockFromIdBuAndFundingDate(fundingDate, RequestContext.get().getIdBu(), createDate));
        return operationCodaBlockResponse;

    }

    /**
     * WS de récupération du fichier PDF
     * @param idShop
     * @param fundingDate
     * @param response
     * @throws MarketPayException
     */
    @RequestMapping(value = "/pdf", method = RequestMethod.GET)
    @Profile({})
    public void getPdfFile(@RequestParam(value= "idShop", required = false) Long idShop, @RequestParam(value= "idBu", required = false) Long idBu, @RequestParam(value = "fundingDate") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fundingDate, HttpServletResponse response) throws MarketPayException {

        //Si on passe un idShop, on vérifie que le user à le droit d'accès à ce shop
        List<Long> shopIdList = getAuthoriseShop(idShop, idBu);

        String filename = FILENAME_PREFIX_PDF + fundingDate + ".pdf";
        response.setContentType("application/pdf");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        try {

            PDDocument pdDocument = operationService.getPdfFileFromTable(fundingDate, shopIdList, RequestContext.get().getLanguage());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            pdDocument.save(outputStream);
            pdDocument.close();
            response.getOutputStream().write(outputStream.toByteArray());
        } catch (IOException e) {
            throw new MarketPayException(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur est survenue lors de la création du fichier pdf");
        }

    }



    @Profile({})
    @RequestMapping(value = "/lastfundingdate", method = RequestMethod.GET)
    public @ResponseBody LastFundingDateResponse getLastFundingDate(@RequestParam(value = "idBu", required = false) Long idBu,  @RequestParam(value = "idShop", required = false) Long idShop) throws MarketPayException {

        //On check les droits en fonction du profile du user
        if(idBu != null && idShop != null) {
            throw  new MarketPayException(HttpStatus.BAD_REQUEST, "Soit idBu soit idShop, pas les deux");
        } else if(idBu != null) {
            checkAccessBU(idBu);
        } else if(idShop != null) {
            checkAccessShop(idShop);
        } else {
            throw  new MarketPayException(HttpStatus.BAD_REQUEST, "Pas d'idBu ou d'idShop");
        }

        //On récupère la dernière fundingDate des operations associé au user et à la shopList
        LOGGER.info("Récupération de la lastFundingDate");
        return new LastFundingDateResponse(operationService.getLastFundingDateForBuOrShop(idBu, idShop));
    }



    /**
     * Récupère la liste des shopId autorisé à l'utilisateur,
     * Compare avec celui demandé
     * renvoie une liste de shop id
     * @param idShop : Liste
     * @return List<Long>
     * @throws MarketPayException : Si l'utilisateur souhaite accéder à un shop non autorisé
     */
    private List<Long> getAuthoriseShop(Long idShop, Long idBu) throws MarketPayException {
        //On récupère la liste des shop associé au user
        List<Long> shopIdList = RequestContext.get().getIdShopList();

        //Si on passe un idShop, on vérifie que le user à le droit d'accès à ce shop
        if( idShop != null) {
            if( !shopIdList.contains(idShop) && !USER_PROFILE.ADMIN_USER.equals(RequestContext.get().getUserProfile()) ) {
                throw new MarketPayException(HttpStatus.UNAUTHORIZED, "Le user " + RequestContext.get().getUser().getId() + " n'a pas accès au shop " + idShop);
            } else {
                shopIdList.clear();
                shopIdList.add(idShop);
            }
        } else if(idBu != null) {
            //On vérifie que c'est bien l'admin
            if(!USER_PROFILE.ADMIN_USER.equals(RequestContext.get().getUserProfile())){
                throw new MarketPayException(HttpStatus.BAD_REQUEST, "Pas d'idBu pour le user non admin " + RequestContext.get().getUser().getId());
            }
            //On récupère les shops de la bu
            shopIdList = shopService.getShopListByIdBu(idBu).stream().map(Shop::getId).collect(Collectors.toList());
        }

        return shopIdList;
    }
}

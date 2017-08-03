package com.marketpay.api.operation;

import com.marketpay.annotation.Profile;
import com.marketpay.api.MarketPayController;
import com.marketpay.api.RequestContext;
import com.marketpay.api.operation.response.OperationCodaBlockResponse;
import com.marketpay.api.operation.response.OperationListResponse;
import com.marketpay.exception.MarketPayException;
import com.marketpay.references.USER_PROFILE;
import com.marketpay.services.operation.OperationService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tchekroun on 10/07/2017.
 */
@RestController
@RequestMapping(value = "/operation")
public class OperationController extends MarketPayController {

    private final Logger LOGGER = LoggerFactory.getLogger(OperationController.class);

    @Autowired
    private OperationService operationService;

    /**
     * Renvoie la liste des opérations associé à un utilisateur à un instant T
     * @param localDate : Date des opérations
     * @param idShop
     * @return OperationListResponse
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @Profile({USER_PROFILE.SUPER_USER, USER_PROFILE.USER, USER_PROFILE.USER_MANAGER})
    public @ResponseBody
    OperationListResponse getOperationListByDate(@RequestParam(value = "localDate") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate localDate, @RequestParam(value="idShop", required = false) Long idShop) throws MarketPayException {
        OperationListResponse operationListResponse = new OperationListResponse();

        //Si on passe un idShop, on vérifie que le user à le droit d'accès à ce shop
        List<Long> shopIdList = getAuthoriseShop(idShop);

        //Appel au service
        operationListResponse.setOperationList(operationService.getOperationFromShopIdListAndLocalDate(localDate, shopIdList));
        LOGGER.info("Récupération de " + operationListResponse.getOperationList().size() + " pour la fundingDate " + localDate.toString() + " et le user " + RequestContext.get().getUser().getId());

        return operationListResponse;
    }

    /**
     * WS de récupération d'un block CODA
     * @param fundingDate
     * @return
     */
    @RequestMapping(value = "/block", method = RequestMethod.GET)
    @Profile({USER_PROFILE.SUPER_USER, USER_PROFILE.USER, USER_PROFILE.USER_MANAGER})
    public @ResponseBody
    OperationCodaBlockResponse getCodaBlock(@RequestParam(value = "fundingDate")  @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fundingDate) {
        OperationCodaBlockResponse operationCodaBlockResponse = new OperationCodaBlockResponse();
        operationCodaBlockResponse.setFileContent(operationService.getCodaBlockFromIdBuAndFundingDate(fundingDate, RequestContext.get().getIdBu()));
        return operationCodaBlockResponse;

    }

    /**
     * WS de récupération du fichier PDF
     * @param idShop
     * @param fundingDate
     * @param response
     * @throws MarketPayException
     */
    @RequestMapping(value = "test", method = RequestMethod.GET)
    @Profile({USER_PROFILE.SUPER_USER, USER_PROFILE.USER, USER_PROFILE.USER_MANAGER})
    public void getPdfFile(@RequestParam(value= "idShop", required = false) Long idShop, @RequestParam(value = "fundingDate") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fundingDate, HttpServletResponse response) throws MarketPayException {

        //Si on passe un idShop, on vérifie que le user à le droit d'accès à ce shop
        List<Long> shopIdList = getAuthoriseShop(idShop);

        response.setContentType("application/pdf");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Disposition", "attachment; filename=test.pdf");
        try {

            LOGGER.debug("Get document ");
             PDDocument pdDocument = operationService.getPdfFileFromTable(fundingDate, shopIdList);
             pdDocument.save("ceciestuntest.pdf");
             pdDocument.save(response.getOutputStream());
             pdDocument.close();
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

    /**
     * Récupère la liste des shopId autorisé à l'utilisateur,
     * Compare avec celui demandé
     * renvoie une liste de shop id
     * @param idShop : Liste
     * @return List<Long>
     * @throws MarketPayException : Si l'utilisateur souhaite accéder à un shop non autorisé
     */
    private List<Long> getAuthoriseShop(Long idShop) throws MarketPayException {
        //On récupère la liste des shop associé au user
        List<Long> shopIdList = RequestContext.get().getIdShopList();

        //Si on passe un idShop, on vérifie que le user à le droit d'accès à ce shop
        if( idShop != null) {
            if( !shopIdList.contains(idShop) ) {
                throw new MarketPayException(HttpStatus.UNAUTHORIZED, "Le user " + RequestContext.get().getUser().getId() + " n'a pas accès au shop " + idShop);
            } else {
                shopIdList.clear();
                shopIdList.add(idShop);
            }
        }

        return shopIdList;
    }
}

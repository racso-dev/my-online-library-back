package com.marketpay.api.parsing;

import com.marketpay.annotation.Dev;
import com.marketpay.annotation.Profile;
import com.marketpay.api.MarketPayController;
import com.marketpay.job.parsing.ParsingDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/parsing")
public class ParsingController extends MarketPayController {

    private final Logger LOGGER = LoggerFactory.getLogger(ParsingController.class);

    @Autowired
    private ParsingDispatcher parsingDispatcher;

    @Dev
    @Profile({})
    @RequestMapping(value = "", method = RequestMethod.POST)
    public @ResponseBody void parsingFileAtPath(@RequestParam(value = "filepath") String filepath, HttpServletResponse response) {
        LOGGER.info("Parsing du fichier " + filepath);
        parsingDispatcher.parsingFile(filepath);
        response.setStatus(HttpStatus.OK.value());
    }

}

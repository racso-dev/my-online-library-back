package com.marketpay.api.parsing;

import com.marketpay.api.MarketPayController;
import com.marketpay.job.parsing.ParsingDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/api/parsing")
public class ParsingController extends MarketPayController {

    @Autowired
    private ParsingDispatcher parsingDispatcher;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public @ResponseBody void parsingFileAtPath(@RequestParam(value = "filepath") String filepath, HttpServletResponse response) {
        parsingDispatcher.parsingFile(filepath);
        response.setStatus(HttpStatus.OK.value());
    }

}

package com.marketpay.api.parsing;

import com.marketpay.api.MarketPayController;
import com.marketpay.job.parsing.ParsingDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping(value = "/api/parsing")
public class ParsingController extends MarketPayController {

    @Autowired
    private ParsingDispatcher parsingDispatcher;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public @ResponseBody void parsingFileAtPath(@PathParam(value = "filepath") String filepath, HttpServletResponse response) {
        parsingDispatcher.parsingFile(filepath);
        response.setStatus(HttpServletResponse.SC_OK);
    }

}

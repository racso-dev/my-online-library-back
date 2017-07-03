package com.marketpay.api.hello;

import com.marketpay.api.MarketPayController;
import com.marketpay.services.hello.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by etienne on 03/07/17.
 */
@RestController
public class HelloController extends MarketPayController {

    @Autowired
    private HelloService helloService;

    /**
     * Hello world d'exemple
     * @return
     */
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public @ResponseBody String hello() {
        return helloService.hello();
    }

}

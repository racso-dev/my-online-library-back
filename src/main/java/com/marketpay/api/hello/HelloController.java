package com.marketpay.api.hello;

import com.marketpay.api.MarketPayController;
import com.marketpay.persistence.repository.BlockRepository;
import com.marketpay.persistence.entity.Block;
import com.marketpay.services.hello.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Created by etienne on 03/07/17.
 */
@RestController
public class HelloController extends MarketPayController {

    @Autowired
    private HelloService helloService;

    @Autowired
    private BlockRepository blockRepository;

    /**
     * Hello world d'exemple
     * @return
     */
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public @ResponseBody String hello() {
        Optional<Block> block = blockRepository.findFirstById(7l);
        if(block.isPresent()) {
            return "youhou";
        }
        return helloService.hello();

    }
}

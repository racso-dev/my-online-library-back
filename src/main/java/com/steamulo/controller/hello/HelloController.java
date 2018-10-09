package com.steamulo.controller.hello;

import com.steamulo.services.hello.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller Hello
 */
@RestController
@RequestMapping(value = "/hello")
public class HelloController {

    private final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);
    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }


    /**
     * WS de récupération de hello
     * @return
     */
    @PreAuthorize("@helloService.hasRightToSayHello(principal)")
    @GetMapping()
    public @ResponseBody Map<String, String> getHello()  {
        LOGGER.info("Récupération de hello");
        HashMap<String, String> map = new HashMap<>();
        map.put("msg", helloService.getHelloMessage());
        return map;
    }
}

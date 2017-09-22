package com.steamulo.api.hello;

import com.steamulo.annotation.NotAuthenticated;
import com.steamulo.exception.ApiException;
import com.steamulo.references.LANGUAGE;
import com.steamulo.services.hello.HelloService;
import com.steamulo.utils.I18nUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller Hello
 */
@RestController
@RequestMapping(value = "/hello")
public class HelloController {

    private final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private HelloService helloService;

    /**
     * WS de récupération de hello
     * @return
     */
    @NotAuthenticated //Ne pas oublier les droits d'accès à la requête
    @RequestMapping(value = "", method = RequestMethod.GET)
    public @ResponseBody String getHello() throws ApiException {
        LOGGER.info("Récupération de hello");
        return helloService.getHelloMessage();
    }
}

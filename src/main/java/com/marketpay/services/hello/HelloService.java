package com.marketpay.services.hello;

import org.springframework.stereotype.Component;

/**
 * Created by etienne on 03/07/17.
 */
@Component
public class HelloService {

    /**
     * Service d'exemple hello World
     * @return
     */
    public String hello(){
        return "Salut les meufs";
    }

}

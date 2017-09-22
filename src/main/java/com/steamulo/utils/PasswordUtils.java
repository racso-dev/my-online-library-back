package com.steamulo.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtils {

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(11);

    public static void main(String[] args) {

        /**
         * Encode les password permettant de les rentrer directment en bdd
         */
        String p0 = "gourio";
        String p01 = "user2";
        String p02 = "123456789";
        String p1 = "admin";
        String p2 = "super";
        String p3 = "super2";
        String p4 = "super3";

        String h0 = PASSWORD_ENCODER.encode(p0);
        String h01 = PASSWORD_ENCODER.encode(p01);
        String h02 = PASSWORD_ENCODER.encode(p02);
        String h1 = PASSWORD_ENCODER.encode(p1);
        String h2 = PASSWORD_ENCODER.encode(p2);
        String h3 = PASSWORD_ENCODER.encode(p3);
        String h4 = PASSWORD_ENCODER.encode(p4);

        System.err.println(p0 + " = " + h0);
        System.err.println(p01 + " = " + h01);
        System.err.println(p02 + " = " + h02);
        System.err.println(p1 + " = " + h1);
        System.err.println(p2 + " = " + h2);
        System.err.println(p3 + " = " + h3);
        System.err.println(p4 + " = " + h4);
    }

}

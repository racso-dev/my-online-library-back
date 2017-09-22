package com.steamulo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation pour la gestion des droits d'accès à des WS réserver au mode dev
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD})
public @interface Dev {

}

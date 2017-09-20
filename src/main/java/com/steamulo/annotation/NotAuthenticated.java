package com.steamulo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation qui pour la gestion des droits d'accès à des WS sans être authentifié
 * Created by etienne on 01/08/17.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD})
public @interface NotAuthenticated {
}

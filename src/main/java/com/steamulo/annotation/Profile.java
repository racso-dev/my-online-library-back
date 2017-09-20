package com.steamulo.annotation;

import com.steamulo.references.USER_PROFILE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation pour la gestion des droits d'accès en fonction du profile du user
 * Created by etienne on 31/07/17.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface Profile {

    USER_PROFILE[] value();

}

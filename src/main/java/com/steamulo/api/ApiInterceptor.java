package com.steamulo.api;

import com.steamulo.annotation.Dev;
import com.steamulo.annotation.NotAuthenticated;
import com.steamulo.annotation.Permission;
import com.steamulo.exception.ApiException;
import com.steamulo.persistence.entity.User;
import com.steamulo.persistence.repository.UserRepository;
import com.steamulo.references.USER_PROFILE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created by etienne on 03/07/17.
 */
public class ApiInterceptor extends HandlerInterceptorAdapter {

    private final Logger LOGGER = LoggerFactory.getLogger(ApiInterceptor.class);
    private final String DEV_PROFILE = "dev";

    private final String LOGIN_URI = "/login";
    private final String SWAGGER_URI = "/swagger";
    private final String MANAGE_URI = "/manage";
    private final String ERROR_URI = "/error";
    private final String CONFIGURATION_URI = "/configuration";

    private final String COOKIE_NAME_TOKEN = "sid";


    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();

        // On ne passe pas par l'interceptor pour certaines uri
        if (!uri.equals(LOGIN_URI) && !uri.startsWith(MANAGE_URI) && !uri.startsWith(SWAGGER_URI) && !uri.equals(ERROR_URI) && !uri.startsWith(CONFIGURATION_URI)) {
            HandlerMethod methodHandler = (HandlerMethod) handler;

            //On récupère l'identité du user via son login
            String login = null;
            NotAuthenticated notAuthenticated = methodHandler.getMethodAnnotation(NotAuthenticated.class);
            if(notAuthenticated == null && SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
                login = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            }

            // Les controllers @Dev ne sont autorisés que si on est en mode dev
            Dev devController = methodHandler.getMethodAnnotation(Dev.class);
            if (devController != null && !Arrays.asList(applicationContext.getEnvironment().getActiveProfiles()).contains(DEV_PROFILE)) {
                throw new ApiException(HttpStatus.UNAUTHORIZED, "Le service " + uri + " n'est accessible qu'en mode dev");
            }

            //On récupère le user connecté
            Optional<User> userOpt = Optional.empty();
            if(login != null){
                userOpt = userRepository.findByLogin(login);
                if(!userOpt.isPresent()){
                    throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "User introuvable en base pour le login " + login + " du user connecté");
                }
            }

            //On récupère le profile du user
            USER_PROFILE userProfile = null;
            if(userOpt.isPresent()){
                userProfile = USER_PROFILE.getByCode(userOpt.get().getProfile());
                if(userProfile == null){
                    throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Profile " + userOpt.get().getProfile() + " inconnu");
                }
            }

            //On check ses droits d'accès à l'uri
            Permission permission = methodHandler.getMethodAnnotation(Permission.class);
            if(notAuthenticated == null && permission == null){
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Aucune sécurité sur le profile d'accès est implémentée pour le service " + uri);
            }
            if(userProfile != null){
                checkProfile(permission, userProfile, uri);
            }


            //On créé le RequestContext
            RequestContext context = new RequestContext();
            context.setUri(uri);
            context.setUserProfile(userProfile);
            context.setUser(userOpt.orElse(null));

            //On récupère le token
            String token = null;
            if(request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals(COOKIE_NAME_TOKEN)) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
            context.setToken(token);

            RequestContext.set(context);
        }

        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Traitement requête terminé, supprimer le contexte
        RequestContext.clear();
        super.afterCompletion(request, response, handler, ex);
    }

    /**
     * Méthod qui check si userProfile est autorisé par la permission
     * @param permission
     * @param userProfile
     */
    private void checkProfile(Permission permission, USER_PROFILE userProfile, String uri) throws ApiException {
        if(!userProfile.getPermissionList().contains(permission.value())){
            //Si profile ne contient pas la permission alors on est pas autorisé, on STOP
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Le service " + uri + " n'est pas accessible au profile " + userProfile.getCode() + " car il n'a pas la permission " + permission.value().getCode());
        }
    }
}

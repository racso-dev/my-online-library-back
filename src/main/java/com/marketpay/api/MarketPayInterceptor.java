package com.marketpay.api;

import com.marketpay.annotation.Dev;
import com.marketpay.annotation.NotAuthenticated;
import com.marketpay.annotation.Profile;
import com.marketpay.exception.EntityNotFoundException;
import com.marketpay.exception.MarketPayException;
import com.marketpay.persistence.entity.Shop;
import com.marketpay.persistence.entity.User;
import com.marketpay.persistence.repository.ShopRepository;
import com.marketpay.persistence.repository.UserRepository;
import com.marketpay.references.LANGUAGE;
import com.marketpay.references.USER_PROFILE;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by etienne on 03/07/17.
 */
public class MarketPayInterceptor extends HandlerInterceptorAdapter {

    private final Logger LOGGER = LoggerFactory.getLogger(MarketPayInterceptor.class);
    private final String DEV_PROFILE = "dev";

    private final String LOGIN_URI = "/login";
    private final String SWAGGER_URI = "/swagger";
    private final String MANAGE_URI = "/manage";
    private final String ERROR_URI = "/error";
    private final String CONFIGURATION_URI = "/configuration";
    private final String COOKIE_LANGUAGE = "marketPayLanguage";

    private final String COOKIE_NAME_TOKEN = "sid";


    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShopRepository shopRepository;

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
                throw new MarketPayException(HttpStatus.UNAUTHORIZED, "Le service " + uri + " n'est pas accessible en mode non dev");
            }

            //On récupère le user connecté
            Optional<User> userOpt = Optional.empty();
            if(login != null){
                userOpt = userRepository.findByLogin(login);
                if(!userOpt.isPresent()){
                    throw new MarketPayException(HttpStatus.INTERNAL_SERVER_ERROR, "User introuvable en base pour le login " + login + " du user connecté");
                }
            }

            //On récupère le profile du user
            USER_PROFILE userProfile = null;
            if(userOpt.isPresent()){
                userProfile = USER_PROFILE.getByCode(userOpt.get().getProfile());
                if(userProfile == null){
                    throw new MarketPayException(HttpStatus.INTERNAL_SERVER_ERROR, "Profile " + userOpt.get().getProfile() + " inconnu");
                }
            }

            //On check ses droits d'accès à l'uri
            Profile profile = methodHandler.getMethodAnnotation(Profile.class);
            if(notAuthenticated == null && profile == null){
                throw new MarketPayException(HttpStatus.INTERNAL_SERVER_ERROR, "Aucune sécurité sur le profile d'accès est implémentée pour le service " + uri);
            }
            if(userProfile != null){
                checkProfile(profile, userProfile, uri);
            }

            //On récupère la liste des shop associés au user et la BU
            List<Long> idShopList = new ArrayList<>();
            Long idBu = null;
            if(userOpt.isPresent()) {
                if (userOpt.get().getIdShop() != null) {
                    //On récupère le shop
                    Optional<Shop> shopOpt = shopRepository.findOne(userOpt.get().getIdShop());
                    if(shopOpt.isPresent()){
                        idBu = shopOpt.get().getIdBu();
                    }
                    idShopList.add(userOpt.get().getIdShop());
                } else if (userOpt.get().getIdBu() != null) {
                    idBu = userOpt.get().getIdBu();
                    //On récupère les shop de la BU
                    shopRepository.findByIdBu(userOpt.get().getIdBu()).forEach(shop -> {
                        idShopList.add(shop.getId());
                    });
                }
            }

            //On récupère la langue en cookie si elle est présente, sinon on prend l'anglais par défaut
            LANGUAGE language = LANGUAGE.EN;
            if(request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals(COOKIE_LANGUAGE)) {
                        language = LANGUAGE.getByCode(cookie.getValue());
                    }
                }
            }

            //On créé le RequestContext
            RequestContext context = new RequestContext();
            context.setUri(uri);
            context.setUserProfile(userProfile);
            context.setUser(userOpt.orElse(null));
            context.setIdBu(idBu);
            context.setIdShopList(idShopList);
            context.setLanguage(language);

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
     * Méthod qui check si userProfile est autorisé par profile
     * @param profile
     * @param userProfile
     */
    private void checkProfile(Profile profile, USER_PROFILE userProfile, String uri) throws MarketPayException {
        //Si profile ne contient pas de USER_PROFILE, il n'y a pas de droit d'accès particulier
        //Donc on laisse passer
        //Si profile contient userProfile c'est OK, on laisse passer
        List<USER_PROFILE> profileList = Arrays.asList(profile.value());
        if(!profileList.isEmpty() && !profileList.contains(userProfile)){
            //Si profile ne contient pas userProfile alors on est pas autorisé, on STOP
            throw new MarketPayException(HttpStatus.UNAUTHORIZED, "Le service " + uri + " n'est pas accessible au profile " + userProfile.getCode());
        }
    }
}

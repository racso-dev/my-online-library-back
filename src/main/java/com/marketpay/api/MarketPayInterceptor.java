package com.marketpay.api;

import com.marketpay.annotation.Dev;
import com.marketpay.annotation.Profile;
import com.marketpay.persistence.entity.User;
import com.marketpay.persistence.repository.ShopRepository;
import com.marketpay.persistence.repository.UserRepository;
import com.marketpay.references.USER_PROFILE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

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

            //On récupère l'identité du user via son login
            String login = null;
            if(SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
                login = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            }


            HandlerMethod methodHandler = (HandlerMethod) handler;

            // Les controllers @Dev ne sont autorisés que si on est en mode dev
            Dev devController = methodHandler.getMethodAnnotation(Dev.class);
            if (devController != null && !Arrays.asList(applicationContext.getEnvironment().getActiveProfiles()).contains(DEV_PROFILE)) {
                LOGGER.error("Le service " + uri + " n'est pas accessible en mode non dev");
                //TODO ETI throw MarketPayException
            }

            //On récupère le user connecté
            Optional<User> userOpt = Optional.empty();
            if(login != null){
                userOpt = userRepository.findByLogin(login);
                if(!userOpt.isPresent()){
                    //TODO ETI throw MarketPayException
                }
            }

            //On récupère le profile du user
            USER_PROFILE userProfile = null;
            if(userOpt.isPresent()){
                userProfile = USER_PROFILE.getByCode(userOpt.get().getProfile());
                if(userProfile == null){
                    //TODO ETI throw MarketPayException
                }
            }

            //On check ses droits d'accès à l'uri
            Profile profile = methodHandler.getMethodAnnotation(Profile.class);
            if(profile == null){
                //TODO ETI throw MarketPayException
            }
            if(userProfile != null){
                checkProfile(profile, userProfile);
            }

            //On récupère la liste des shop associés au user et la BU
            List<Long> idShopList = new ArrayList<>();
            Long idBu = null;
            if(userOpt.isPresent()) {
                if (userOpt.get().getIdShop() != null) {
                    idShopList.add(userOpt.get().getIdShop());
                } else if (userOpt.get().getIdBu() != null) {
                    idBu = userOpt.get().getIdBu();
                    //On récupère les shop de la BU
                    shopRepository.findByIdBu(userOpt.get().getIdBu()).forEach(shop -> {
                        idShopList.add(shop.getId());
                    });
                }
            }

            //On créé le RequestContext
            RequestContext context = new RequestContext();
            context.setUri(uri);
            context.setUserProfile(userProfile);
            context.setUser(userOpt.orElse(null));
            context.setIdBu(idBu);
            context.setIdShopList(idShopList);
            RequestContext.set(context);
        }

        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Traitement requête terminé, supprimer le contexte
        RequestContext.clear();
        //TODO ETI
//        ErrorHandler.resetErrorHandler();
        super.afterCompletion(request, response, handler, ex);
    }

    /**
     * Méthod qui check si userProfile est autorisé par profile
     * @param profile
     * @param userProfile
     */
    private void checkProfile(Profile profile, USER_PROFILE userProfile) {
        //Si profile ne contient pas de USER_PROFILE, il n'y a pas de droit d'accès particulier
        //Donc on laisse passer
        //Si profile contient userProfile c'est OK, on laisse passer
        List<USER_PROFILE> profileList = Arrays.asList(profile.value());
        if(!profileList.isEmpty() && !profileList.contains(userProfile)){
            //Si profile ne contient pas userProfile alors on est pas autorisé, on STOP
            //TODO ETI throw MarketPayException
        }
    }
}

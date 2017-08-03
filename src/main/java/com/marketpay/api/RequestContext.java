package com.marketpay.api;

import com.marketpay.persistence.entity.User;
import com.marketpay.references.LANGUAGE;
import com.marketpay.references.USER_PROFILE;

import java.util.List;

/**
 * Created by etienne on 31/07/17.
 */
public class RequestContext {

    //Uri de la request
    private String uri;

    //Profile du user
    private USER_PROFILE userProfile;

    //BU du user
    private Long idBu;

    //Shop du user
    private List<Long> idShopList;

    //User
    private User user;

    //Language de la request
    private LANGUAGE language;

    //Déclaration du context
    private static ThreadLocal<RequestContext> mpContext = new ThreadLocal<RequestContext>();

    /**
     * Récupération du context
     * @return
     */
    public static RequestContext get() {
        return mpContext.get();
    }

    /**
     * Set du context
     * @param context
     */
    public static void set(RequestContext context) {
        mpContext.set(context);
    }

    /**
     * Vider le context en cours
     */
    public static void clear() {
        mpContext.remove();
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public USER_PROFILE getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(USER_PROFILE userProfile) {
        this.userProfile = userProfile;
    }

    public Long getIdBu() {
        return idBu;
    }

    public void setIdBu(Long idBu) {
        this.idBu = idBu;
    }

    public List<Long> getIdShopList() {
        return idShopList;
    }

    public void setIdShopList(List<Long> idShopList) {
        this.idShopList = idShopList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LANGUAGE getLanguage() {
        return language;
    }

    public void setLanguage(LANGUAGE language) {
        this.language = language;
    }

}

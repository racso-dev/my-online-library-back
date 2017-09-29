package com.steamulo.api;

import com.steamulo.persistence.entity.User;
import com.steamulo.references.USER_ROLE;

/**
 * Keep context of the request
 */
public class RequestContext {

    //Uri de la request
    private String uri;

    //Role du user
    private USER_ROLE userRole;

    //User
    private User user;

    //Token
    private String token;

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

    public USER_ROLE getUserRole() {
        return userRole;
    }

    public void setUserRole(USER_ROLE userRole) {
        this.userRole = userRole;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

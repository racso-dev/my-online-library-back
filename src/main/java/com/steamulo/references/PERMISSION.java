package com.steamulo.references;

/**
 * Created by etienne on 22/09/17.
 */
public enum PERMISSION {

    //Une permission est une autorisation de faire une action par exemple créer un user
    //Liste des permissions
    //A compléter ou modifier au besoin
    USER_CREATE("user:create"),
    USER_DELETE("user:delete"),
    USER_GET("user:get"),
    USER_GET_CONNECTED("user:getConnected");

    private String code;

    PERMISSION(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static PERMISSION getByCode(String code) {
        for(PERMISSION permission : PERMISSION.values()) {
            if(permission.getCode().equals(code)){
                return permission;
            }
        }
        return null;
    }
}

package com.steamulo.references;

/**
 * Enum des permissions des appels aux WS
 */
public enum PERMISSION {

    //Une permission est une autorisation de faire une action par exemple créer un user
    //Liste des permissions
    //A compléter ou modifier au besoin
    USER_CREATE("user:create"),
    USER_DELETE("user:delete"),
    USER_GET("user:get"),
    USER_GET_SELF("user:getConnected");

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

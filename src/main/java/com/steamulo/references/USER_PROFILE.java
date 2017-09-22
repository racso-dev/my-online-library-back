package com.steamulo.references;

import java.util.Arrays;
import java.util.List;

/**
 * Enum des profiles USER
 */
public enum USER_PROFILE {

    //Un profile donne des droits au user via sa collection de permission
    //Liste des profiles
    //A compléter ou modifier au besoin
    ADMIN_USER("admin", Arrays.asList(
        PERMISSION.USER_CREATE,
        PERMISSION.USER_DELETE,
        PERMISSION.USER_GET,
        PERMISSION.USER_GET_SELF
    )),
    USER("user", Arrays.asList(
        PERMISSION.USER_GET_SELF
    ));

    private String code;
    private List<PERMISSION> permissionList;

    USER_PROFILE(String code, List<PERMISSION> permissionList) {
        this.code = code;
        this.permissionList = permissionList;
    }

    public String getCode() {
        return code;
    }

    public List<PERMISSION> getPermissionList() {
        return permissionList;
    }

    public static USER_PROFILE getByCode(String code) {
        for(USER_PROFILE userProfile : USER_PROFILE.values()) {
            if(userProfile.getCode().equals(code)){
                return userProfile;
            }
        }
        return null;
    }
}

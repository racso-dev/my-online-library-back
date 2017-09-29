package com.steamulo.references;

import java.util.Arrays;
import java.util.List;

/**
 * Enum des roles USER
 */
public enum USER_ROLE {

    //Un role donne des droits au user via sa collection de permission
    //Liste des roles
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

    USER_ROLE(String code, List<PERMISSION> permissionList) {
        this.code = code;
        this.permissionList = permissionList;
    }

    public String getCode() {
        return code;
    }

    public List<PERMISSION> getPermissionList() {
        return permissionList;
    }

    public static USER_ROLE getByCode(String code) {
        for(USER_ROLE userRole : USER_ROLE.values()) {
            if(userRole.getCode().equals(code)){
                return userRole;
            }
        }
        return null;
    }
}

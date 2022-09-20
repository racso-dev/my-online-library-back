package com.steamulo.enums;

import java.util.Arrays;
import java.util.List;

public enum UserRole {

    ADMIN(Arrays.asList(
            Permission.USER_CREATE,
            Permission.USER_DELETE,
            Permission.USER_GET,
            Permission.USER_GET_SELF)),
    USER(Arrays.asList(
            Permission.USER_GET_SELF,
            Permission.USER_UPDATE_SELF,
            Permission.BOOK_GET));

    private List<Permission> permissionList;

    UserRole(List<Permission> permissionList) {
        this.permissionList = permissionList;
    }

    public List<Permission> getPermissionList() {
        return permissionList;
    }

}

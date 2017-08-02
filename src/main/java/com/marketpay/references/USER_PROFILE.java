package com.marketpay.references;

/**
 * Enum des profiles USER
 * Created by etienne on 31/07/17.
 */
public enum USER_PROFILE {

    ADMIN_USER(1),
    SUPER_USER(2),
    USER_MANAGER(3),
    USER(4);

    private int code;

    USER_PROFILE(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static USER_PROFILE getByCode(int code){
        for(USER_PROFILE userProfile : USER_PROFILE.values()){
            if(userProfile.getCode() == code){
                return userProfile;
            }
        }
        return null;
    }
}

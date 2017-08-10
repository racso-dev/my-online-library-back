package com.marketpay.references;

/**
 * Created by etienne on 10/08/17.
 */
public enum MAIL_TYPE {

    CREATE_USER(1, "createMail"),
    RESET_PASSWORD(2, "resetMail");

    private int code;
    private String name;

    MAIL_TYPE(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static MAIL_TYPE getByCode(int code){
        for(MAIL_TYPE mailType : MAIL_TYPE.values()){
            if(mailType.getCode() == code){
                return mailType;
            }
        }
        return null;
    }
}

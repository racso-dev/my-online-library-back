package com.marketpay.services.mail.resource;

import com.marketpay.references.MAIL_TYPE;

/**
 * Created by etienne on 10/08/17.
 */
public class CreateEmailBody extends EmailBody {

    private String urlFirstConnection;
    private String login;
    private String shopName;
    private String buName;

    public CreateEmailBody() {
        super(MAIL_TYPE.CREATE_USER);
    }

    public String getUrlFirstConnection() {
        return urlFirstConnection;
    }

    public void setUrlFirstConnection(String urlFirstConnection) {
        this.urlFirstConnection = urlFirstConnection;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }
}

package com.marketpay.services.mail.resource;

import com.marketpay.references.MAIL_TYPE;

/**
 * Created by etienne on 10/08/17.
 */
public class ResetEmailBody extends EmailBody {

    private String urlReset;

    public ResetEmailBody() {
        super(MAIL_TYPE.RESET_PASSWORD);
    }

    public String getUrlReset() {
        return urlReset;
    }

    public void setUrlReset(String urlReset) {
        this.urlReset = urlReset;
    }

}

package com.marketpay.services.mail.resource;

import com.marketpay.references.MAIL_TYPE;

/**
 * Created by etienne on 10/08/17.
 */
public class EmailBody {

    private String firstName;
    private String lastName;
    private MAIL_TYPE mailType;

    public EmailBody() {
    }

    public EmailBody(MAIL_TYPE mailType) {
        this.mailType = mailType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public MAIL_TYPE getMailType() {
        return mailType;
    }

    public void setMailType(MAIL_TYPE mailType) {
        this.mailType = mailType;
    }
}

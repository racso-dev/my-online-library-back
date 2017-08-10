package com.marketpay.services.mail.resource;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean avec le contenu du mail
 * Created by etienne on 08/08/17.
 */
public class MarketPayEmail {

    private String subject;

    private String body;

    private List<String> toList = new ArrayList<String>();

    private List<String> hiddenToList = new ArrayList<String>();

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getToList() {
        return toList;
    }

    public void setToList(List<String> toList) {
        this.toList = toList;
    }

    public List<String> getHiddenToList() {
        return hiddenToList;
    }

    public void setHiddenToList(List<String> hiddenToList) {
        this.hiddenToList = hiddenToList;
    }

}

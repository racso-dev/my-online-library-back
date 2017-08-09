package com.marketpay.services.mail;

import com.marketpay.conf.EmailConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by etienne on 08/08/17.
 */
@Component
public class MailService {

    private final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailConfig emailConfig;


    /**
     * Ce service permet d'envoyer un mail
     * @param email
     * @return
     */
    public boolean sendMail(MarketPayEmail email) {

        try {
            //Construction du message
            SimpleMailMessage message = new SimpleMailMessage();

            //on met le mail d'origin
            message.setFrom(new InternetAddress(emailConfig.getEmailFrom()).getAddress());

            //on ajoute un mail de réponse s'il y en a
            //TODO ETI à mettre en place plus tard
            if(email.getReplyTo() != null && !email.getReplyTo().isEmpty()){
                String replyTo = new InternetAddress(email.getReplyTo()).getAddress();
                message.setReplyTo(replyTo);
            }

            // Liste des destinaires effectifs (non filtrés par la conf)
            List<String> toList = new ArrayList<>();

            //liste des destinataires
            for (String to : email.getToList()){
                if(isEmailAuthorized(to)){
                    toList.add(new InternetAddress(to).getAddress());
                } else{
                    LOGGER.info("Envoi de l'email " + email.getSubject() + " - utilisateur filtré : " + to);
                }
            }

            // Liste des destinaires effectifs (non filtrés par la conf)
            List<String> hiddenToList = new ArrayList<>();

            //liste des destinataires
            for (String hiddenTo : email.getHiddenToList()){
                if(isEmailAuthorized(hiddenTo)){
                    hiddenToList.add(new InternetAddress(hiddenTo).getAddress());
                } else{
                    LOGGER.info("Envoi de l'email " + email.getSubject() + " - utilisateur filtré : " + hiddenTo);
                }
            }

            if(!toList.isEmpty()){

                message.setTo(toList.toArray(new String[toList.size()]));

                if(hiddenToList.size() > 0){
                    message.setBcc(hiddenToList.toArray(new String[hiddenToList.size()]));
                }

                // Sujet de l'email
                message.setSubject(email.getSubject());
                message.setSentDate(new Date());

                // Set body
                //TODO ETI, template?
//                message.setContent(multipart);


                mailSender.send(message);
                toList.forEach(to -> {
                    LOGGER.info("Envoi de l'email " + email.getSubject() + " à l'utilisateur : " + to);
                });
            }
            return true;

        } catch (Exception e) {
            LOGGER.error("Une erreur est survenue pendant l'envoi du mail :", e);
            return false;
        }
    }


    /**
     * Est-il possible d'envoyer un e-mail à l'utilisateur
     * @param email
     * @return
     */
    private boolean isEmailAuthorized(String email){
        String authorizedDomains = emailConfig.getFilter();

        if (authorizedDomains != null && !authorizedDomains.isEmpty()) {
            for(String filter : authorizedDomains.split(";")) {
                // Email autorisé
                if(email.contains(filter)){
                    return true;
                }
            }
            return false;
        }
        // Rien de configuré
        else {
            return true;
        }
    }

}

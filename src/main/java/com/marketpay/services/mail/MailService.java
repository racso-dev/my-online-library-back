package com.marketpay.services.mail;

import com.marketpay.conf.EmailConfig;
import com.marketpay.exception.MarketPayException;
import com.marketpay.services.mail.resource.MarketPayEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
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

    @Autowired
    private ApplicationContext applicationContext;

    private final String IMAGE_MARKET_PAY_PATH = "classpath:templates/logoMP-horizontal-small.png";


    /**
     * Ce service permet d'envoyer un mail
     * @param email
     * @return
     */
    public boolean sendMail(MarketPayEmail email) throws MarketPayException {
        try {
            //Construction du message avec des fichiers joints
            MimeMessage message = mailSender.createMimeMessage();

            //on met le mail d'origin
            message.setFrom(new InternetAddress(emailConfig.getEmailFrom()));

            //on ajoute un mail de réponse s'il y en a
            if(emailConfig.getReplyTo() != null && !emailConfig.getReplyTo().isEmpty()){
                List<Address> replyToList = new ArrayList<>();
                replyToList.add(new InternetAddress(emailConfig.getReplyTo()));
                message.setReplyTo(replyToList.toArray(new Address[replyToList.size()]));
            }

            // Liste des destinaires effectifs (non filtrés par la conf)
            List<InternetAddress> toAddresseList = new ArrayList<InternetAddress>();

            //liste des destinataires
            if(email.getToList() != null) {
                for (String to : email.getToList()) {
                    if (isEmailAuthorized(to)) {
                        toAddresseList.add(new InternetAddress(to));
                    } else {
                        LOGGER.info("Envoi de l'email " + email.getSubject() + " - utilisateur filtré : " + to);
                    }
                }
            }

            // Liste des destinaires caché effectifs (non filtrés par la conf)
            List<InternetAddress> toHiddenAddressList = new ArrayList<InternetAddress>();

            //liste des destinataires
            if(email.getHiddenToList() != null) {
                for (String toHidden : email.getHiddenToList()) {
                    if (isEmailAuthorized(toHidden)) {
                        toHiddenAddressList.add(new InternetAddress(toHidden));
                    } else {
                        LOGGER.info("Envoi de l'email " + email.getSubject() + " - utilisateur filtré : " + toHidden);
                    }
                }
            }

            if(!toAddresseList.isEmpty()) {

                message.setRecipients(Message.RecipientType.TO, toAddresseList.toArray(new InternetAddress[toAddresseList.size()]));
                if (toHiddenAddressList.size() > 0) {
                    LOGGER.info("On envoie un mail en copie caché à " + toHiddenAddressList.get(0).getAddress());
                    message.setRecipients(Message.RecipientType.BCC, toHiddenAddressList.toArray(new InternetAddress[toHiddenAddressList.size()]));
                }

                // Sujet de l'email
                message.setSubject(email.getSubject());
                message.setSentDate(new Date());

                // Type du contenu et contenu du mail
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(email.getBody(), "text/html; charset=" + "UTF-8");

                // creation du multi-part
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);

                // Ajout image MarketPay
                File imageFile = getMarketPayImage();

                if (imageFile != null && imageFile.exists()) {
                    MimeBodyPart imagePart = new MimeBodyPart();
                    imagePart.setHeader("Content-ID", "<marketPay>");
                    imagePart.setDisposition(MimeBodyPart.INLINE);

                    // Pièce jointe de type image
                    imagePart.attachFile(imageFile);
                    multipart.addBodyPart(imagePart);
                } else {
                    if (imageFile != null) {
                        LOGGER.info("L'image " + imageFile.getAbsolutePath() + " à insérer dans l'email n'existe pas sur le filesystem");
                    }
                }

                // sets the multi-part as e-mail's content
                message.setContent(multipart);

                mailSender.send(message);
                toAddresseList.forEach(recipient -> {
                    LOGGER.info("Envoi de l'email " + email.getSubject() + " à l'utilisateur : " + recipient.getAddress());
                });
            }

            return true;
        } catch (Exception e) {
            throw new MarketPayException(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur est survenue pendant l'envoi du mail", e);
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

    /**
     * Récupération de l'image MarketPay
     * @return
     */
    public File getMarketPayImage() {
        try {

//            File img2 = applicationContext.getResource(IMAGE_MARKET_PAY_PATH) != null ? applicationContext.getResource(IMAGE_MARKET_PAY_PATH).getFile() : null;
            File img3 = applicationContext.getResource("templates/logoMP-horizontal-small.png") != null ? applicationContext.getResource("templates/logoMP-horizontal-small.png").getFile() : null;
            File img4 = new File(IMAGE_MARKET_PAY_PATH);
            File img5 = new File("templates/logoMP-horizontal-small.png");

//            if (img2 != null) {
//                LOGGER.info("img2");
//                return img2;
//            }
            if (img3 != null) {
                LOGGER.info("img3");
                return img3;
            }
            if (img4 != null) {
                LOGGER.info("img4");
                return img4;
            }
            LOGGER.info("img5");
            return img5;
        } catch(Exception e) {
            LOGGER.error("Erreur lors de la récupération de l'image marketPay", e);
            return null;
        }
    }

}

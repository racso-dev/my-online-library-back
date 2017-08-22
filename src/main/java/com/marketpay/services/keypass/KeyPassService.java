package com.marketpay.services.keypass;

import com.marketpay.api.keypass.request.ResetPasswordRequest;
import com.marketpay.exception.EntityNotFoundException;
import com.marketpay.exception.MarketPayException;
import com.marketpay.persistence.entity.User;
import com.marketpay.persistence.entity.UserKeyPass;
import com.marketpay.persistence.repository.UserKeyPassRepository;
import com.marketpay.persistence.repository.UserRepository;
import com.marketpay.references.LANGUAGE;
import com.marketpay.services.auth.TokenAuthenticationService;
import com.marketpay.services.mail.MailBuilder;
import com.marketpay.services.mail.MailService;
import com.marketpay.services.mail.resource.CreateEmailBody;
import com.marketpay.services.mail.resource.EmailBody;
import com.marketpay.services.mail.resource.MarketPayEmail;
import com.marketpay.services.mail.resource.ResetEmailBody;
import com.marketpay.services.user.UserService;
import com.marketpay.services.user.resource.UserResource;
import com.marketpay.utils.I18nUtils;
import com.marketpay.utils.PasswordUtils;
import com.marketpay.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by etienne on 09/08/17.
 */
@Component
public class KeyPassService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserKeyPassRepository userKeyPassRepository;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Value("${keypass.expirationReset}")
    private long EXPIRATION_RESET_KEY_PASS_MIN;

    @Value("${keypass.expirationCreate}")
    private long EXPIRATION_CREATE_RESET_KEY_PASS_MIN;

    @Value("${keypass.urlReset}")
    private String URL_RESET_KEY_PASS;

    @Autowired
    private MailService mailService;

    @Autowired
    private MailBuilder mailBuilder;

    @Autowired
    private UserService userService;

    @Autowired
    private I18nUtils i18nUtils;

    /**
     * Service de modification de mot de passe via KeyPass
     * Connecte le user et retourne le token
     * @param keyPass
     * @param request
     * @return
     */
    public String resetPasswordKeyPass(String keyPass, ResetPasswordRequest request) throws MarketPayException {
        //On check le keyPass
        UserKeyPass userKeyPass = userKeyPassRepository.findByKeyPass(keyPass).orElseThrow(() ->
            new MarketPayException(HttpStatus.UNAUTHORIZED, "KeyPass inexistant")
        );

        //On vérifie la date d'expiration
        if(LocalDateTime.now().isAfter(userKeyPass.getExpirationDateTime())){
            //KeyPass expiré on le supprime puis on renvoi un unauthorize
            userKeyPassRepository.delete(userKeyPass);
            throw new MarketPayException(HttpStatus.UNAUTHORIZED, "KeyPass expired");
        }

        //On récupère le user associé
        User user = userRepository.findOne(userKeyPass.getIdUser()).orElseThrow(() ->
            new EntityNotFoundException(userKeyPass.getIdUser(), "user")
        );

        //On check le login
        if(!user.getLogin().equals(request.getLogin())){
            throw new MarketPayException(HttpStatus.BAD_REQUEST, "Login incorrect", "login");
        }

        //On supprime le userKeyPass
        userKeyPassRepository.delete(userKeyPass);

        //On change le mot de passe
        user.setPassword(PasswordUtils.PASSWORD_ENCODER.encode(request.getPassword()));

        //On connecte le user et on renvoi le token
        return tokenAuthenticationService.connectUser(user.getLogin());
    }

    /**
     * Génère un nouveau keyPass et envoi le mail de resetPassword pour l'email renseigné
     * @param login
     * @throws MarketPayException
     */
    public void sendKeyPass(String login, boolean createMode, LANGUAGE language) throws MarketPayException {
        //On récupère le user associé à l'email
        User user = userRepository.findByLogin(login).orElseThrow(() ->
            new MarketPayException(HttpStatus.BAD_GATEWAY, "Pas de user pour login" + login, "login")
        );

        //On vérifie qu'il n'y a pas déjà une demande de resetPassword en cours pour ce user
        Optional<UserKeyPass> oldUserKeyPassOpt = userKeyPassRepository.findByIdUser(user.getId());
        //Si c'est le cas on la supprime
        oldUserKeyPassOpt.ifPresent(oldUserKeyPass -> {
            userKeyPassRepository.delete(oldUserKeyPass);
        });

        //On génère le keyPass
        String keyPass = RandomUtils.getRandowString(30);

        //On génère la date d'expiration du keyPass
        LocalDateTime expirationDateTime = LocalDateTime.now();
        if(createMode){
            //En mode create la date d'expiration est plus longue
            expirationDateTime = expirationDateTime.plusMinutes(EXPIRATION_CREATE_RESET_KEY_PASS_MIN);
        } else {
            expirationDateTime = expirationDateTime.plusMinutes(EXPIRATION_RESET_KEY_PASS_MIN);
        }

        //On créé le userKeyPass
        UserKeyPass userKeyPass = new UserKeyPass();
        userKeyPass.setIdUser(user.getId());
        userKeyPass.setKeyPass(keyPass);
        userKeyPass.setExpirationDateTime(expirationDateTime);
        userKeyPassRepository.save(userKeyPass);

        //On construit l'url du reset
        String urlReset = URL_RESET_KEY_PASS + keyPass;

        //On récupère l'objet du mail
        String subject = null;

        //On construit le body du mail
        EmailBody body;
        if(createMode){
            //body
            CreateEmailBody createBody = new CreateEmailBody();
            createBody.setLogin(user.getLogin());
            createBody.setUrlFirstConnection(urlReset);

            //On récupère le userResource pour avoir la BU et le shop
            UserResource userResource = userService.getUserResource(user);
            createBody.setShopName(userResource.getNameShop());
            createBody.setBuName(userResource.getNameBu());

            body = createBody;

            //subject
            subject = i18nUtils.getMessage("mail.create.subject", null, language);
        } else {
            //body
            ResetEmailBody resetBody = new ResetEmailBody();
            resetBody.setUrlReset(urlReset);
            body = resetBody;

            //subject
            subject = i18nUtils.getMessage("mail.reset.subject", null, language);
        }

        body.setFirstName(user.getFirstName());
        body.setLastName(user.getLastName());

        try {
            //On construit le mail
            List<String> toList = new ArrayList<>();
            toList.add(user.getEmail());

            MarketPayEmail marketPayEmail = mailBuilder.build(
                toList,
                null,
                subject,
                body,
                language);

            //On envoi le mail
            mailService.sendMail(marketPayEmail);
        } catch (Exception e) {
            //Si une erreur est survenue pendant l'envoi du mail on rollback
            userKeyPassRepository.delete(userKeyPass);
            if((e instanceof MarketPayException)){
                throw new MarketPayException(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur est survenue lors de l'envoi du mail", e);
            } else {
                throw e;
            }
        }

    }

}

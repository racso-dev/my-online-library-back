package com.marketpay.services.keypass;

import com.marketpay.api.keypass.request.ResetPasswordRequest;
import com.marketpay.exception.EntityNotFoundException;
import com.marketpay.exception.MarketPayException;
import com.marketpay.persistence.entity.User;
import com.marketpay.persistence.entity.UserKeyPass;
import com.marketpay.persistence.repository.UserKeyPassRepository;
import com.marketpay.persistence.repository.UserRepository;
import com.marketpay.services.auth.TokenAuthenticationService;
import com.marketpay.utils.PasswordUtils;
import com.marketpay.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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

        //On check le password
        //TODO ETI ou dans la request

        //On change le mot de passe
        user.setPassword(PasswordUtils.PASSWORD_ENCODER.encode(request.getPassword()));

        //On connecte le user et on renvoi le token
        return tokenAuthenticationService.connectUser(user.getLogin());
    }

    /**
     * Génère un nouveau keyPass et envoi le mail de resetPassword pour l'email renseigné
     * @param email
     * @throws MarketPayException
     */
    public void sendKeyPass(String email, boolean createMode) throws MarketPayException {
        //On récupère le user associé à l'email
        User user = userRepository.findUserByEmail(email).orElseThrow(() ->
            new MarketPayException(HttpStatus.BAD_GATEWAY, "Pas de user pour l'email " + email, "email")
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

        //On envoi le mail
        //TODO ETI
        System.err.println(keyPass);
    }

}

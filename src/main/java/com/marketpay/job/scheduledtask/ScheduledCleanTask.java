package com.marketpay.job.scheduledtask;

import com.marketpay.persistence.repository.UserKeyPassRepository;
import com.marketpay.persistence.repository.UserTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Created by etienne on 16/08/17.
 */
@Component
public class ScheduledCleanTask {

    private final Logger LOGGER = LoggerFactory.getLogger(ScheduledCleanTask.class);

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Autowired
    private UserKeyPassRepository userKeyPassRepository;

    /**
     * Task qui clean les userToken périmé et les userKeyPass périmé
     * Elle s'exécute tous les jours à 3h
     */
    @Scheduled(cron = "0 0 3 * * *")
    private void schedulerClean() {
        LOGGER.info("*************** START Scheduled Clean Task ***************");

        //On récupère les userToken périmé et on les supprime
        userTokenRepository.findByExpirationDateTimeLessThan(LocalDateTime.now()).forEach(userToken -> {
            try {
                userTokenRepository.delete(userToken);
            } catch (Exception e) {
                //Si il y a une erreur, on ne veut pas que ça soit bloquant
                LOGGER.info("Erreur lors de la suppression du userToken " + userToken.getId(), e);
            }
        });

        //On récupère les userKeyPass périmé et on les supprime
        userKeyPassRepository.findByExpirationDateTimeLessThan(LocalDateTime.now()).forEach(userKeyPass -> {
            try {
                userKeyPassRepository.delete(userKeyPass);
            } catch (Exception e) {
                //Si il y a une erreur, on ne veut pas que ça soit bloquant
                LOGGER.info("Erreur lors de la suppression du userKeyPass " + userKeyPass.getId(), e);
            }
        });

        LOGGER.info("*************** END Scheduled Clean Task ***************");
    }
}
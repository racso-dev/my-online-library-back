package com.steamulo.scheduledtask;

import com.steamulo.persistence.repository.UserTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Exemple d'utilisation @Scheduled
 * Pour activer les schedule task, ajouter l'annotation @EnableScheduling dans Application.java
 */
@Component
public class ScheduledTask {

    private final Logger LOGGER = LoggerFactory.getLogger(ScheduledTask.class);

    @Autowired
    private UserTokenRepository userTokenRepository;

    /**
     * Task qui clean les userToken périmé et les userKeyPass périmé
     * Elle s'exécute tous les jours à 3h
     * cron : cron expression. Pour exécuter des tâches pédiodiques
     * min	heure   jour/mois	mois	jour/semaine	Périodicité
     *	*	 *	     *	         *	       *            Toutes les minutes
     *  30	 0	     1	        1,6,12	   *	        à 00:30 le premier janvier, juin et décembre
     *	0    20	     *	        10	       1-5	        à 20:00 chaque jour de la semaine (du lundi au vendredi) d’octobre
     *	0	 0       1,10,15	*	       *	        à minuit les premiers, dixièmes, et quinzième jours de chaque mois
     * 5,10	 0	     10	        *	       1	        à 00:05 et 00:10 chaque lundi et le 10 de chaque mois
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
        LOGGER.info("*************** END Scheduled Clean Task ***************");
    }

    /**
     * fixedDelay : Durée entre la fin de la dernière exécution et le début de la prochaine exécution est figée.
     * La tâche attend toujours que la tâche précédente soit terminée
     */
    @Scheduled(fixedDelay = 2000)
    public void scheduleFixedDelayTask() {
        LOGGER.info("Fixed delay task - " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

    /**
     * fixedRate : Temps fixe. La tâche va s'exécuter sans attendre la fin de l'exécution précédente. A utiliser
     * seulement pour des tâches indépendantes
     */
    @Scheduled(fixedRate = 5000)
    public void scheduleFixedRateTask() {
        LOGGER.info("Fixed rate task - " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

    /**
     *  initialDelay : Temps initial avant que la tâche s'éxécute
     */
    @Scheduled(fixedDelay = 10000, initialDelay = 10000)
    public void scheduleFixedRateWithInitialDelayTask() {
        LOGGER.info("Fixed rate task with initial delay - " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

}

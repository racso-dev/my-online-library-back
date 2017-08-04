package com.marketpay.job.scheduledsftp;

import com.jcraft.jsch.*;
import com.marketpay.conf.SFTPConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by etienne on 03/07/17.
 */
@Component
public class ScheduledSFTPTasks {

    @Autowired
    private SFTPConfig sftpConfig;

    private JSch jSch = new JSch();
    private Session session;
    private ChannelSftp channelSftp;


    //TODO ETI
    @Scheduled(fixedRate = 30000)
    private void schedulerSFTP() {
        System.err.println(System.currentTimeMillis());
        //TODO ETI
    }


    /**
     * Se connecte au serveur SFTP en utilisant une clé publique
     * @param publicKeyPath path de la clé
     * @throws JSchException
     */
    public void connectWithPublicKey(String publicKeyPath) throws JSchException {
        // Création de la session JSch
        jSch.addIdentity(publicKeyPath);
        session = jSch.getSession(sftpConfig.getUser(), sftpConfig.getHost(), sftpConfig.getPort());

        // Connexion au serveur SFTP
        //TODO ETI
//        session.setConfig(config);
        session.connect();

        // Ouverture du canal
        channelSftp = (ChannelSftp)session.openChannel("sftp");
        channelSftp.connect();
    }

    /**
     * Change de dossier
     * @param folder
     * @throws SftpException
     */
    public void changeDir(String folder) throws SftpException {
        channelSftp.cd(folder);
    }

    /**
     * Liste tous les fichiers du dossier indiqué, sauf le dossier courant (.) et le dossier parent (..). Ne liste pas
     * les sous-dossiers.
     * @param folder le dossier à lister
     * @return une liste de noms de fichiers.
     * @throws SftpException
     */
    public List<String> getList(String folder) throws SftpException {
        //On récupère la liste depuis le sftp, on ne garde que les noms de fichier
        List<String> fileNameList = new ArrayList<String>();

        // La commande "ls" renvoie en fait un Vector d'entrée ls (avec les mêmes champs qu'un ls sous unix).
        new ArrayList<ChannelSftp.LsEntry>(channelSftp.ls(folder)).forEach(file -> {
            if (!file.getFilename().equals(".") && !file.getFilename().equals("..")) {
                fileNameList.add(file.getFilename());
            }
        });

        return fileNameList;
    }

    /**
     * Copie un fichier du serveur SFTP sur le disque local
     * @param nameSftp le nom du fichier distant dans le répertoire courant
     * @param pathLocal le chemin local où copier ce fichier.
     * @throws SftpException
     */
    public void getFile(String nameSftp, String pathLocal) throws SftpException {
        channelSftp.get(nameSftp, pathLocal);
    }

    /**
     * Envoie un fichier sur le serveur SFTP dans le dossier courant, en gardant le même nom.
     * @param fichier
     * @throws FileNotFoundException
     * @throws SftpException
     */
    public void sendFile(File fichier) throws FileNotFoundException, SftpException {
        channelSftp.put(new FileInputStream(fichier), fichier.getName());
    }

    /**
     * Supprimer un fichier du serveur SFTP.
     * @param fileName
     * @throws SftpException
     */
    public void removeFile(String fileName) throws SftpException {
        channelSftp.rm(fileName);
    }

    /**
     * Se déconnecte proprement du serveur SFTP (canal puis session)
     */
    public void disconnect() {
        if (channelSftp != null) {
            channelSftp.quit();
            channelSftp.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }
}

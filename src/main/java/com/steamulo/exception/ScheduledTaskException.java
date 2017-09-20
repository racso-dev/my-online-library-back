package com.steamulo.exception;

/**
 * Created by etienne on 08/08/17.
 */
public class ScheduledTaskException extends Exception {

    private String incommingPath;
    private String archivePath;

    public ScheduledTaskException(String message, Throwable cause, String incommingPath, String archivePath) {
        super("Error lors de la tache de récupération des fichiers à parser\n" +
              "incommingPath : " + incommingPath + "\n" +
              "archivePath : " + archivePath + "\n" +
              "Message : " + message, cause);
        this.incommingPath = incommingPath;
        this.archivePath = archivePath;
    }

    public ScheduledTaskException(String message, String incommingPath, String archivePath) {
        super("Error lors de la tache de récupération des fichiers à parser\n" +
              "incommingPath : " + incommingPath + "\n" +
              "archivePath : " + archivePath + "\n" +
              "Message : " + message);
        this.incommingPath = incommingPath;
        this.archivePath = archivePath;
    }

    public String getIncommingPath() {
        return incommingPath;
    }

    public String getArchivePath() {
        return archivePath;
    }

}

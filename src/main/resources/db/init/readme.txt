***************************************************************************
Répertoire pour les fichiers fichier d'initialisation de la base par Flyway

Syntaxe pour le nommage:

V{numero}__INIT_{type}.sql

numero :
    pour l'ordre d'execution des scripts, exemple: 01, 02, 03, ...

type :
    DB => creation du schema de la base et des tables
          (ce fichier doit être mis à jour au fur et à mesure avec les deltas STR car pour chaque montée de version on
          supprime les deltas et on les concatène dans les fichier INIT)

    USER => creation modification des user pour la base

    REF => ajout de ligne dans une table pour des données de reference
           (ce fichier doit être mis à jour au fur et à mesure avec les deltas REF car pour chaque montée de version on
           supprime les deltas et on les concatène dans les fichier INIT)

exemple :
    V01_INIT_DB.sql

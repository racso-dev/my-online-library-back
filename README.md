# Market Pay API

## TODO
Script dev qui complète la table Block.

## Nom des commits
Chaque nom de commit doit être préfixé par l'une des valeurs suivantes :
 - [EVOL] Ajout de logique
 - [ADD] Ajout sans logique / Commentaire
 - [FIX] Correction de bug
 - [CLEAN] Suppression de code mort
 - [FIX MR] Correction des retours de merge request

## Run configuration projet
Le fichier application.yml représente la configuration par défaut.
Dans le sous dossier dev, on retrouve les fichiers de config application-dev.yml qui rajoute des éléments de conf pour developper.
Puis on trouve les autres fichiers yml qui correspondent au conf individuel.

### Utilisation des fichiers de configuration
Pour utiliser un fichier de conf en particulier, il faut créer / éditer une configuration de Run projet et rajouter en VM Options :

```
-Dspring.config.location=classpath:dev/ -Dspring.profiles.active=XXX,...
```
Par exemple 
```
-Dspring.config.location=classpath:dev/ -Dspring.profiles.active=dev,atran
```


TODOux Thomas C. et Antony T.


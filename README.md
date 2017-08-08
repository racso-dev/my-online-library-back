# Market Pay API

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

### Fichier de conf à compléter pour le path du sftp en dev avec par exemple:
```
sftp:
  pathIncomming: /home/etienne/workspaceMarketPay/incomming
  pathArchive: /home/etienne/workspaceMarketPay/archive
```

## TIPS
Si le port que vous souhaitez utiliser n'est pas disponible et que vous voulez le libérer :
 - lsof -i :'port' (par exemple lsof -i :3000)
 - kill -9 'PID'

## Swagger
Swagger est sur l'url: http://localhost:8000/swagger-ui.html

## Gestion des droits en fonction du profile

La gestion des droits par profile se fait via l'intercepteur. Pour ce faire sur chaque method de controller (WS) il faut
ajouter l'annotation @Profile. Qui prend en paramètre un array de USER_PROFILE autorisés à accéder au WS.
Si l'array est vide alors tous les profiles sont autorisé.

## Annotations particulières

L'annotation @Dev permet de dire que le WS, sur lequel est mise @Dev, est accessible uniquement en mode dev.
C'est à dire que la conf dev est utilisée.

L'annotation @NotAuthenticated permet de dire que le au WS, sur lequel est mise @NotAuthenticated, est accessible sans être authentifié.
Biensûr cela ne suffit pas à rendre accessible le WS sans authentification, il faut également autoriser la route dans WebSecurityConfig.

## Réponse HTTP des WS et gestion des Exception (MarketPayException)

Pour renvoyer comme réponse à un WS un code HTTP autre que 200 (dans le cas d'une erreur fonctionnelle ou technique),
il suffit de throw une MarketPayException qui prend en paramètre un code HTTP et un message. Celle ci sera attrapée par
un ExceptionHandler et renvoyée en réponse au WS avec le code HTTP spécifié. Cette exception peut être appelée partout
à tout moment et elle stop le traitement en cours pour renvoyer l'erreur souhaitée.



TODOux Thomas C. et Antony T.


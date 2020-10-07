# STEAMULO API STARTER
Ce projet spring boot est un starter pour la création d'une api. Il contient la logique applicative de 
base d'une api rest, une gestion de l'authentification, des utilisateurs et des exceptions...

## Run
### Pré requis
* [Java 11](https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.5%2B10_openj9-0.17.0/OpenJDK11U-jdk_x64_linux_openj9_linuxXL_11.0.5_10_openj9-0.17.0.tar.gz)
* Maven 3.6.2 ou plus (voir https://maven.apache.org/install.html ou utiliser le gestionnaire de package de votre distribution)
* Mysql 5.7 + Serveur SMTP :
    * [installer docker](https://docs.docker.com/install/)
    * [installer docker-compose](https://docs.docker.com/compose/install/)
    * ```sudo docker-compose up```

### Configuration
* Le fichier application.yml représente est la configuration de base.
* Le fichier application-test.yml est la configuration pour les tests.
* Le fichier application-dev.yml est la configuration pour les devs.

Il est recommandé d'utiliser les services installés via docker. Pour développer il est seulement nécéssaire d'activer le profile "dev"
dans la configuration de lancement d'Intellij. Pour ce faire éditer dans l'onglet "Configuration" puis dans la section Spring boot le champ "Active profiles" et y ajouter "dev".

Dans la liste déroulante "On 'Update' action" choisir "Update classes and ressources" pour tirer parti des spring-boot-devtools via le raccourci CTRL+F9.
Dans la liste déroulante "On frame deactivation" choisir "Update classes and ressources" pour tirer parti des spring-boot-devtools lors du changement de fenêtre

### Liens
* [API](http://localhost:8000)
* [Swagger](http://localhost:8000/swagger-ui.html)
* [MailDev](http://localhost:8081)


## Développement

### Référence et documentation externes (RTFM)
* Framework : [Spring Boot 2.2.1](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/reference/htmlsingle/)
* Migration de base de données: [Flyway](https://flywaydb.org/)
* Génération de Setter/Getter: [Lombock](https://projectlombok.org/)

### Gestion des droits en fonction du role
La gestion des droits et des accès est réalisée via Spring Security. L'annotation @PreAuthorize permet de vérifier
l'accès à un endpoint. Les vérifications métier peuvent également être faite via l'appel de service dans l'annotation.
Voir la documentation : https://docs.spring.io/spring-security/site/docs/3.0.x/reference/el-access.html

### Réponse HTTP des WS et gestion des Exception (ApiException)
Pour renvoyer comme réponse à un WS un code HTTP autre que 200 (dans le cas d'une erreur fonctionnelle ou technique),
il suffit de throw une ApiException qui prend en paramètre un code HTTP et un message. Celle ci sera attrapée par
un ExceptionHandler et renvoyée en réponse au WS avec le code HTTP spécifié. Cette exception peut être appelée partout
à tout moment et elle stop le traitement en cours pour renvoyer l'erreur souhaitée.

### Authentification
JWT est utilisé au pour l'authentification, ce qui veut dire que pour les appels nécessitant une authentification, 
il faut avoir le token récupéré par l'appel /auth/login et l'envoyer en header de la requete sous la forme :
key: Authorization
value: Bearer ${your-token}

## Intégration continue
Le projet est livré avec un jenkinsfile de base, quelques variables sont a completer (todo ou valeur entre '<>').
Seule un entrée multibranch pipeline pointant vers le repo git du projet est necessaire dans jenkins.
Il est également conseillé de mettre en place un webhook sur gitlab afin de declancher le scan des branches a chaque push sur le projet.
L'url à utiliser est la suivante : https://gitlab:111168cf050f3a6848774fb59e4c321fdb@ci.steamulo.com/job/<Chemin vers le job>/build?delay=0sec

### Changelog
Tous les changements notables seront listés dans le fichier CHANGELOG.md
Le format du fichier CHANGELOG.md provient de [Keep a Changelog](https://keepachangelog.com/en/1.0.0/)

# TODO
remplacer l'instance bcrypt par un BEAN
Regarder si les annotations @valid marche bien avec les erreurs en réponse, sinon reprendre le truc du basejump de julien

# STEAMULO API STARTER
Ce projet spring boot utilisant java 8 est un starter pour la création d'une api. Il contient la logique applicative de 
base d'une api rest, une gestion simplifié de l'authentification, des utilisateurs et des exceptions...

## Run
### Pré requis
* Java 8 
* Maven 3.3.3 ou plus (voir https://maven.apache.org/install.html ou utiliser le gestionnaire de package de votre distribution)
* Mysql 5.7 + Serveur SMTP :
    * [installer docker](https://docs.docker.com/install/)
    * [installer docker-compose](https://docs.docker.com/compose/install/)
    * ```sudo docker-compose up```

### Configuration
Le fichier application.yml représente la configuration par défaut.
Le fichier application-test.yml représente la configuration par défaut.
Le fichier application-dev.yml représente la configuration par défaut.

Il est recommandé d'utiliser les services installés via docker. Pour développer il est seulement nécéssaire d'activer le profile "dev"
dans la configuration de lancement d'Intellij. Pour ce faire éditer dans l'onglet "Configuration" puis dans la section Spring boot le champ "Active profiles" et y ajouter "dev".

Dans la liste déroulante "On 'Update' action" choisir "Update classes and ressources" pour tirer parti des spring-boot-devtools via le raccourci CTRL+F9.
Dans la liste déroulante "On frame deactivation" choisir "Update classes and ressources" pour tirer parti des spring-boot-devtools lors du changement de fenêtre

### Liens
* [API](http://localhost:8000)
* [Swagger](http://localhost:8000/swagger-ui.html)
* [MailDev](http://localhost:8081)
* [phpMyAdmin](http://localhost:8082)


## Développement

### Bibliothèques principales du projet
* Migration de base de données: [Flyway](https://flywaydb.org/)
* Génération de Setter/Getter: [Lombock](https://projectlombok.org/)

### Gestion des droits en fonction du role
La gestion des droits et des accès est réalisée via Spring Security. L'annotation @PreAuthorize permet de vérifier
l'accès a un endpoint. Les vérifications métier peuvent également être faite via l'appel de service dans l'annotation.
Voir la documentation : https://docs.spring.io/spring-security/site/docs/3.0.x/reference/el-access.html

### Réponse HTTP des WS et gestion des Exception (ApiException)
Pour renvoyer comme réponse à un WS un code HTTP autre que 200 (dans le cas d'une erreur fonctionnelle ou technique),
il suffit de throw une ApiException qui prend en paramètre un code HTTP et un message. Celle ci sera attrapée par
un ExceptionHandler et renvoyée en réponse au WS avec le code HTTP spécifié. Cette exception peut être appelée partout
à tout moment et elle stop le traitement en cours pour renvoyer l'erreur souhaitée.

### Authentification
JWT est utilisé au niveau de l'authentification, ce qui veut dire que pour les appels nécessitant une authentification, 
il faut avoir le token récupéré par l'appel /auth/login et l'envoyer en header de la requete sous la forme :
key: Authorization
value: Bearer ${your-token}

### Controller
Les requêtes HTTP sont traitées par les controllers. Ils sont identifiés par l'annotation **@RestController** et se trouve 
dans le package api/. Créer un package requête (Exemple: /user), dans lequel se trouve un package **request**
qui contient les inputs des requêtes, un package **response** qui contients les outputs des requêtes et le **controller**.
Attention à ne pas implémenter la logique métier dans les controllers, cette logique se trouve dans la couche **service**.
Utiliser l'annotation **@Autowired** pour liée les différents services au controller.

### Service
L'annotation **@Component** est utilisée pour chaque service.
Créer un package par objet métier dans lequel contient un package **ressource** qui contient les différents objets et 
le **service**.

### Persistence des données
Chaque entité est annoté par **@Entity** (/persistence/entity/), ce qui indique que c'est une entité JPA et qu'elle est 
mappé directement avec la table correspondante avec ces différents attributs. Si le nom d'un attribut ne correspond pas
exactemenent à celui en BDD utilisé l'annotation **@Column(name="nom")**.
Pour utiliser de simples queries, nous pouvons utiliser les interfaces CrudRepository.

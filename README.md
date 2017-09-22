# STEAMULO API STARTER

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

## Réponse HTTP des WS et gestion des Exception

Pour renvoyer comme réponse à un WS un code HTTP autre que 200 (dans le cas d'une erreur fonctionnelle ou technique),
il suffit de throw une MarketPayException qui prend en paramètre un code HTTP et un message. Celle ci sera attrapée par
un ExceptionHandler et renvoyée en réponse au WS avec le code HTTP spécifié. Cette exception peut être appelée partout
à tout moment et elle stop le traitement en cours pour renvoyer l'erreur souhaitée.

## Controller

Les requêtes HTTP sont traitées par les controllers. Ils sont identifiés par l'annotation **@RestController** et se trouve 
dans le package api/. Créer un package requête (Exemple: /user), dans lequel se trouve un package **request**
qui contient les inputs des requêtes, un package **response** qui contients les outputs des requêtes et le **controller**.
Attention à ne pas implémenter la logique métier dans les controllers, cette logique se trouve dans la couche **service**.
Utiliser l'annotation **@Autowired** pour liée les différents services au controller.

## Service

L'annotation **@Component** est utilisée pour chaque service.
Créer un package par objet métier dans lequel contient un package **ressource** qui contient les différents objets et 
le **service**.

## Persistence des données

Chaque entité est annoté par **@Entity** (/persistence/entity/*), ce qui indique que c'est une entité JPA et qu'elle est 
mappé directement avec la table correspondante avec ces différents attributs. Si le nom d'un attribut ne correspond pas
exactemenent à celui en BDD utilisé l'annotation **@Column(name="nom")**.
Pour utiliser de simples queries, nous pouvons utiliser les interfaces CrudRepository.

## Tâches planifiées

Pour créer des tâches planifiées, ajouter des méthodes dans le package src/main/java/com/steamulo/scheduledtask/* 
précédés par l'annotation **@Scheduled(...)** avec l'expression souhaité.
Attention à ne pas oublier d'ajouter l'annotation **@EnableScheduling** à Application.java pour activer ces tâches.


TODOux Thomas C. et Antony T.


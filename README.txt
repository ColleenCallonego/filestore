## Introduction

## Prérequis

### Keycloak Server

Notre filestore est conçu pour utiliser une authentification externe. Nous avons choisi le serveur d'authentification (IdP) Keycloak.
Pour cela, une instance de keycloak doit être installée et configurée. Cette instance est commune à tous les composants du OneClickFilestore.
Nous pouvons utiliser une image docker de keycloak :

Un documentation est disponible ici :

https://www.keycloak.org/getting-started/getting-started-docker

Je vous conseille de configurer un volume ce qui vous permettra de détruire le conteneur sans perdre vos données lors d'une mise à jours par exemple.

```
docker run --name keycloak -p 8080:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin123 -v /var/keycloak:/opt/jboss/keycloak/standalone/data quay.io/keycloak/keycloak:15.1.0
```

Il faudra créer le royaume "filestore" dans lequel il faudra déclarer un client filestore également.

### Consul Server

Pour que les composants puissent se découvrir et communiquer entre eux, nous allons également utiliser un annuaire de service commun à tous : Consul.

https://hub.docker.com/_/consul/

```
docker run -d --name=consul -p 8500:8500 -p 8600:8600 -e CONSUL_BIND_INTERFACE=eth0 consul
```

Consul tournera sur le port 8500, le port 8600 quand à lui sert à proposer un service de DNS que nous n'utiliserons pas pour le moment.

## Filestore

Chaque instance du filestore devra pouvoir fonctionner dans l'environnement cible avec consul et keycloak notamment. Pour cela, il est nécessaire de configurer le serveur d'application wildfly en conséquence, notamment pour Keycloak.
De plus, notre application utilise des sources de données et un topic de messages qu'il faut également configurer dans wildfly.

### Configuration de wildfly

### OIDC

Pour que Wildfly puisse valider les jetons d'authentification via Keycloak (Bear Token) il est nécessaire de configurer l'authentification OIDC. Depuis Wildfly 25, le support de OIDC est standard dans Elytron (couche de sécurité de wildfly)

http://www.mastertheboss.com/jbossas/jboss-security/secure-wildfly-applications-with-openid-connect/

Il suffit donc simplement de créer un fichier oidc.json dans le dossier WEB-INF et de configurer l'authentification dans web.xml.

### DataSource et Topic JMS

Pour pouvoir utiliser la persistence, ainsi qu'un topic JMS il faut partir du fichier de config standalone-full.xml (il inclut le sous système JMS qui n'est pas présent dans le standalone.xml de base) et ajouter ceci :

```
<server xmlns="urn:jboss:domain:18.0">
    (...)
    <subsystem xmlns="urn:jboss:domain:datasources:6.0">
        <datasources>
            (...)
            <datasource jndi-name="java:jboss/datasources/fsDS" pool-name="fsDS" enabled="true" use-java-context="true" statistics-enabled="${wildfly.datasources.statistics-enabled:${wildfly.statistics-enabled:false}}">
                <connection-url>jdbc:h2:~/.filestore/filestore;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE</connection-url>
                <driver>h2</driver>
                <security>
                    <user-name>sa</user-name>
                    <password>sa</password>
                </security>
            </datasource>
            (...)
        </datasources>
    </subsystem>
    (...)
    <subsystem xmlns="urn:jboss:domain:messaging-activemq:13.0">
        <server name="default">
            (...)
            <jms-topic name="notification" entries="java:/jms/topic/notification"/>
            (...)
        </server>
    </subsystem>
    (...)
</server>
```

### Docker image

Comme le filestore est aussi packagé sous forme de conteneur docker il est possible de le lancer avec la commande suivante :

```bash
docker run -d --name=filestore -p 8280:8080 -e FILESTORE_OWNER=jerome -e FILESTORE_CONSULHOST=172.17.0.1 -e FILESTORE_AUTH_URI=http://172.17.0.1:8080/auth/realms/filestore blanchard/filestore:21.1-SNAPSHOT
```


## Manager


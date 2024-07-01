# <span style="color: blue;"> Projet 9 : MediLabo Solutions (MLS) - PM</span>

Le projet MédiLabo Solutions fournit des informations sur les patients par rapport à leur risque de diabète.
Le projet est organisé avec plusieurs microservices qui communiquent entre eux :
>- **front-mls** : qui représente le front avec ne base de données mysql qui stocke les users
>- **mls-coordonnees-patient** : qui représente un service back-end qui renvoie les coordonnées des patients. 
>Les données sont stockées dans une base de données mysql partagée avec le service front pour respecter certains principes du green code.
>- **mls-notes-patient** : qui représente un service back-end qui renvoie les notes du praticien sur le patient
>- **mls-gateway** : qui est la gateway protégeant les appels aux services back-end. Le front n'appellera pas directement les services back.
>- **eureka-server** : qui est un edge microservice représentant un server de registre détectant les microservices et leur état une fois déclaré en tant que client.

## <span style="color: red;">Pré-requis</span>

Les données injectées dans la base de données mysql sont présentes [ici](./front-mls/data/data.sql).
Pour la partie container, les bases de données mysql et plus tard mongo auront des volumes pour persister les données.

## <span style="color: grey;">Architecture du projet</span>

## <span style="color: white;">Déploiement du projet</span> 

## <span style="color: limegreen;">Green code</span>

- Utilisation de [squoosh](https://squoosh.app/) pour mettre les images au format webp et les compresser au maximum :
  - passage au format webp
  - gain sur la taille de l'image : 154 ko à 6 ko
- Partage du serveur de base de données _**MySQL**_ avec deux schémas :
  - un schéma **mls** qui contient la tables des utilisateurs de l'application et la table des structures associées aux utilisateurs et qui est liée au service front **mls-front**
  - un schéma **mls_coordonnees** qui contient les données des coordonnées des patients et qui est liée au service back-end **mls-coordonnees-patient**
# <span style="color: blue;"> Projet 9 : MediLabo Solutions (MLS) - PM</span>

Le projet MédiLabo Solutions fournit des informations sur les patients par rapport à leur risque de diabète.
Le projet est organisé avec plusieurs microservices qui communiquent entre eux :
- **front-mls** : qui représente le front avec ne base de données mysql qui stocke les users
- **mls-coordonnees-patient** : qui représente un service back-end qui renvoie les coordonnées des patients. 
Les données sont stockées dans une base de données mysql partagée avec le service front pour respecter certains principes du green code.
- **mls-notes-patient** : qui représente un service back-end qui renvoie les notes du praticien sur le patient
- **mls-gateway** : qui est la gateway proté&geant les appels aux services back-end. Le front n'appellera pas directement les services back.
- **eureka-server** : qui est un edge microservice représentant un server de registre détectant les microservices et leur état une fois déclaré en tant que client.

## <span style="color: grey;">Architecture du projet</span>

## <span style="color: white;">Déploiement du projet</span> 

## <span style="color: limegreen;">Green code</span>

- Utilisation de [squoosh](https://squoosh.app/) pour mettre les images au format webp et les compresser au maximum :
  - passage au format webp
  - gain sur la taille de l'image : 154 ko à 6 ko
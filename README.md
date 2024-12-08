# Qu'est-ce ?
Il s'agit d'un projet personnel Java-Processing 4.0 qui a pour but de m'entraîner dans la création de l'architecture d'un jeu multijoueur en monde ouvert sur un réseau local.

# Comment fonctionne-t-il ?
Le client n'est qu'une interface qui récupère et gère les données récupérées du server qui détient toutes les informations.
Que ce soit en solo ou multi, la fenêtre de jeu ne communique qu'avec un server, qu'il soit sur la machine ou autre part.
Je n'ai utilisé aucune bibliothèque autre que le module réseau et le coeur Processing, qui permet simplement d'envoyer des données en chaînes de caractères, pour créer la communication entre le client et server, tout est made by Kiment !

# Pourquoi ?
J'ai 2-3 autres projets en réseau local mais aucun n'a vraiment été au point de fonctionnement, j'ai donc mis l'accent sur la conception de l'architecture pour ce projet. Une fois que j'aurai complètement compris et créé mon interface client-server, je pourrai la réutiliser pour créer beaucoup d'autres jeux en multijoueur local.
L'objectif est de créer un moteur pour mes futurs projets.

# Comment lancer le jeu ?
Après compilation avec Gradle, World.java lance un serveur et OpenMonsterHunter.java lance le jeu.
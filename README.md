# Qu'est-ce ?
Il s'agit d'un petit projet personnel Java Processing 4.0 qui a pour but de m'entraîner dans l'architecture d'un jeu multijoueur en monde ouvert sur un réseau local.

# Comment fonctionne-t-il ?
Le client n'est qu'une interface qui récupère et gère les données récupérées du server qui détient toutes les informations.
Que ce soit en solo ou multi, la fenêtre de jeu ne communique qu'avec un server, qu'il soit sur la machine ou autre part.
Je n'ai utilisé aucune bibliothèque autre que le .net de Processing, qui permet simplement d'envoyer des données en chaînes de caractères, pour créer la communication entre le client et server, tout est made by Kiment !

# Pourquoi ?
J'ai 2-3 autres projets en réseau local mais aucun n'a vraiment été au point de fonctionnement, j'ai donc mis l'accent sur la conception de l'architecture pour ce projet. Une fois que j'aurai complètement compris et créé mon interface client-server, je pourrai la réutiliser pour créer beaucoup d'autres jeux en multijoueur local.

# Comment lancer le jeu ?
Le server et le client partagent le même code, il suffit alors de lancer OpenMonsterHunter.java pour lancer le client et World.java pour lancer un server. Une instance World.java est lancée automatiquement quand on clique sur "Jouer solo" dans le client.

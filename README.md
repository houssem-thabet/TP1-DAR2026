# TP1-DAR2026 — Les sockets en mode connecté TCP

Matière : Développement d'applications réparties — LSI 3, Faculté des Sciences de Sfax (2026 – 2027)

IDE utilisé : IntelliJ IDEA — Langage : Java

## Structure du projet

```
TP1-DAR2026/
├── src/
│   ├── serverPackage/Server.java     version finale (Activité 1-3)
│   └── clientPackage/Client.java     version finale (Activité 1-3)
├── activites/
│   ├── activite-1-1/                 code de l'Activité 1-1
│   └── activite-1-2/                 code de l'Activité 1-2
└── README.md
```

Le dossier `activites/` conserve le code des activités 1-1 et 1-2 (fichiers `.java.txt`, pour ne pas être compilés avec `src/`). Le code de `src/` est la version finale de l'Activité 1-3.

---

## Activité 1-1 : Première connexion Client/Serveur

### Étape 1 : Vérification de Git

Commande : `git --version`

Description : la commande affiche la version de Git installée, ce qui confirme que Git est disponible dans le PATH.

### Étape 2 : Vérification de Java

Commande : `java --version`

Description : la commande affiche la version du JDK installé (>= 1.8 requis).

Dossier de travail créé : `votreNomPrenom_LSI3_DevAppReparties`

### Étape 3 : Création du serveur

Projet IntelliJ `TP1-DAR2026` créé avec la case « Git » cochée. Package `serverPackage`, classe `Server` avec `main()`.

Les parties TODO complétées :

- création du `ServerSocket` sur le port 5000 ;
- `accept()` pour attendre le client ;
- fermeture du `Socket` et du `ServerSocket`.

Interprétation : le serveur affiche « Je suis un serveur en attente la connexion d'un client » et reste bloqué sur `accept()`. Il attend qu'un client se connecte. Le programme ne se termine pas tant qu'aucun client n'arrive.

### Étape 4 : Création du client

Package `clientPackage`, classe `Client` avec `main()`.

Les parties TODO complétées :

- création du `Socket` vers `localhost` sur le port 5000 ;
- fermeture du `Socket`.

Ordre de lancement : le serveur d'abord, puis le client.

Interprétation : le client affiche « Je suis un client pas encore connecté… » puis « je suis un client connecté » une fois la connexion acceptée. Côté serveur, le message « un client est connecté » s'affiche au même moment : la connexion TCP est établie.

Si le client est lancé sans serveur, une exception `Connection refused` apparaît : aucun processus n'écoute sur ce port.

Changement de `localhost` par une adresse IP (ici `127.0.0.1`, puis l'IP de la machine) :

Interprétation : le résultat est identique, `localhost` est simplement le nom associé à `127.0.0.1`.

### Étape 5 : Dépôt

Lien du repository déposé sur la plateforme ENT.

---

## Activité 1-2 : Service de calculatrice

Le client saisit un entier `x`, l'envoie au serveur, qui renvoie `x * 5`.

Classes utilisées : `java.io.DataOutputStream` (`writeInt`, `writeDouble`) et `java.io.DataInputStream` (`readInt`, `readDouble`).

### Test avec différentes valeurs

Interprétation :

- `x = 4` donne `20` ;
- `x = -3` donne `-15` : `DataInputStream` / `DataOutputStream` envoient les 4 octets de l'entier tel quel, donc les négatifs sont transmis correctement ;
- `x = 0` arrête la communication.

### Saisies successives et arrêt propre

- le client continue tant que `x != 0` ;
- chaque saisie est envoyée, le résultat est lu puis affiché ;
- quand `x = 0`, le client envoie 0 puis ferme ses flux et son socket ; le serveur reçoit 0, sort de sa boucle et ferme les siens.

---

## Activité 1-3 : Amélioration du service de calculatrice

### Étape 1 : Deux machines distinctes

- `java.net.InetAddress` et `java.net.InetSocketAddress` gèrent l'adresse IP et le port (5000) ;
- **Serveur** : le `ServerSocket` est lié (`bind`) à l'adresse IP réelle de la machine, détectée automatiquement. On peut aussi la fournir en argument du programme.
- **Client** : au lancement, le client demande l'adresse IP du serveur (au lieu de `localhost`).

Procédure :

1. relever l'IP du serveur (`ipconfig` sous Windows) ;
2. lancer `Server` sur la machine serveur : l'adresse et le port s'affichent ;
3. lancer `Client` sur l'autre machine du même réseau local et saisir cette IP ;
4. si la connexion est refusée, autoriser le port 5000 dans le pare-feu Windows de la machine serveur.

### Étape 2 : Calculatrice avec quatre opérations

Menu côté client :

```
1 - Addition
2 - Soustraction
3 - Multiplication
4 - Division
0 - Quitter
```

Protocole :

1. le client envoie le choix (`int`), puis `x` (`int`) ;
2. le serveur calcule `x <opération> 5` ;
3. le serveur renvoie le résultat (`double`) ;
4. le choix `0` ferme proprement la communication.

La valeur `5` est la constante `OPERANDE` de `Server`. Le résultat est un `double`, donc la division n'est pas tronquée (`9 / 5 = 1.8`).

Tests effectués :

| Choix | x  | Résultat |
|-------|----|----------|
| Addition | 7  | 12.0 |
| Soustraction | -3 | -8.0 |
| Multiplication | 4  | 20.0 |
| Division | 9  | 1.8 |

---

## Lancer le projet dans IntelliJ

1. Ouvrir le projet `TP1-DAR2026`.
2. Clic droit sur `serverPackage/Server.java` puis **Run 'Server.main()'**.
3. Clic droit sur `clientPackage/Client.java` puis **Run 'Client.main()'** (les deux consoles sont indépendantes).
4. Saisir l'adresse IP du serveur quand le client la demande.

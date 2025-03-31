# Prérequis

* JDK 23
* Maven
* Git
* Docker
* Un IDE Java

# Lancer le projet en local

1. Cloner le projet dans un dossier avec
```
git clone https://github.com/trapping703/wacdo.git
```
2. Ouvrir le projet sur l'IDE depuis le pom.xml du projet


3. Lancer la base de données PostgreSQL avec la commande Docker depuis le dossier du projet
```
docker compose up
```
4. Lancer l'application en exécutant WacdoApplication.java avec ces variables d'environnements (modifier l'adresse et port de la BDD si nécessaire) :
```
DATABASE_URL=jdbc:postgresql://localhost:5432/wacdo;DATABASE_USER=postgres;DATABASE_PASSWORD=
```
5. Se connecter avec le compte admin avec ces identifiants
```
mail:victor-g@wacdo.fr
mdp:test
```
# Envrionnement déployé

Vous pouvez accéder à l'application sur l'url ci-dessous, si l'instance était inactive, elle peut prendre jusqu'à une minute à être relancé.

- [wacdo.onrender.com](https://wacdo.onrender.com)

vous pouvez vous connecter avec le compte admin avec ces identifiants
```
mail:victor-g@wacdo.fr
mdp:test
```
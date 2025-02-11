CREATE EXTENSION IF NOT EXISTS pg_trgm;
-- https://mazeez.dev/posts/pg-trgm-similarity-search-and-fast-like


create table Employe(
    id int GENERATED BY DEFAULT AS IDENTITY primary key,
    prenom varchar,
    nom varchar,
    email varchar,
    motDePasse varchar,
    dateEmbauche date,
    admin boolean not null
);

create table Restaurant(
    id int GENERATED BY DEFAULT AS IDENTITY primary key,
    nom varchar,
    adresse varchar,
    codePostal int,
    ville varchar
);

create table Fonction (
    id int GENERATED BY DEFAULT AS IDENTITY primary key,
    libelle varchar
);

create table Affectation(
    id int GENERATED BY DEFAULT AS IDENTITY primary key,
    dateDebut date,
    dateFin date,
    employe_id int references Employe(id),
    restaurant_id int references Restaurant(id),
    fonction_id int references Fonction(id)
);

insert into Restaurant(id, nom, adresse, codePostal, ville) values (1,'Wacdo laMax', '4 rue de Nice',57000, 'Nice');
insert into Restaurant(id, nom, adresse, codePostal, ville) values (2,'Wacdo yana', '5 rue de Paris',67000, 'Paris');
insert into Restaurant(id, nom, adresse, codePostal, ville) values (3,'Wacdo adipiscing', '6 rue de Lyon',77000, 'Lyon');
insert into Restaurant(id, nom, adresse, codePostal, ville) values (4,'Wacdo consectetur', '7 rue de Strasbourg',87000, 'Strasbourg');
insert into Restaurant(id, nom, adresse, codePostal, ville) values (5,'Wacdo amet', '8 rue de Montpelier',97000, 'Montpelier');

insert into Fonction(id, libelle) values (1,'manager');
insert into Fonction(id, libelle) values (2,'cuisinier');
insert into Fonction(id, libelle) values (3,'serveur');

insert into Employe(id, prenom, nom, email, motDePasse, dateEmbauche, admin) values (1,'Victor', 'G.', 'victor-g@wacdo.fr', '', current_date,false);
insert into Employe(id, prenom, nom, email, motDePasse, dateEmbauche, admin) values (2,'Mathieu', 'P.', 'mathieu-p@wacdo.fr', '', current_date,false);
insert into Employe(id, prenom, nom, email, motDePasse, dateEmbauche, admin) values (3,'Quentin', 'F.', 'quentin-f@wacdo.fr', '', current_date,false);
insert into Employe(id, prenom, nom, email, motDePasse, dateEmbauche, admin) values (4,'Julien', 'X.', 'julien-x@wacdo.fr', '', current_date,false);
insert into Employe(id, prenom, nom, email, motDePasse, dateEmbauche, admin) values (5,'Charles', 'W.', 'charles-w@wacdo.fr', '', current_date,false);
insert into Employe(id, prenom, nom, email, motDePasse, dateEmbauche, admin) values (6,'Victor', 'Z.', 'victor-z@wacdo.fr', '', current_date,false);

insert into Affectation(dateDebut, dateFin, employe_id, restaurant_id, fonction_id) values (CURRENT_DATE - INTERVAL '1 year', null, 1,1,1);
insert into Affectation(dateDebut, dateFin, employe_id, restaurant_id, fonction_id) values (CURRENT_DATE - INTERVAL '1 year', CURRENT_DATE - INTERVAL '1 month', 2,1,2);
insert into Affectation(dateDebut, dateFin, employe_id, restaurant_id, fonction_id) values (CURRENT_DATE - INTERVAL '1 year', CURRENT_DATE - INTERVAL '1 month', 3,1,3);
insert into Affectation(dateDebut, dateFin, employe_id, restaurant_id, fonction_id) values (CURRENT_DATE - INTERVAL '1 month', null, 1,1,2);
insert into Affectation(dateDebut, dateFin, employe_id, restaurant_id, fonction_id) values (CURRENT_DATE - INTERVAL '1 month', null, 1,1,3);

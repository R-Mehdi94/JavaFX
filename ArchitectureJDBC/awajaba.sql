drop database if exists awajaba ;
create database awajaba ;

use awajaba ;


create table Membre(
    id int(5) NOT NULL auto_increment,
	nomConnexion varchar(40) NOT NULL,
	mdp varchar(40) NOT NULL,
	email varchar(40) NOT NULL,
	dateInscription date NOT NULL,
	bloque boolean,
	primary key(id)
);

create table Specialiste(
    id int(5) NOT NULL auto_increment,
	libelle varchar(20) NOT NULL,
	primary key(id)
);

create table Avoir(
    idMembre int(5) NOT NULL,
    idSpecialiste int(5) NOT NULL,
    niveau int(2) NOT NULL,
	primary key(idMembre,idSpecialiste),
	foreign key(idMembre) references Membre(id),
    foreign key(idSpecialiste) references Specialiste(id)
);

insert into Membre(nomConnexion,mdp,email,dateInscription,bloque) values("mehdi94", "azerty", "mehdi94@gmail.com", "2022/11/07", false);
insert into Membre(nomConnexion,mdp,email,dateInscription,bloque) values("walid77", "azerty", "walid77@gmail.com", "2022/09/14", false);
insert into Membre(nomConnexion,mdp,email,dateInscription,bloque) values("Ahmed94", "azerty", "Ahmed94@gmail.com", "2022/01/21", false);

insert into Specialiste(libelle) values("Francaise");
insert into Specialiste(libelle) values("Marocaine");
insert into Specialiste(libelle) values("Pakistanaise");

insert into Avoir(idMembre,idSpecialiste,niveau) values(1,3,10);
insert into Avoir(idMembre,idSpecialiste,niveau) values(2,1,6);
insert into Avoir(idMembre,idSpecialiste,niveau) values(3,2,2);
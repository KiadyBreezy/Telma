drop database telma;
create database telma;
alter database telma owner to postgres;

\c telma

create table utilisateur(
    idUtilisateur serial primary key,
    numeroTelephone varchar(200),
    nom varchar(200) not null,
    mdp varchar(100)
);

CREATE UNIQUE INDEX numeroTelephone ON utilisateur (numeroTelephone);

create table token(
    idToken serial primary key,
    idUtilisateur int,
    tokenValue varchar(200),
    foreign key(idUtilisateur) references utilisateur(idUtilisateur)
);

create table test(
    id int,
    nom varchar(50)    
);
insert into test values (1,'Rakoto');

---Mongo
/*CREATE TABLE TypeOffre (
    idTypeOffre serial primary key,
    nom VARCHAR(50)
);

---Mongo
CREATE TABLE Unite (
    idUnite serial primary key,
    nom VARCHAR(50)
);

---Mongo
CREATE TABLE TypeDuree (
    idTypeDuree serial primary key,
    nom VARCHAR(50),
    valeur INTEGER,
    idUnite INTEGER,
    foreign key (idUnite) references Unite(idUnite)
);

CREATE TABLE Offre (
    idOffre serial primary key,
    nom VARCHAR(60),
    prix DOUBLE PRECISION,
    idTypeOffre INTEGER,
    idTypeDuree INTEGER,
    Foreign key (idTypeDuree) references TypeDuree(idTypeDuree),
    Foreign key (idTypeOffre) references TypeOffre(idOffre)
);


CREATE TABLE DetailOffre (
    idDetailOffre serial primary key,
    idOffre INTEGER,
    intervalleP Time,
    intervalleD Time,
    valeur DOUBLE PRECISION,
    idUnite INTEGER,
    mop INTEGER,
    aop INTEGER,
    international INTEGER,
    siteSpecifique VARCHAR(20),
    foreign key (idUnite) references Unite(idOffre),
    foreign key (idOffre) references Offre(idOffre)
);
---Mongo
CREATE TABLE Credit(
    minimum double PRECISION,
    mop integer,
    aop integer,
    international integer
);/*
*/
CREATE TABLE MouvementMC (
    idMouvementMC serial primary key,
    idUtilisateur INTEGER,
    montant DOUBLE PRECISION,
    typeTransaction VARCHAR(7),  -- soit depot ou retrait
    typeMouvementMC VARCHAR(7),  -- soit mvola ou credit
    dateMouvementMC Timestamp,
    dateAcceptation Timestamp, -- Rehefa depot credit dia tsy null intsony fa tonga dia meme @ ilay dateMouvementMC
    foreign key (idUtilisateur) references Utilisateur(idUtilisateur)
);

insert into MouvementMC (idUtilisateur, montant, typeTransaction, typeMouvementMC, dateMouvementMC) values (17, 20000, 'depot', 'credit' , '2021-02-20 10:00:00');
insert into MouvementMC (idUtilisateur, montant, typeTransaction, typeMouvementMC, dateMouvementMC) values (18, 30000, 'depot', 'credit' , '2021-02-20 10:00:00');


CREATE TABLE MouvementOffre (
    idMouvementOffre serial primary key,
    idUtilisateur INTEGER,
    idOffre INTEGER,
    dateMouvementOffre Timestamp,
    foreign key (idUtilisateur) references Utilisateur(idUtilisateur)
);

CREATE TABLE MouvementMA (
    idMouvementMA serial primary key,
    idUtilisateur INTEGER,
    dateMouvementMA Timestamp,
    duree double PRECISION,  -- duree appel en seconde ou 50mo tao am site 
    destinataire VARCHAR(100), -- Soit site , soit numero
    typeMouvementMA VARCHAR(10), -- Appel ou Mega
    idOffre int,
    foreign key (idUtilisateur) references Utilisateur(idUtilisateur)                                                           ---( mega , appel )
);

insert into MouvementMA (idUtilisateur, dateMouvementMA, duree,destinataire,typeMouvementMA,idOffre) 
    values (17,'2021-03-18 16:11:52','4','0348766709','appel',1);
-- raha sady niala ny facebobaka sady niala ny yelow dia 2 ny MouvementMA insert 

create table operateur(
    idOperateur serial primary key,
    nomOperateur varchar(50),
    initial varchar(10)
);

insert into operateur (nomOperateur, intial) values ('Telma','034');




-- ---------------VIEW ------------------------------
CREATE VIEW V_MouvementMega as
select * from MouvementMA where typeMouvementMA='mega' ;

CREATE VIEW V_MouvementAppel as 
select * from MouvementMA where typeMouvementMA='appel' ;
-- -------------------------------------------------------
CREATE VIEW V_MouvementMVola as 
select * from MouvementMC where typeMouvementMC='Mvola' ;

CREATE VIEW V_MouvementCredit as 
select * from MouvementMC where typeMouvementMC='Credit' ;

-- -------------------------------------------------------
drop view v_soldeCredit;
drop view v_creditDepot;
drop view v_creditRetrait;



create or replace view v_creditDepot as 
    select idUtilisateur , sum(montant) as creditDepot from MouvementMC where typeMouvementMC = 'credit' and typeTransaction = 'depot' group by idUtilisateur;

create or replace view v_creditRetrait as 
    select  idUtilisateur , sum(montant) as creditRetrait from MouvementMC where typeMouvementMC = 'credit' and typeTransaction = 'retrait' group by idUtilisateur;

create or replace view v_soldeCredit as
select cd.idUtilisateur, (coalesce(sum(cd.creditDepot))- coalesce(sum(cr.creditRetrait), 0)) as credit
from v_creditDepot cd 
left join v_creditRetrait cr on cd.idUtilisateur = cr.idUtilisateur 
group by cd.idUtilisateur;


-- -----------------------------------------------------------

create or replace view v_MvolaDepot as 
    select idUtilisateur , sum(montant) as MvolaDepot from MouvementMC where typeMouvementMC = 'mvola' and typeTransaction = 'depot' and dateAcceptation is not null group by idUtilisateur;

create or replace view v_MvolaRetrait as 
    select  idUtilisateur , sum(montant) as MvolaRetrait from MouvementMC where typeMouvementMC = 'mvola' and typeTransaction = 'retrait' group by idUtilisateur;

create or replace view v_soldeMvola as
select md.idUtilisateur, (coalesce(sum(md.MvolaDepot))- coalesce(sum(mr.MvolaRetrait), 0)) as mvola
from v_MvolaDepot md 
left join v_MvolaRetrait mr on md.idUtilisateur = mr.idUtilisateur 
group by md.idUtilisateur;
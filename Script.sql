drop database telma;
create database telma;
alter database telma owner to postgres;

\c telma

-- Creation des tables
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

create table categorie(
    idCategorie serial primary key,
    duration varchar(50), -- hebd , mens , annuel
    duree int
);

CREATE TABLE Offre (
    idOffre serial primary key,
    nom VARCHAR(60),
    prix DOUBLE PRECISION,
    idCategorie integer,
    foreign key (idCategorie) references Categorie(idCategorie)
);

CREATE TABLE DetailOffre (
    idDetailOffre serial primary key,
    idOffre INTEGER,
    typeOffre VARCHAR,
    valeur DOUBLE PRECISION,
    mop INTEGER,
    aop INTEGER,
    international INTEGER,
    siteSpecifique VARCHAR(20),
    foreign key (idOffre) references Offre(idOffre)
);

CREATE TABLE Credit(
    minimum double PRECISION,
    prixSms double PRECISION,   -- par 1sms
    prixMega double PRECISION, -- par 1mo
    mop integer,   -- 4
    aop integer,    -- 8
    international integer
);

insert into credit values (200,5,10,4,8,20);

-- -----------------------------------------------

-- Donnees de test Offre:

-- Categorie : 

insert into Categorie(duration, duree) values ('journalier',1);
insert into Categorie(duration, duree) values ('mensuel',30);
insert into Categorie(duration, duree) values ('hebdomadaire',7);

--  Offre Mora:
insert into Offre(nom, prix, idCategorie) values ('Mora', 1000, 1);
insert into DetailOffre(idOffre,typeOffre,valeur,siteSpecifique) values (1,'internet',50,'internet');
insert into DetailOffre(idOffre,typeOffre,valeur,mop,aop,international) values (1,'appel',1000,1,3,10);
insert into DetailOffre(idOffre,typeOffre,valeur) values (1,'sms',50);

-- Offre facebobaka
insert into Offre(nom, prix, idCategorie) values ('Facebobaka', 500, 3);
insert into DetailOffre(idOffre,typeOffre,valeur,siteSpecifique) values (2,'internet',1000,'facebook');

create or replace view v_offreDetailed as
select o.* , c.duration,  c.duree , d.typeOffre, d.valeur, d.mop , d.aop, d.international from offre o join categorie c on o.idCategorie = c.idCategorie join detailOffre d on d.idOffre = o.idOffre;

-- Vue date expiration offre :
create or replace view v_mouvementOffre as 
select mo.idUtilisateur, mo.idMouvementOffre,mo.idOffre , mo.dateMouvementOffre as dateDebut, v_offreDetailed.duree,
case 
    when duree = 1 then mo.dateMouvementOffre::date 
    when duree = 7 then mo.dateMouvementOffre::date + integer '7'
    when duree = 30 then mo.dateMouvementOffre::date + integer '30'
end as dateExpiration,
'23:59'::time as TimeExpiration
from MouvementOffre mo join v_offreDetailed on v_offreDetailed.idOffre = mo.idOffre;

-- Consomation

create table consomation(
    idMouvementOffre int,
    valeur double PRECISION,
    typeOffre varchar,
    foreign key(idMouvementOffre) references MouvementOffre(idMouvementOffre)
);

-- Les mouvements :

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
    dateMouvementOffre Timestamp, -- 
    foreign key (idUtilisateur) references Utilisateur(idUtilisateur),
    foreign key (idOffre) references Offre(idOffre)
    
);

insert into MouvementOffre(idUtilisateur, idOffre,dateMouvementOffre) values (17,1,'24-03-2021 08:00:00');
insert into MouvementOffre(idUtilisateur, idOffre,dateMouvementOffre) values (17,2,'24-03-2021 10:00:00');



CREATE TABLE MouvementMA (
    idMouvementMA serial primary key,
    idUtilisateur INTEGER,
    dateMouvementMA Timestamp,
    duree double PRECISION,  -- duree appel en seconde ou 50mo tao am site 
    destinataire VARCHAR(100), -- Soit site , soit numero
    typeMouvementMA VARCHAR(10), -- Appel ou Mega
    idOffre int,
    foreign key (idUtilisateur) references Utilisateur(idUtilisateur)                                                          ---( mega , appel )
);


insert into MouvementMA (idUtilisateur, dateMouvementMA, duree,destinataire,typeMouvementMA,idOffre) 
    values (1,'2021-03-18 16:11:52','4','0348766709','appel',1);
-- raha sady niala ny facebobaka sady niala ny yelow dia 2 ny MouvementMA insert 

create table operateur(
    idOperateur serial primary key,
    nomOperateur varchar(50),
    initial varchar(10)
);

insert into operateur (nomOperateur, initial) values ('Telma','034');

-- --------------------------------------------------
create or replace view v_user_token as
select u.* , t.tokenValue from utilisateur u join token t on u.idUtilisateur = t.idToken;


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
select u.idUtilisateur, (coalesce(sum(md.MvolaDepot),0)- coalesce(sum(mr.MvolaRetrait), 0)) as mvola
from v_MvolaDepot md 
left join v_MvolaRetrait mr on md.idUtilisateur = mr.idUtilisateur right join utilisateur u on md.idUtilisateur = u.idUtilisateur
group by u.idUtilisateur;

-------------------------------------------------------------------

-- Kiady

CREATE or REPLACE VIEW V_MouvementMVola as 
select   MouvementMC.* ,utilisateur.nom
from MouvementMC join utilisateur on utilisateur.idutilisateur=MouvementMC.idutilisateur 
where typeMouvementMC='mvola' ;

create or replace view demandedepot as
select V_MouvementMVola.*
from V_MouvementMVola
where dateAcceptation is null order by datemouvementmc desc;

CREATE Sequence seq_idoffre ;
****************************************************
Répertoire pour les fichiers deltas de Flyway en DEV

Syntaxe pour le nommage:

V{date}__{type_modif}_{numero_feature}.sql

date :
    date et heure de la creation du fichier par exemple 2017.07.03_10.19
type_modif :
    STR pour struture => ajout, modification, suppression de table
    REF pour reference => ajout, modification, suppression de ligne dans une table
numero_feature :
    numero de feature par exemple MP-1

exemple :
    V2017.07.03_10.19__STR_MP-1.sql

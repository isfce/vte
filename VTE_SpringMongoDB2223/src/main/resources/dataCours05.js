db.cours.deleteMany({});
db.cours.insertMany([{ 
    "_id" : "IPAI", 
    "intitule" : "Principe d'analyse informatique", 
    "nbPeriodes" : 60.0, 
    "etcs" : 4.0, 
    "determinant" : false
},
// ----------------------------------------------
{ 
    "_id" : "ITGP", 
    "intitule" : "Technique de gestion de projet", 
    "nbPeriodes" : 40.0, 
    "etcs" : 3.0, 
    "determinant" : false
},
// ----------------------------------------------
{ 
    "_id" : "IBRE", 
    "intitule" : "Base des reseaux", 
    "nbPeriodes" : 80.0, 
    "etcs" : 6.0, 
    "determinant" : false
},
// ----------------------------------------------
{ 
    "_id" : "IASR", 
    "intitule" : "Administration, gestion et securisation des reseaux", 
    "nbPeriodes" : 100.0, 
    "etcs" : 8.0, 
    "determinant" : true
},
// ----------------------------------------------
{ 
    "_id" : "IPAC", 
    "intitule" : "Projet d'analyse et de conception", 
    "nbPeriodes" : 100.0, 
    "etcs" : 10.0, 
    "determinant" : true
},
// ----------------------------------------------
{ 
    "_id" : "ISIP", 
    "intitule" : "Bachelier : Stage d'integration professionnelle", 
    "nbPeriodes" : 120.0, 
    "etcs" : 5.0, 
    "determinant" : false
},
// ----------------------------------------------
{ 
    "_id" : "IIBD", 
    "intitule" : "Initiation aux bases de donnees", 
    "nbPeriodes" : 60.0, 
    "etcs" : 5.0, 
    "determinant" : false
},
// ----------------------------------------------
{ 
    "_id" : "IGEB", 
    "intitule" : "Gestio et exploitation de bases de donnees", 
    "nbPeriodes" : 60.0, 
    "etcs" : 5.0, 
    "determinant" : false
},
// ----------------------------------------------
{ 
    "_id" : "IVTE", 
    "intitule" : "Veille technologique", 
    "nbPeriodes" : 40.0, 
    "etcs" : 4.0, 
    "determinant" : true
},
// ----------------------------------------------
{ 
    "_id" : "ISO2", 
    "intitule" : "Structure des ordinateurs", 
    "nbPeriodes" : 60.0, 
    "etcs" : 5.0, 
    "determinant" : false
},
// ----------------------------------------------
{ 
    "_id" : "IPAP", 
    "intitule" : "Principe algorithmique et programmation", 
    "nbPeriodes" : 120.0, 
    "etcs" : 8.0, 
    "determinant" : false
},
// ----------------------------------------------
{ 
    "_id" : "IPO2", 
    "intitule" : "Programmation oriente objet", 
    "nbPeriodes" : 120.0, 
    "etcs" : 9.0, 
    "determinant" : false
},
// ----------------------------------------------
{ 
    "_id" : "IPDB", 
    "intitule" : "Projet de developpement SGBD", 
    "nbPeriodes" : 80.0, 
    "etcs" : 8.0, 
    "determinant" : true
},
// ----------------------------------------------
{ 
    "_id" : "IPID", 
    "intitule" : "Projet d'integration  de developpement", 
    "nbPeriodes" : 60.0, 
    "etcs" : 9.0, 
    "determinant" : true
},
// ----------------------------------------------
{ 
    "_id" : "IAPF", 
    "intitule" : "Bachelier : Activites professionnelles de formation", 
    "nbPeriodes" : 240.0, 
    "etcs" : 12.0, 
    "determinant" : true
},
// ----------------------------------------------
{ 
    "_id" : "ISE2", 
    "intitule" : "Systeme d'exploitation", 
    "nbPeriodes" : 100.0, 
    "etcs" : 8.0, 
    "determinant" : false
},
// ----------------------------------------------
{ 
    "_id" : "IWPB", 
    "intitule" : "Web : principe de base", 
    "nbPeriodes" : 40.0, 
    "etcs" : 3.0, 
    "determinant" : false
},
// ----------------------------------------------
{ 
    "_id" : "IPDW", 
    "intitule" : "Projet de developpement Web", 
    "nbPeriodes" : 100.0, 
    "etcs" : 10.0, 
    "determinant" : true
}
]);
db.prof.deleteMany({});
db.prof.insertMany([
{ 
    "_id" : "VO", 
    "nom" : "Van Oudenhove", 
    "prenom" : "Didier", 
    "contact" : {
        "email" : "voisfce@gmail.com", 
        "tel" : "02/647.25.69"
    }, 
    "cours" : [
    {"$ref":"cours","$id":"IIBD"},
    {"$ref":"cours","$id":"IGEB"},
    {"$ref":"cours","$id":"IPID"},
    {"$ref":"cours","$id":"IVTE"},
    {"$ref":"cours","$id":"IPAP"}
    ], 
    "actif" : true, 
    "dateFonction" : new Date(2010,10,1)
},
// ----------------------------------------------
{ 
    "_id" : "DH", 
    "nom" : "De Henau", 
    "prenom" : "Marie Ange", 
    "contact" : {
        "email" : null, 
        "tel" : "02/647.25.69"
    }, 
    
    "cours" : [
    {"$ref":"cours","$id":"IPAP"},
    {"$ref":"cours","$id":"IWPB"},
    {"$ref":"cours","$id":"IPAP"}
    ], 
    "actif" : false, 
    "dateFonction" : new Date(2000,9,1)
},
// ----------------------------------------------
{ 
    "_id" : "WA", 
    "nom" : "Wafflard", 
    "prenom" : "Alain", 
    "contact" : {
        "email" : "waf@isfce.be", 
        "tel" : "02/647.25.69"
    }, 
    
    "cours" : [
		{"$ref":"cours","$id":"IPDW"},
        {"$ref":"cours","$id":"IWPB"}
    ], 
    "actif" : true, 
    "dateFonction" : new Date(2015,9,1)
},
// ----------------------------------------------
{ 
    "_id" : "LM", 
    "nom" : "Lemaigre", 
    "prenom" : "Christophe", 
    "contact" : {
        "email": "cl@isfce.org",
        "tel" : "02/647.25.70"
    }, 
    "cours" : [
    {"$ref":"cours","$id":"ISE2"},
    {"$ref":"cours","$id":"IPDW"}
    ], 
    "actif" : false, 
    "dateFonction" : new Date(2013,9,1)
},
// ----------------------------------------------
{ 
    "_id" : "HE", 
    "nom" : "Hermel", 
    "prenom" : "Nicolas", 
    "contact" : {
        "email" : "nicolas@gmail.org"
    }, 
    "cours" : [
       {"$ref":"cours","$id":"IASR"}
    ], 
    "actif" : false, 
    "dateFonction" : new Date(2015,4,1)
},
// ----------------------------------------------
{ 
    "_id" : "HS", 
    "nom" : "Huguier", 
    "prenom" : "Stephane", 
    "contact" : {
        "email" : "hs@isfce.org"
    }, 
    "cours" : [
       {"$ref":"cours","$id":"IASR"},
       {"$ref":"cours","$id":"IBRE"},
       {"$ref":"cours","$id":"ISO2"}
    ], 
    "actif" : true, 
    "dateFonction" : new Date(2021,9,1)
},
// ----------------------------------------------
{ 
    "_id" : "DC", 
    "nom" : "Decamp", 
    "prenom" : "Gérald", 
    "contact" : {
        "email": "dc@isfce.org",
        "tel" : "02/647.25.70"
    }, 
    "cours" : [
    {"$ref":"cours","$id":"ISE2"},
    {"$ref":"cours","$id":"IWPB"},
    {"$ref":"cours","$id":"IBRE"}
    ], 
    "actif" : false, 
    "dateFonction" : new Date(2005,9,1)
}])
CREATE TABLE artikel (_id INTEGER PRIMARY KEY, bezeichnung TEXT);
CREATE TABLE einkauf (_id INTEGER PRIMARY KEY, datum TEXT, datum_sort TEXT, shop INTEGER, bemerkung TEXT);
CREATE TABLE listen (_id INTEGER PRIMARY KEY, artikel TEXT, anzahl INTEGER, einkaufnr INTEGER, erledigt INTEGER, sortindex INTEGER, hide INTEGER);
CREATE TABLE shops (_id INTEGER PRIMARY KEY, shopname TEXT, ort TEXT);
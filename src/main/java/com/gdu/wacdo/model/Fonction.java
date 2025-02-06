package com.gdu.wacdo.model;

import jakarta.persistence.Entity;

@Entity
public class Fonction extends AbstractPersistentEntity<Integer>{

    private String libelle;

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}

package com.gdu.wacdo.model;

import jakarta.persistence.Entity;

@Entity
public class Fonction extends AbstractPersistentEntity<Integer> {

    private String libelle;

    public Fonction(int id) {
        this.setId(id);
    }

    public Fonction() {

    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}

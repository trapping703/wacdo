package com.gdu.wacdo.model;

import jakarta.persistence.Entity;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Restaurant extends AbstractPersistentEntity<Integer>{

    private String nom;
    private String adresse;
    private int codePostal;
    private String ville;

    private List<Affectation> affectations;

    public Restaurant() {

    }

    public Restaurant(Integer id, String nom, String adresse, int codePostal, String ville) {
        setId(id);
        this.nom = nom;
        this.adresse = adresse;
        this.codePostal = codePostal;
        this.ville = ville;
    }
}

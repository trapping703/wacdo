package com.gdu.wacdo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Employe extends AbstractPersistentEntity<Integer> {

    private String nom;
    private String prenom;
    private String email;
    private LocalDate dateEmbauche;
    private boolean admin;
    private String motDePasse;

    @OneToMany(mappedBy = "employe")
    private List<Affectation> affectations;

    public Employe(int id, String nom, String prenom, LocalDate dateEmbauche, boolean admin, String motDePasse) {
        this.setId(id);
        this.nom = nom;
        this.prenom = prenom;
        this.dateEmbauche = dateEmbauche;
        this.admin = admin;
        this.motDePasse = motDePasse;
    }

    public Employe() {

    }
}

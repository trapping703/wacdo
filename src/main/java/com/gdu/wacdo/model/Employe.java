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

    @OneToMany
    private List<Affectation> affectations;

}

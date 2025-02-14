package com.gdu.wacdo.dto.model;

import com.gdu.wacdo.model.AbstractPersistentEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EmployeDTO {

    private int id;
    private String nom;
    private String prenom;
    private String email;
    private LocalDate dateEmbauche;
    private boolean admin;
    private String motDePasse;

    private List<AffectationDTO> affectations;

    public EmployeDTO(int id, String nom, String prenom, LocalDate dateEmbauche, boolean admin, String motDePasse) {
        this.setId(id);
        this.nom = nom;
        this.prenom = prenom;
        this.dateEmbauche = dateEmbauche;
        this.admin = admin;
        this.motDePasse = motDePasse;
    }

    public EmployeDTO() {

    }
}

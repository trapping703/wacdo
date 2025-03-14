package com.gdu.wacdo.dto.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
public class EmployeDTO {

    private int id;
    private String nom;
    private String prenom;
    private String email;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
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

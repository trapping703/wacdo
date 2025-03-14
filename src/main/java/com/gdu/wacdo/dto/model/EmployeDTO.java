package com.gdu.wacdo.dto.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
public class EmployeDTO {

    private Integer id;
    @NotEmpty
    private String nom;
    @NotEmpty
    private String prenom;
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Mail incompatible")
    private String email;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateEmbauche;
    private boolean admin;
    private String motDePasse;

    private List<AffectationDTO> affectations;

    public EmployeDTO(Integer id, String nom, String prenom, LocalDate dateEmbauche, boolean admin, String motDePasse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateEmbauche = dateEmbauche;
        this.admin = admin;
        this.motDePasse = motDePasse;
    }

    public EmployeDTO() {

    }
}

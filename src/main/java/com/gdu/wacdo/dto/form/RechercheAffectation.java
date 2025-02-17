package com.gdu.wacdo.dto.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class RechercheAffectation {

    private String ville;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDebut;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFin;
    private int fonction_id;

    public RechercheAffectation(String ville, LocalDate dateDebut, LocalDate dateFin, int fonction_id) {
        this.ville = ville;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.fonction_id = fonction_id;
    }

    public RechercheAffectation() {
    }
}

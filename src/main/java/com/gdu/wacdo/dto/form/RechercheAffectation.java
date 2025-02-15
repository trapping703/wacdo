package com.gdu.wacdo.dto.form;

import com.gdu.wacdo.dto.model.FonctionDTO;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class RechercheAffectation {

    private String ville;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate dateDebut;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate dateFin;
    private int fonction_id;
}

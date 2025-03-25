package com.gdu.wacdo.dto.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class RechercheAffectationDetailsEmployeDTO {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDebut;
    private int fonction_id;

    public RechercheAffectationDetailsEmployeDTO() {
    }
}

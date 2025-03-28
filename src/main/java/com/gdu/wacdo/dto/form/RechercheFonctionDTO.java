package com.gdu.wacdo.dto.form;

import lombok.Data;

@Data
public class RechercheFonctionDTO {

    private String libelle;

    public RechercheFonctionDTO(String libelle) {
        this.libelle = libelle;
    }

    public RechercheFonctionDTO(){

    }
}

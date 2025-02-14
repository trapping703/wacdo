package com.gdu.wacdo.dto.model;

import lombok.Data;

import java.util.List;

@Data
public class RestaurantDTO {

    private int id;
    private String nom;
    private String adresse;
    private int codePostal;
    private String ville;

    private List<AffectationDTO> affectations;

    public RestaurantDTO() {

    }

    public RestaurantDTO(Integer id, String nom, String adresse, int codePostal, String ville) {
        setId(id);
        this.nom = nom;
        this.adresse = adresse;
        this.codePostal = codePostal;
        this.ville = ville;
    }
}

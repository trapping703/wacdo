package com.gdu.wacdo.dto.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class RestaurantDTO {

    private Integer id;
    @NotEmpty
    private String nom;
    @NotEmpty
    private String adresse;
    @Size(min = 5, max = 5, message = "code postal incompatible")
    @Pattern(regexp = "^(\\s*|\\d+)$", message = "code postal incompatible")
    private String codePostal;
    @NotEmpty
    private String ville;

    private List<AffectationDTO> affectations;

    public RestaurantDTO() {

    }

    public RestaurantDTO(Integer id, String nom, String adresse, String codePostal, String ville) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.codePostal = codePostal;
        this.ville = ville;
    }
}

package com.gdu.wacdo.dto.form;

import lombok.Data;

@Data
public class RechercheRestaurant {

    private String nom,ville;
    private int CodePostal;
}

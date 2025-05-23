package com.gdu.wacdo.dto.form;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RechercheRestaurantDTO {

    private String nom, ville;
    @Size(min = 0, max = 5, message = "code postal incompatible")
    @Pattern(regexp = "^(\\s*|\\d+)$", message = "code postal incompatible")
    private String codePostal;
}

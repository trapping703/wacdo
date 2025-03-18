package com.gdu.wacdo.dto.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class FonctionDTO {

    private Integer id;
    @NotEmpty(message = "libelle incompatible")
    private String libelle;

}

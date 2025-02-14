package com.gdu.wacdo.dto.model;

import com.gdu.wacdo.model.AbstractPersistentEntity;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class FonctionDTO {

    private int id;
    private String libelle;

}

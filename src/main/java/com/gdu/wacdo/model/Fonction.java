package com.gdu.wacdo.model;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Fonction extends AbstractPersistentEntity<Integer> {

    private String libelle;

    public Fonction(int id) {
        this.setId(id);
    }

    public Fonction() {

    }
}

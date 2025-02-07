package com.gdu.wacdo.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;


@MappedSuperclass
public class AbstractPersistentEntity <T> implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}


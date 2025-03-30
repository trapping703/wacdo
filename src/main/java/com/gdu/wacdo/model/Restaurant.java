package com.gdu.wacdo.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class Restaurant extends AbstractPersistentEntity<Integer> {

    private String nom;
    private String adresse;
    @Size(min = 5, max = 5, message = "code postal incompatible")
    @Pattern(regexp = "^(\\s*|\\d+)$", message = "code postal incompatible")
    private String codePostal;
    private String ville;

    @OneToMany(mappedBy = "restaurant")
    @JsonIncludeProperties(value = {"id"})
    private List<Affectation> affectations;

    public Restaurant() {

    }

    public Restaurant(Integer id, String nom, String adresse, String codePostal, String ville) {
        setId(id);
        this.nom = nom;
        this.adresse = adresse;
        this.codePostal = codePostal;
        this.ville = ville;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public List<Affectation> getAffectations() {
        return affectations;
    }

    public void setAffectations(List<Affectation> affectations) {
        this.affectations = affectations;
    }
}

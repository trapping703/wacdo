package com.gdu.wacdo.repository;

import com.gdu.wacdo.model.Affectation;
import com.gdu.wacdo.model.Employe;
import com.gdu.wacdo.model.Fonction;
import com.gdu.wacdo.model.Restaurant;
import com.gdu.wacdo.repositories.AffectationRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestEntityManager
public class AffectationRepositoryIntegrationTest {

    @Autowired
    private AffectationRepository affectationRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Transactional
    @Rollback
    public void givenNewAffectation_whenSave_thenCorrect() {
        //given
        Affectation affectation = new Affectation();
        affectation.setDateDebut(LocalDate.now());
        //then
        affectationRepository.save(affectation);
        //when
        assertThat(affectation.getId()).isNotNull();
        assertThat(entityManager.find(Affectation.class, affectation.getId())).isEqualTo(affectation);
    }

    @Test
    @Transactional
    @Rollback
    public void givenPlusieursAffectations_whenFindAffectationsPourRechercheAvecAllCritere_thenAffectationsCorrectesTrouvee() {
        //given
        Restaurant restaurantBerlin = creerRestaurant("Berlin");
        Restaurant restaurantMoscow = creerRestaurant("Moscow");
        Fonction fonction1 = creerFonction();
        Fonction fonction2 = creerFonction();
        Employe employe = creerEmploye();
        Affectation affectation1 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), fonction1, restaurantBerlin, employe);
        Affectation affectation2 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), fonction1, restaurantBerlin, employe);
        Affectation affectation3 = creerAffectation(LocalDate.of(2019, 1, 1), LocalDate.of(2021, 1, 1), fonction1, restaurantBerlin, employe);
        Affectation affectation4 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2022, 1, 1), fonction1, restaurantBerlin, employe);
        Affectation affectation5 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), fonction2, restaurantBerlin, employe);
        Affectation affectation6 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), fonction1, restaurantMoscow, employe);
        //then
        List<Affectation> affectations = affectationRepository.findAffectationsPourRecherche(restaurantBerlin.getVille(), LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), fonction1.getId());
        //when
        assertThat(affectations).containsOnly(affectation1, affectation2);
    }

    @Test
    @Transactional
    @Rollback
    public void givenPlusieursAffectations_whenFindAffectationsPourRechercheAvecCritereDateDebut_thenAffectationsCorrectesTrouvee() {
        //given
        Restaurant restaurantBerlin = creerRestaurant("Berlin");
        Restaurant restaurantMoscow = creerRestaurant("Moscow");
        Fonction fonction1 = creerFonction();
        Fonction fonction2 = creerFonction();
        Employe employe = creerEmploye();
        Affectation affectation1 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), fonction1, restaurantBerlin, employe);
        Affectation affectation2 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), fonction1, restaurantBerlin, employe);
        Affectation affectation3 = creerAffectation(LocalDate.of(2019, 1, 1), LocalDate.of(2021, 1, 1), fonction1, restaurantBerlin, employe);
        Affectation affectation4 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2022, 1, 1), fonction1, restaurantBerlin, employe);
        Affectation affectation5 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), fonction2, restaurantBerlin, employe);
        Affectation affectation6 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), fonction1, restaurantMoscow, employe);
        //then
        List<Affectation> affectations = affectationRepository.findAffectationsPourRecherche("", LocalDate.of(2020, 1, 1), null, 0);
        //when
        assertThat(affectations).containsOnly(affectation1, affectation2, affectation6, affectation4, affectation5);
    }

    @Test
    @Transactional
    @Rollback
    public void givenPlusieursAffectations_whenFindAffectationsPourRechercheAvecCritereDateFin_thenAffectationsCorrectesTrouvee() {
        //given
        Restaurant restaurantBerlin = creerRestaurant("Berlin");
        Restaurant restaurantMoscow = creerRestaurant("Moscow");
        Fonction fonction1 = creerFonction();
        Fonction fonction2 = creerFonction();
        Employe employe = creerEmploye();
        Affectation affectation1 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), fonction1, restaurantBerlin, employe);
        Affectation affectation2 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), fonction1, restaurantBerlin, employe);
        Affectation affectation3 = creerAffectation(LocalDate.of(2019, 1, 1), LocalDate.of(2021, 1, 1), fonction1, restaurantBerlin, employe);
        Affectation affectation4 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2022, 1, 1), fonction1, restaurantBerlin, employe);
        Affectation affectation5 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), fonction2, restaurantBerlin, employe);
        Affectation affectation6 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), fonction1, restaurantMoscow, employe);
        //then
        List<Affectation> affectations = affectationRepository.findAffectationsPourRecherche("", null, LocalDate.of(2022, 1, 1), 0);
        //when
        assertThat(affectations).containsOnly(affectation4);
    }

    @Test
    @Transactional
    @Rollback
    public void givenPlusieursAffectations_whenFindAffectationsPourRechercheAvecCritereVille_thenAffectationsCorrectesTrouvee() {
        //given
        Restaurant restaurantBerlin = creerRestaurant("Berlin");
        Restaurant restaurantMoscow = creerRestaurant("Moscow");
        Fonction fonction1 = creerFonction();
        Fonction fonction2 = creerFonction();
        Employe employe = creerEmploye();
        Affectation affectation1 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), fonction1, restaurantBerlin, employe);
        Affectation affectation2 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), fonction1, restaurantBerlin, employe);
        Affectation affectation3 = creerAffectation(LocalDate.of(2019, 1, 1), LocalDate.of(2021, 1, 1), fonction1, restaurantBerlin, employe);
        Affectation affectation4 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2022, 1, 1), fonction1, restaurantBerlin, employe);
        Affectation affectation5 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), fonction2, restaurantBerlin, employe);
        Affectation affectation6 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), fonction1, restaurantMoscow, employe);
        //then
        List<Affectation> affectations = affectationRepository.findAffectationsPourRecherche("Berlin", null, null, 0);
        //when
        assertThat(affectations).containsOnly(affectation1, affectation2, affectation3, affectation4, affectation5);
    }


    @Test
    @Transactional
    @Rollback
    public void givenPlusieursAffectations_whenFindAffectationsPourRechercheAvecCritereFonction_thenAffectationsCorrectesTrouvee() {
        //given
        Restaurant restaurantBerlin = creerRestaurant("Berlin");
        Restaurant restaurantMoscow = creerRestaurant("Moscow");
        Fonction fonction1 = creerFonction();
        Fonction fonction2 = creerFonction();
        Employe employe = creerEmploye();
        Affectation affectation1 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), fonction1, restaurantBerlin, employe);
        Affectation affectation2 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), fonction1, restaurantBerlin, employe);
        Affectation affectation3 = creerAffectation(LocalDate.of(2019, 1, 1), LocalDate.of(2021, 1, 1), fonction1, restaurantBerlin, employe);
        Affectation affectation4 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2022, 1, 1), fonction1, restaurantBerlin, employe);
        Affectation affectation5 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), fonction2, restaurantBerlin, employe);
        Affectation affectation6 = creerAffectation(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), fonction1, restaurantMoscow, employe);
        //then
        List<Affectation> affectations = affectationRepository.findAffectationsPourRecherche("", null, null, fonction2.getId());
        //when
        assertThat(affectations).containsOnly(affectation5);
    }

    private Affectation creerAffectation(LocalDate dateDebut, LocalDate dateFin, Fonction fonction, Restaurant restaurant, Employe employe) {
        Affectation affectation = new Affectation();
        affectation.setDateDebut(dateDebut);
        affectation.setDateFin(dateFin);
        affectation.setFonction(fonction);
        affectation.setRestaurant(restaurant);
        affectation.setEmploye(employe);
        return entityManager.persist(affectation);
    }

    private Employe creerEmploye() {
        Employe employe = new Employe();
        employe.setNom("Employe");
        return entityManager.persist(employe);
    }

    private Fonction creerFonction() {
        Fonction fonction = new Fonction();
        fonction.setLibelle("test");
        return entityManager.persist(fonction);
    }

    private Restaurant creerRestaurant(String ville) {
        Restaurant restaurant = new Restaurant();
        restaurant.setVille(ville);
        restaurant.setNom("Macdal");
        return entityManager.persist(restaurant);
    }
}
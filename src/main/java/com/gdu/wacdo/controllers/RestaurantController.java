package com.gdu.wacdo.controllers;

import com.gdu.wacdo.model.Affectation;
import com.gdu.wacdo.model.Employe;
import com.gdu.wacdo.model.Fonction;
import com.gdu.wacdo.model.Restaurant;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Controller
public class RestaurantController {

    private final List<Restaurant> restaurants;

    public RestaurantController() {
        restaurants = Stream.iterate(0, n -> n + 1)
                .limit(10)
                .map(i -> new Restaurant(i,"nom" + i.toString(), "adresse" + i.toString(),10 + i,  "ville" + i.toString()))
                .toList();
    }


    @GetMapping("/restaurants")
    public String restaurants(Model model) {


        model.addAttribute("restaurants", restaurants);

        return "restaurants";
    }


    @GetMapping("/restaurant/{id}")
    public String restaurant(Model model, @PathVariable int id) {
        Restaurant resto = restaurants.stream().filter(r -> r.getId() == id).findAny().get();

        rajouterAffectations(resto);

        model.addAttribute("restaurant", resto);
        return "restaurant";
    }

    private void rajouterAffectations(Restaurant resto) {
        Fonction manager = new Fonction();
        manager.setLibelle("Manager");
        Employe managerEmp = new Employe(1, "managerNom", "managerPrenom", LocalDate.now(), true, "bleh");


        Fonction serveur = new Fonction();
        serveur.setLibelle("serveur");
        Employe serveurEmp = new Employe(1, "serveurNom", "serveurPrenom", LocalDate.now(), false, "");

        Fonction cuisinier = new Fonction();
        cuisinier.setLibelle("cuisinier");
        Employe cuisinierEmp = new Employe(1, "cuisinierNom", "cuisinierPrenom", LocalDate.now(), false, "");

        Affectation affectationManager = new Affectation(1, LocalDate.now().minusDays(1), LocalDate.now(), managerEmp, resto, manager);
        Affectation affectationServeur = new Affectation(1, LocalDate.now().minusDays(1), null, serveurEmp, resto, serveur);
        Affectation affectationCuisinier = new Affectation(1, LocalDate.now().minusDays(1), null, cuisinierEmp, resto, manager);

        resto.setAffectations(List.of(affectationManager, affectationCuisinier, affectationServeur));
    }
}

package com.gdu.wacdo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/restaurants")
    public String restaurants() {
        return "restaurants";
    }


    @GetMapping("/employes")
    public String employes() {
        return "employes";
    }

    @GetMapping("/affectations")
    public String affectations() {
        return "affectations";
    }

    @GetMapping("/fonctions")
    public String fonctions() {
        return "fonctions";
    }



}

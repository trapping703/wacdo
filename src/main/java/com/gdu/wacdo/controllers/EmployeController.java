package com.gdu.wacdo.controllers;

import com.gdu.wacdo.model.Employe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class EmployeController {

    @GetMapping("/employes")
    public String employes(Model model) {
        model.addAllAttributes( List.of(new Employe(),"employes"));
        return "employes";
    }

    @GetMapping("/employe/{id}")
    public String employes(Model model, @PathVariable int id) {
        return "employe";
    }
}

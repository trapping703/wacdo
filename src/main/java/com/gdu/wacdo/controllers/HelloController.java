package com.gdu.wacdo.controllers;

import com.gdu.wacdo.model.Employe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

}

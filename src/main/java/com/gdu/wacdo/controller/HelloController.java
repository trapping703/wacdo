package com.gdu.wacdo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping({"","/","test","/test"})
    public String index() {
        return "index";
    }


}

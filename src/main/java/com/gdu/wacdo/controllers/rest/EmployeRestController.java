package com.gdu.wacdo.controllers.rest;

import com.gdu.wacdo.model.Employe;
import com.gdu.wacdo.services.EmployeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class EmployeRestController {

    @Autowired
    private EmployeService employeService;

    @GetMapping("/employes")
    public List<Employe> getAllEmployes() {
        return (List<Employe>) employeService.findAll().getData();
    }

    @GetMapping("/employe/{id}")
    public Employe getEmploye(@PathVariable int id) {
        return (Employe) employeService.findById(id).getData();
    }

    @PostMapping("/employe")
    public ResponseEntity<Employe> createEmploye(@RequestBody Employe employe) {
        return new ResponseEntity<>((Employe) employeService.save(employe).getData(), HttpStatus.CREATED);
    }

    @PatchMapping("/employe")
    public ResponseEntity<Employe> updateEmploye(@RequestBody Employe employe) {
        return new ResponseEntity<>((Employe) employeService.save(employe).getData(), HttpStatus.OK);
    }
}

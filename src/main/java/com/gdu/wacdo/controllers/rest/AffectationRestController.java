package com.gdu.wacdo.controllers.rest;

import com.gdu.wacdo.model.Affectation;
import com.gdu.wacdo.services.AffectationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class AffectationRestController {

    @Autowired
    private AffectationService affectationService;

    @GetMapping("/affectations")
    public List<Affectation> getAllAffectations() {
        return (List<Affectation>) affectationService.findAll().getData();
    }

    @GetMapping("/affectation/{id}")
    public Affectation getAffectation(@PathVariable int id) {
        return (Affectation) affectationService.findById(id).getData();
    }

    @PostMapping("/affectation")
    public ResponseEntity<Affectation> createAffectation(@RequestBody Affectation affectation) {
        return new ResponseEntity<>((Affectation) affectationService.save(affectation).getData(), HttpStatus.CREATED);
    }

    @PatchMapping("/affectation")
    public ResponseEntity<Affectation> updateAffectation(@RequestBody Affectation affectation) {
        return new ResponseEntity<>((Affectation) affectationService.save(affectation).getData(), HttpStatus.OK);
    }
}

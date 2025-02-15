package com.gdu.wacdo.controllers.rest;

import com.gdu.wacdo.model.Fonction;
import com.gdu.wacdo.services.FonctionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class FonctionRestController {

    @Autowired
    private FonctionService fonctionService;

    @GetMapping("/fonctions")
    public List<Fonction> getAllFonctions() {
        return (List<Fonction>) fonctionService.findAll().getData();
    }

    @GetMapping("/fonction/{id}")
    public Fonction getFonction(@PathVariable int id) {
        return (Fonction) fonctionService.findById(id).getData();
    }

    @PostMapping("/fonction")
    public ResponseEntity<Fonction> createFonction(@RequestBody Fonction fonction) {
        return new ResponseEntity<>((Fonction) fonctionService.save(fonction).getData(), HttpStatus.CREATED);
    }

    @PatchMapping("/fonction")
    public ResponseEntity<Fonction> updateFonction(@RequestBody Fonction fonction) {
        return new ResponseEntity<>((Fonction) fonctionService.save(fonction).getData(), HttpStatus.OK);
    }
}

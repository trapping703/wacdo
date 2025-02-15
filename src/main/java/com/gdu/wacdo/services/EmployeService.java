package com.gdu.wacdo.services;

import com.gdu.wacdo.model.Employe;
import com.gdu.wacdo.repositories.EmployeRepository;
import com.gdu.wacdo.dto.response.ReponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.gdu.wacdo.status.CodeReponse.*;
import static com.gdu.wacdo.dto.response.ReponseService.reponse;

@Service
@Slf4j
public class EmployeService {

    private final EmployeRepository employeRepository;

    public EmployeService(EmployeRepository employeRepository) {
        this.employeRepository = employeRepository;
    }

    public ReponseService save(Employe employe) {
        try {
            return reponse(OK, employeRepository.save(employe));
        } catch (Exception e) {
            return reponse(ERROR, employe, e);
        }
    }

    public ReponseService findAll() {
        return reponse(OK, employeRepository.findAll());
    }

    public ReponseService findById(int id) {
        try {
            Optional<Employe> employe = employeRepository.findById(id);
            return employe.map(value -> reponse(OK, value)).orElseGet(() -> reponse(EMPTY, id));
        } catch (Exception e) {
            return reponse(ERROR, id, e);
        }
    }

    public ReponseService delete(int id) {
        try {
            employeRepository.deleteById(id);
            return reponse(OK, id);
        } catch (Exception e) {
            return reponse(ERROR, id, e);
        }
    }
}

package com.gdu.wacdo.repository;

import com.gdu.wacdo.model.Employe;
import com.gdu.wacdo.repositories.EmployeRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestEntityManager
public class EmployeRepositoryIntegrationTest {

    @Autowired
    private EmployeRepository employeRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Transactional
    @Rollback
    public void givenNewEmploye_whenSave_thenCorrect() {
        //given
        Employe employe = new Employe();
        employe.setNom("testEmploye");
        //then
        employeRepository.save(employe);
        //when
        assertThat(employe.getId()).isNotNull();
        assertThat(entityManager.find(Employe.class, employe.getId())).isEqualTo(employe);
    }
}
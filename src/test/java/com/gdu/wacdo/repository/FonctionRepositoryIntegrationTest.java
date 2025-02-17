package com.gdu.wacdo.repository;

import com.gdu.wacdo.model.Fonction;
import com.gdu.wacdo.repositories.FonctionRepository;
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
public class FonctionRepositoryIntegrationTest {

    @Autowired
    private FonctionRepository fonctionRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Transactional
    @Rollback
    public void givenNewFonction_whenSave_thenCorrect() {
        //given
        Fonction fonction = new Fonction();
        fonction.setLibelle("testIntegration");
        //then
        fonctionRepository.save(fonction);
        //when
        assertThat(fonction.getId()).isNotNull();
        assertThat(entityManager.find(Fonction.class, fonction.getId())).isEqualTo(fonction);
    }
}
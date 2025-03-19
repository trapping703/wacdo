package com.gdu.wacdo.services;

import com.gdu.wacdo.model.Employe;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class PasswordEncodeurTest {

    @Mock
    private BCryptPasswordEncoder passwordEncrypteur;
    @InjectMocks
    private PasswordEncodeur service;

    @Test
    public void givenEmployeSansMotDePasse_whenEncrypte_thenAucunAppelALEncoder() {
        //given
        Employe employe = new Employe();
        //when
        service.encrypte(employe);
        //then
        Mockito.verifyNoInteractions(passwordEncrypteur);
    }

    @Test
    public void givenEmployeMotDePasseEmpty_whenEncrypte_thenAucunAppelALEncoder() {
        //given
        Employe employe = new Employe();
        employe.setMotDePasse("");
        //when
        service.encrypte(employe);
        //then
        Mockito.verifyNoInteractions(passwordEncrypteur);
    }

    @Test
    public void givenEmployeMotDePasseNonEncode_whenEncrypte_thenAppelALEncoder() {
        //given
        Employe employe = new Employe();
        employe.setMotDePasse("test");
        //when
        service.encrypte(employe);
        //then
        Mockito.verify(passwordEncrypteur).encode("test");
        Mockito.verifyNoMoreInteractions(passwordEncrypteur);
    }

    @Test
    public void givenEmployeMotDePasseEncode_whenEncrypte_thenAucunAppelALEncoder() {
        //given
        Employe employe = new Employe();
        employe.setMotDePasse("$2y$10$MLqiUPAH7NQR8cK0mcQPVOvxoikpAmia43OmwfogmG0Y3Uuw/fwmW");
        //when
        service.encrypte(employe);
        //then
        Mockito.verifyNoInteractions(passwordEncrypteur);
    }
}
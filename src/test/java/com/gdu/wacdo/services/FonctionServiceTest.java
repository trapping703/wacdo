package com.gdu.wacdo.services;

import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Fonction;
import com.gdu.wacdo.repositories.FonctionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class FonctionServiceTest {

    @Autowired
    private ModelMapper modelMapper;
    @Mock
    private FonctionRepository fonctionRepository;
    @InjectMocks
    private FonctionService fonctionService;

    @Test
    void givenFonctionSansID_whenSaveSucess_thenReturnFonctionAvecIdEtReponseOK() {
        //given
        Fonction fonction = new Fonction();
        int idAttendu = 1;
        Mockito.doAnswer(invocation -> {
            Fonction fonction1 = invocation.getArgument(0);
            fonction1.setId(idAttendu);
            return fonction1;
        }).when(fonctionRepository).save(fonction);
        //when
        ReponseService<Fonction> reponse = fonctionService.save(fonction);
        //then
        assertThat(reponse.isOk()).isTrue();
        assertThat(reponse.getData().getId()).isEqualTo(idAttendu);
        verify(fonctionRepository, Mockito.times(1)).save(fonction);
        verifyNoMoreInteractions(fonctionRepository);
    }

    @Test
    void givenFonctionSansID_whenSaveFail_thenReturnFonctionSansIdEtReponsError() {
        //given
        Fonction fonction = new Fonction();
        Mockito.doReturn(fonction).when(fonctionRepository).save(fonction);
        //when
        ReponseService<Fonction> reponse = fonctionService.save(fonction);
        //then
        assertThat(reponse.isError()).isTrue();
        assertThat(reponse.getData().getId()).isNull();
        verify(fonctionRepository, Mockito.times(1)).save(fonction);
        verifyNoMoreInteractions(fonctionRepository);
    }

    @Test
    void givenException_whenSave_thenReturnReponsErrorEtEx() {
        //given
        Fonction fonction = new Fonction();
        Exception exception = new RuntimeException();
        Mockito.doThrow(exception).when(fonctionRepository).save(fonction);
        //when
        ReponseService<Fonction> reponse = fonctionService.save(fonction);
        //then
        assertThat(reponse.isError()).isTrue();
        assertThat(reponse.getException()).isEqualTo(exception);
        verify(fonctionRepository, Mockito.times(1)).save(fonction);
        verifyNoMoreInteractions(fonctionRepository);
    }

    @Test
    void GivenListFonction_whenFindAll_thenReturnListFonctionEtReponseOK() {
        //given
        List<Fonction> fonctions = new ArrayList<>();
        doReturn(fonctions).when(fonctionRepository).findAll();
        //when
        ReponseService<List<Fonction>> reponse = fonctionService.findAll();
        //then
        assertThat(reponse.isOk()).isTrue();
        assertThat(reponse.getData()).isEqualTo(fonctions);
        verify(fonctionRepository, Mockito.times(1)).findAll();
        verifyNoMoreInteractions(fonctionRepository);
    }

    @Test
    void givenIdFonctionExistante_whenFindById_thenReturnFonctionEtReponseOK() {
        //given
        Fonction fonction = new Fonction();
        doReturn(Optional.of(fonction)).when(fonctionRepository).findById(1);
        //when
        ReponseService<Fonction> reponse = fonctionService.findById(1);
        //then
        assertThat(reponse.isOk()).isTrue();
        assertThat(reponse.getData()).isEqualTo(fonction);
        verify(fonctionRepository, Mockito.times(1)).findById(1);
        verifyNoMoreInteractions(fonctionRepository);
    }

    @Test
    void givenIdFonctionNonExistante_whenFindById_thenReturnReponseEmpty() {
        //given
        doReturn(Optional.empty()).when(fonctionRepository).findById(1);
        //when
        ReponseService<Integer> reponse = fonctionService.findById(1);
        //then
        assertThat(reponse.isEmpty()).isTrue();
        assertThat(reponse.getData()).isEqualTo(1);
        verify(fonctionRepository, Mockito.times(1)).findById(1);
        verifyNoMoreInteractions(fonctionRepository);
    }

    @Test
    void givenExcpetion_whenFindById_thenReturnReponseError() {
        //given
        Exception exception = new RuntimeException();
        Mockito.doThrow(exception).when(fonctionRepository).findById(1);
        //when
        ReponseService<Integer> reponse = fonctionService.findById(1);
        //then
        assertThat(reponse.isError()).isTrue();
        assertThat(reponse.getException()).isEqualTo(exception);
        verify(fonctionRepository, Mockito.times(1)).findById(1);
        verifyNoMoreInteractions(fonctionRepository);
    }

    @Test
    void givenFonctionExistante_whenFindByLibelle_thenReturnFonctionsEtReponseOK() {
        //given
        Fonction fonction = new Fonction();
        String libelle = "libelle";
        doReturn(fonction).when(fonctionRepository).findByLibelleContaining(libelle);
        //when
        ReponseService<Fonction> reponse = fonctionService.findByLibelle(libelle);
        //then
        assertThat(reponse.isOk()).isTrue();
        assertThat(reponse.getData()).isEqualTo(fonction);
        verify(fonctionRepository, Mockito.times(1)).findByLibelleContaining(libelle);
        verifyNoMoreInteractions(fonctionRepository);
    }

    @Test
    void givenException_whenFindByLibelle_thenReturnExceptionEtReponseError() {
        //given
        Exception exception = new RuntimeException();
        String libelle = "libelle";
        doThrow(exception).when(fonctionRepository).findByLibelleContaining(libelle);
        //when
        ReponseService reponse = fonctionService.findByLibelle(libelle);
        //then
        assertThat(reponse.isError()).isTrue();
        assertThat(reponse.getException()).isEqualTo(exception);
        verify(fonctionRepository, Mockito.times(1)).findByLibelleContaining(libelle);
        verifyNoMoreInteractions(fonctionRepository);
    }
}
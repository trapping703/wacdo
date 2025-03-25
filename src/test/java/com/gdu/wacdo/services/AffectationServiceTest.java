package com.gdu.wacdo.services;

import com.gdu.wacdo.dto.form.RechercheAffectation;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Affectation;
import com.gdu.wacdo.repositories.AffectationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class AffectationServiceTest {

    @Autowired
    private ModelMapper modelMapper;
    @Mock
    private AffectationRepository affectationRepository;
    @InjectMocks
    private AffectationService affectationService;

    @Test
    void givenAffectationSansID_whenSaveSucess_thenReturnAffectationAvecIdEtReponseOK() {
        //given
        Affectation affectation = new Affectation();
        int idAttendu = 1;
        Mockito.doAnswer(invocation -> {
            Affectation affectation1 = invocation.getArgument(0);
            affectation1.setId(idAttendu);
            return affectation1;
        }).when(affectationRepository).save(affectation);
        //when
        ReponseService<Affectation> reponse = affectationService.save(affectation);
        //then
        assertThat(reponse.isOk()).isTrue();
        assertThat(reponse.getObjetRetour().getId()).isEqualTo(idAttendu);
        verify(affectationRepository, Mockito.times(1)).save(affectation);
        verifyNoMoreInteractions(affectationRepository);
    }

    @Test
    void givenAffectationSansID_whenSaveFail_thenReturnAffectationSansIdEtReponsError() {
        //given
        Affectation affectation = new Affectation();
        Mockito.doReturn(affectation).when(affectationRepository).save(affectation);
        //when
        ReponseService<Affectation> reponse = affectationService.save(affectation);
        //then
        assertThat(reponse.isError()).isTrue();
        assertThat(reponse.getObjetRetour().getId()).isNull();
        verify(affectationRepository, Mockito.times(1)).save(affectation);
        verifyNoMoreInteractions(affectationRepository);
    }

    @Test
    void givenException_whenSave_thenReturnReponsErrorEtEx() {
        //given
        Affectation affectation = new Affectation();
        Exception exception = new RuntimeException();
        Mockito.doThrow(exception).when(affectationRepository).save(affectation);
        //when
        ReponseService<Affectation> reponse = affectationService.save(affectation);
        //then
        assertThat(reponse.isError()).isTrue();
        assertThat(reponse.getException()).isEqualTo(exception);
        verify(affectationRepository, Mockito.times(1)).save(affectation);
        verifyNoMoreInteractions(affectationRepository);
    }

    @Test
    void GivenListAffectation_whenFindAll_thenReturnListAffectationEtReponseOK() {
        //given
        List<Affectation> affectations = new ArrayList<>();
        doReturn(affectations).when(affectationRepository).findAll();
        //when
        ReponseService<List<Affectation>> reponse = affectationService.findAll();
        //then
        assertThat(reponse.isOk()).isTrue();
        assertThat(reponse.getData()).isEqualTo(affectations);
        verify(affectationRepository, Mockito.times(1)).findAll();
        verifyNoMoreInteractions(affectationRepository);
    }

    @Test
    void givenIdAffectationExistante_whenFindById_thenReturnAffectationEtReponseOK() {
        //given
        Affectation affectation = new Affectation();
        doReturn(Optional.of(affectation)).when(affectationRepository).findById(1);
        //when
        ReponseService<Affectation> reponse = affectationService.findById(1);
        //then
        assertThat(reponse.isOk()).isTrue();
        assertThat(reponse.getData()).isEqualTo(affectation);
        verify(affectationRepository, Mockito.times(1)).findById(1);
        verifyNoMoreInteractions(affectationRepository);
    }

    @Test
    void givenIdAffectationNonExistante_whenFindById_thenReturnReponseEmpty() {
        //given
        doReturn(Optional.empty()).when(affectationRepository).findById(1);
        //when
        ReponseService reponse = affectationService.findById(1);
        //then
        assertThat(reponse.isEmpty()).isTrue();
        assertThat(reponse.getData()).isEqualTo(1);
        verify(affectationRepository, Mockito.times(1)).findById(1);
        verifyNoMoreInteractions(affectationRepository);
    }

    @Test
    void givenExcpetion_whenFindById_thenReturnReponseError() {
        //given
        Exception exception = new RuntimeException();
        Mockito.doThrow(exception).when(affectationRepository).findById(1);
        //when
        ReponseService reponse = affectationService.findById(1);
        //then
        assertThat(reponse.isError()).isTrue();
        assertThat(reponse.getException()).isEqualTo(exception);
        verify(affectationRepository, Mockito.times(1)).findById(1);
        verifyNoMoreInteractions(affectationRepository);
    }

    @Test
    void givenAffectationsExistante_whenFindByRechercheAffectation_VueListeAffection_thenReturnAffectationsEtReponseOK() {
        //given
        List<Affectation> affectations = List.of(new Affectation(), new Affectation());
        RechercheAffectation rechercheAffectation = new RechercheAffectation("ville", LocalDate.now(), LocalDate.now(), 1);
        doReturn(affectations).when(affectationRepository).findAffectationsPourRechercheListeAffection(rechercheAffectation.getVille(), rechercheAffectation.getDateDebut(), rechercheAffectation.getDateFin(), rechercheAffectation.getFonction_id());
        //when
        ReponseService<List<Affectation>> reponse = affectationService.findAffectationsPourRechercheListeAffection(rechercheAffectation);
        //then
        assertThat(reponse.isOk()).isTrue();
        assertThat(reponse.getData()).isEqualTo(affectations);
        verify(affectationRepository, Mockito.times(1)).findAffectationsPourRechercheListeAffection("ville", LocalDate.now(), LocalDate.now(), 1);
        verifyNoMoreInteractions(affectationRepository);
    }

    @Test
    void givenAffectationsNonExistante_whenFindByRechercheAffectation_thenReturnRechercheAffectationVueListeAffectionEtReponseEmpty() {
        //given
        RechercheAffectation rechercheAffectation = new RechercheAffectation("ville", LocalDate.now(), LocalDate.now(), 1);
        doReturn(List.of()).when(affectationRepository).findAffectationsPourRechercheListeAffection(rechercheAffectation.getVille(), rechercheAffectation.getDateDebut(), rechercheAffectation.getDateFin(), rechercheAffectation.getFonction_id());
        //when
        ReponseService reponse = affectationService.findAffectationsPourRechercheListeAffection(rechercheAffectation);
        //then
        assertThat(reponse.isEmpty()).isTrue();
        assertThat(reponse.getData()).isEqualTo(rechercheAffectation);
        verify(affectationRepository, Mockito.times(1)).findAffectationsPourRechercheListeAffection("ville", LocalDate.now(), LocalDate.now(), 1);
        verifyNoMoreInteractions(affectationRepository);
    }

    @Test
    void givenException_whenFindByRechercheAffectation_VueListeAffection_thenReturnExceptionEtReponseError() {
        //given
        Exception exception = new RuntimeException();
        RechercheAffectation rechercheAffectation = new RechercheAffectation("ville", LocalDate.now(), LocalDate.now(), 1);
        doThrow(exception).when(affectationRepository).findAffectationsPourRechercheListeAffection(rechercheAffectation.getVille(), rechercheAffectation.getDateDebut(), rechercheAffectation.getDateFin(), rechercheAffectation.getFonction_id());
        //when
        ReponseService reponse = affectationService.findAffectationsPourRechercheListeAffection(rechercheAffectation);
        //then
        assertThat(reponse.isError()).isTrue();
        assertThat(reponse.getData()).isEqualTo(rechercheAffectation);
        assertThat(reponse.getException()).isEqualTo(exception);
        verify(affectationRepository, Mockito.times(1)).findAffectationsPourRechercheListeAffection("ville", LocalDate.now(), LocalDate.now(), 1);
        verifyNoMoreInteractions(affectationRepository);
    }
}
package com.gdu.wacdo.services;

import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Employe;
import com.gdu.wacdo.repositories.EmployeRepository;
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
class EmployeServiceTest {

    @Autowired
    private ModelMapper modelMapper;
    @Mock
    private EmployeRepository employeRepository;
    @InjectMocks
    private EmployeService employeService;

    @Test
    void givenEmployeSansID_whenSaveSucess_thenReturnEmployeAvecIdEtReponseOK() {
        //given
        Employe employe = new Employe();
        int idAttendu = 1;
        Mockito.doAnswer(invocation -> {
            Employe employe1 = invocation.getArgument(0);
            employe1.setId(idAttendu);
            return employe1;
        }).when(employeRepository).save(employe);
        //when
        ReponseService<Employe> reponse = employeService.save(employe);
        //then
        assertThat(reponse.isOk()).isTrue();
        assertThat(reponse.getData().getId()).isEqualTo(idAttendu);
        verify(employeRepository, Mockito.times(1)).save(employe);
        verifyNoMoreInteractions(employeRepository);
    }

    @Test
    void givenEmployeSansID_whenSaveFail_thenReturnEmployeSansIdEtReponsError() {
        //given
        Employe employe = new Employe();
        Mockito.doReturn(employe).when(employeRepository).save(employe);
        //when
        ReponseService<Employe> reponse = employeService.save(employe);
        //then
        assertThat(reponse.isError()).isTrue();
        assertThat(reponse.getData().getId()).isNull();
        verify(employeRepository, Mockito.times(1)).save(employe);
        verifyNoMoreInteractions(employeRepository);
    }

    @Test
    void givenException_whenSave_thenReturnReponsErrorEtEx() {
        //given
        Employe employe = new Employe();
        Exception exception = new RuntimeException();
        Mockito.doThrow(exception).when(employeRepository).save(employe);
        //when
        ReponseService<Employe> reponse = employeService.save(employe);
        //then
        assertThat(reponse.isError()).isTrue();
        assertThat(reponse.getException()).isEqualTo(exception);
        verify(employeRepository, Mockito.times(1)).save(employe);
        verifyNoMoreInteractions(employeRepository);
    }

    @Test
    void GivenListEmploye_whenFindAll_thenReturnListEmployeEtReponseOK() {
        //given
        List<Employe> employes = new ArrayList<>();
        doReturn(employes).when(employeRepository).findAll();
        //when
        ReponseService<List<Employe>> reponse = employeService.findAll();
        //then
        assertThat(reponse.isOk()).isTrue();
        assertThat(reponse.getData()).isEqualTo(employes);
        verify(employeRepository, Mockito.times(1)).findAll();
        verifyNoMoreInteractions(employeRepository);
    }

    @Test
    void givenIdEmployeExistant_whenFindById_thenReturnEmployeEtReponseOK() {
        //given
        Employe employe = new Employe();
        doReturn(Optional.of(employe)).when(employeRepository).findById(1);
        //when
        ReponseService<Employe> reponse = employeService.findById(1);
        //then
        assertThat(reponse.isOk()).isTrue();
        assertThat(reponse.getData()).isEqualTo(employe);
        verify(employeRepository, Mockito.times(1)).findById(1);
        verifyNoMoreInteractions(employeRepository);
    }

    @Test
    void givenIdEmployeNonExistant_whenFindById_thenReturnReponseEmpty() {
        //given
        doReturn(Optional.empty()).when(employeRepository).findById(1);
        //when
        ReponseService<Integer> reponse = employeService.findById(1);
        //then
        assertThat(reponse.isEmpty()).isTrue();
        assertThat(reponse.getData()).isEqualTo(1);
        verify(employeRepository, Mockito.times(1)).findById(1);
        verifyNoMoreInteractions(employeRepository);
    }

    @Test
    void givenExcpetion_whenFindById_thenReturnReponseError() {
        //given
        Exception exception = new RuntimeException();
        Mockito.doThrow(exception).when(employeRepository).findById(1);
        //when
        ReponseService<Integer> reponse = employeService.findById(1);
        //then
        assertThat(reponse.isError()).isTrue();
        assertThat(reponse.getException()).isEqualTo(exception);
        verify(employeRepository, Mockito.times(1)).findById(1);
        verifyNoMoreInteractions(employeRepository);
    }
}
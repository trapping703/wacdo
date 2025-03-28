package com.gdu.wacdo.controllers.employe;

import com.gdu.wacdo.dto.form.RechercheEmployeDTO;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Employe;
import com.gdu.wacdo.services.EmployeNonAffecteService;
import com.gdu.wacdo.services.EmployeService;
import com.gdu.wacdo.status.CodeReponse;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(ListeEmployeController.class)
public class ListeEmployeControllerTest {

    @Autowired
    private MockMvc mvc;
    @TestBean
    private ModelMapper modelMapper;
    @MockitoBean
    private EmployeNonAffecteService employeNonAffecteService;
    @MockitoBean
    private EmployeService employeService;

    static ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenListeEmployeTrouvee_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Employe>> reponse = new ReponseService(CodeReponse.OK, List.of(new Employe()));
        doReturn(reponse).when(employeService).findAll();
        //when + then
        mvc.perform(get("/listeEmployes")
                .contentType(MediaType.TEXT_HTML)).andExpect(MockMvcResultMatchers.status()
                .is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenServiceError_alorsReponseException() throws Exception {
        //given
        ReponseService<List<Employe>> reponse = new ReponseService(CodeReponse.ERROR, null, new RuntimeException());
        doReturn(reponse).when(employeService).findAll();
        //when + then
        Assertions.assertThrows(ServletException.class, () -> mvc.perform(get("/listeEmployes")));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenRechercheEmployeTrouvee_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Employe>> reponse = new ReponseService(CodeReponse.OK, List.of(new Employe()));
        doReturn(reponse).when(employeService).findByRechercheEmploye(any(RechercheEmployeDTO.class));
        //when + then
        mvc.perform(post("/rechercheEmployes")
                        .param("nom", "test")
                        .param("ville", "test")
                        .param("email", "test@test.fr")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenRechercheEmployeEmpty_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Employe>> reponse = new ReponseService(CodeReponse.EMPTY, emptyList());
        doReturn(reponse).when(employeService).findByRechercheEmploye(any(RechercheEmployeDTO.class));
        //when + then
        mvc.perform(post("/rechercheEmployes")
                        .param("nom", "test")
                        .param("ville", "test")
                        .param("email", "test@test.fr")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeExists("recherchevide"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenRechercheEmployeError_alorsReponseException() throws Exception {
        //given
        ReponseService<List<Employe>> reponse = new ReponseService(CodeReponse.ERROR, emptyList(), new RuntimeException());
        doReturn(reponse).when(employeService).findByRechercheEmploye(any(RechercheEmployeDTO.class));
        //when + then
        Assertions.assertThrows(ServletException.class, () -> mvc.perform(post("/rechercheEmployes")
                .param("nom", "test")
                .param("ville", "test")
                .param("email", "test@test.fr")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)));
    }
}
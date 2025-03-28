package com.gdu.wacdo.controllers.employe;

import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.*;
import com.gdu.wacdo.model.Employe;
import com.gdu.wacdo.services.*;
import com.gdu.wacdo.services.EmployeService;
import com.gdu.wacdo.status.CodeReponse;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(GestionEmployeController.class)
class GestionEmployeControllerTest {

    @Autowired
    private MockMvc mvc;
    @TestBean
    private ModelMapper modelMapper;
    @MockitoBean
    private EmployeService employeService;

    static ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenCreerEmploye_alorsReponse200() throws Exception {
        //when + then
        mvc.perform(get("/creerEmploye")
                .contentType(MediaType.TEXT_HTML)).andExpect(MockMvcResultMatchers.status()
                .is2xxSuccessful());
    }


    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenEmployeCreer_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Employe>> reponse = new ReponseService(CodeReponse.OK, new Employe());
        doReturn(reponse).when(employeService).save(any(Employe.class));
        //when + then
        mvc.perform(post("/creerEmploye")
                        .param("nom", "test")
                        .param("prenom", "test")
                        .param("email", "test@test.fr")
                        .param("dateEmbauche", "2025-08-02")
                        .param("motDePasse", "test")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeExists("messageEnregistrement"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenEmployeCreerEmpty_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Employe>> reponse = new ReponseService(CodeReponse.EMPTY, new Employe());
        doReturn(reponse).when(employeService).save(any(Employe.class));
        //when + then
        mvc.perform(post("/creerEmploye")
                        .param("nom", "test")
                        .param("prenom", "test")
                        .param("email", "test@test.fr")
                        .param("dateEmbauche", "2025-08-02")
                        .param("motDePasse", "test")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeExists("messageNonEnregistrement"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenEmployeCreerError_alorsReponse200() throws Exception {
        //given
        ReponseService<Employe> reponse = new ReponseService(CodeReponse.ERROR, new Employe());
        doReturn(reponse).when(employeService).save(any(Employe.class));
        //when + then
        Assertions.assertThrows(ServletException.class, () -> mvc.perform(post("/creerEmploye")
                .param("nom", "test")
                .param("prenom", "test")
                .param("email", "test@test.fr")
                .param("dateEmbauche", "2025-08-02")
                .param("motDePasse", "test")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenEmployeCreerFormError_alorsReponse200() throws Exception {
        //given
        //when + then
        mvc.perform(post("/creerEmploye")
                        .param("nom", "test")
                        .param("prenom", "test")
                        .param("email", "test")
                        .param("dateEmbauche", "2025-08-02")
                        .param("motDePasse", "test")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("messageEnregistrement"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("messageNonEnregistrement"));
    }


    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenEditerEmploye_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Employe>> reponse = new ReponseService(CodeReponse.OK, new Employe());
        doReturn(reponse).when(employeService).findById(1);
        //when + then
        mvc.perform(get("/editerEmploye/1")
                .contentType(MediaType.TEXT_HTML)).andExpect(MockMvcResultMatchers.status()
                .is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenEmployeEditerFormError_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Employe>> reponse = new ReponseService(CodeReponse.OK, new Employe());
        doReturn(reponse).when(employeService).save(any(Employe.class));
        //when + then
        mvc.perform(post("/editerEmploye")
                        .param("id", "1")
                        .param("nom", "test")
                        .param("prenom", "test")
                        .param("email", "test")
                        .param("dateEmbauche", "2025-08-02")
                        .param("motDePasse", "test")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("messageEnregistrement"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("messageNonEnregistrement"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenEmployeEditer_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Employe>> reponse = new ReponseService(CodeReponse.OK, new Employe());
        doReturn(reponse).when(employeService).save(any(Employe.class));
        doReturn(new ReponseService(CodeReponse.OK, new Employe())).when(employeService).findById(1);
        //when + then
        mvc.perform(post("/editerEmploye")
                        .param("id", "1")
                        .param("nom", "test")
                        .param("prenom", "test")
                        .param("email", "test@test.fr")
                        .param("dateEmbauche", "2025-08-02")
                        .param("motDePasse", "test")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeExists("messageEnregistrement"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenEmployeEditerEmpty_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Employe>> reponse = new ReponseService(CodeReponse.EMPTY, new Employe());
        doReturn(reponse).when(employeService).save(any(Employe.class));
        doReturn(new ReponseService(CodeReponse.OK, new Employe())).when(employeService).findById(1);
        //when + then
        mvc.perform(post("/editerEmploye")
                        .param("id", "1")
                        .param("nom", "test")
                        .param("prenom", "test")
                        .param("email", "test@test.fr")
                        .param("dateEmbauche", "2025-08-02")
                        .param("motDePasse", "test")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeExists("messageNonEnregistrement"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenEmployeEditerError_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Employe>> reponse = new ReponseService(CodeReponse.ERROR, new Employe());
        doReturn(reponse).when(employeService).save(any(Employe.class));
        //when + then
        Assertions.assertThrows(ServletException.class, () -> mvc.perform(post("/editerEmploye")
                .param("nom", "1")
                .param("nom", "test")
                .param("prenom", "test")
                .param("email", "test@test.fr")
                .param("dateEmbauche", "2025-08-02")
                .param("motDePasse", "test")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)));
    }
}
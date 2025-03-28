package com.gdu.wacdo.controllers.fonction;

import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Fonction;
import com.gdu.wacdo.services.FonctionService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(GestionFonctionController.class)
class GestionFonctionControllerTest {

    @Autowired
    private MockMvc mvc;
    @TestBean
    private ModelMapper modelMapper;
    @MockitoBean
    private FonctionService fonctionService;

    static ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenCreerFonction_alorsReponse200() throws Exception {
        //when + then
        mvc.perform(get("/creerFonction")
                .contentType(MediaType.TEXT_HTML)).andExpect(MockMvcResultMatchers.status()
                .is2xxSuccessful());
    }


    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenFonctionCreer_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Fonction>> reponse = new ReponseService(CodeReponse.OK, new Fonction());
        doReturn(reponse).when(fonctionService).save(any(Fonction.class));
        //when + then
        mvc.perform(post("/creerFonction")
                        .param("libelle", "test")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeExists("messageEnregistrement"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenFonctionCreerEmpty_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Fonction>> reponse = new ReponseService(CodeReponse.EMPTY, new Fonction());
        doReturn(reponse).when(fonctionService).save(any(Fonction.class));
        //when + then
        mvc.perform(post("/creerFonction")
                        .param("libelle", "test")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeExists("messageNonEnregistrement"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenFonctionCreerError_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Fonction>> reponse = new ReponseService(CodeReponse.ERROR, new Fonction());
        doReturn(reponse).when(fonctionService).save(any(Fonction.class));
        //when + then
        Assertions.assertThrows(ServletException.class, () -> mvc.perform(post("/creerFonction")
                .param("libelle", "test")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)));
    }


    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenEditerFonction_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Fonction>> reponse = new ReponseService(CodeReponse.OK, new Fonction());
        doReturn(reponse).when(fonctionService).findById(1);
        //when + then
        mvc.perform(get("/editerFonction/1")
                .contentType(MediaType.TEXT_HTML)).andExpect(MockMvcResultMatchers.status()
                .is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenFonctionEditerFormError_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Fonction>> reponse = new ReponseService(CodeReponse.OK, new Fonction());
        doReturn(reponse).when(fonctionService).save(any(Fonction.class));
        //when + then
        mvc.perform(post("/editerFonction")
                        .param("libelle", "")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("messageEnregistrement"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("messageNonEnregistrement"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenFonctionEditer_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Fonction>> reponse = new ReponseService(CodeReponse.OK, new Fonction());
        doReturn(reponse).when(fonctionService).save(any(Fonction.class));
        //when + then
        mvc.perform(post("/editerFonction")
                        .param("libelle", "test")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeExists("messageEnregistrement"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenFonctionEditerEmpty_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Fonction>> reponse = new ReponseService(CodeReponse.EMPTY, new Fonction());
        doReturn(reponse).when(fonctionService).save(any(Fonction.class));
        //when + then
        mvc.perform(post("/editerFonction")
                        .param("libelle", "test")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeExists("messageNonEnregistrement"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenFonctionEditerError_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Fonction>> reponse = new ReponseService(CodeReponse.ERROR, new Fonction());
        doReturn(reponse).when(fonctionService).save(any(Fonction.class));
        //when + then
        Assertions.assertThrows(ServletException.class, () -> mvc.perform(post("/editerFonction")
                .param("libelle", "test")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)));
    }
}
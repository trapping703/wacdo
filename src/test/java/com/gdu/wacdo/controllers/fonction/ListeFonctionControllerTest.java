package com.gdu.wacdo.controllers.fonction;

import com.gdu.wacdo.dto.form.RechercheFonctionDTO;
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

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(ListeFonctionController.class)
public class ListeFonctionControllerTest {

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
    public void givenListeFonctionTrouvee_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Fonction>> reponse = new ReponseService(CodeReponse.OK, List.of(new Fonction()));
        doReturn(reponse).when(fonctionService).findAll();
        //when + then
        mvc.perform(get("/listeFonctions")
                .contentType(MediaType.TEXT_HTML)).andExpect(MockMvcResultMatchers.status()
                .is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenServiceError_alorsReponseException() throws Exception {
        //given
        ReponseService<List<Fonction>> reponse = new ReponseService(CodeReponse.ERROR, null, new RuntimeException());
        doReturn(reponse).when(fonctionService).findAll();
        //when + then
        Assertions.assertThrows(ServletException.class, () -> mvc.perform(get("/listeFonctions")));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenRechercheFonctionTrouvee_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Fonction>> reponse = new ReponseService(CodeReponse.OK, List.of(new Fonction()));
        doReturn(reponse).when(fonctionService).findByLibelle(anyString());
        //when + then
        mvc.perform(post("/rechercheFonctions")
                        .param("libelle", "test")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenRechercheFonctionEmpty_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Fonction>> reponse = new ReponseService(CodeReponse.EMPTY, emptyList());
        doReturn(reponse).when(fonctionService).findByLibelle(anyString());
        //when + then
        mvc.perform(post("/rechercheFonctions")
                        .param("libelle", "test")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeExists("recherchevide"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenRechercheFonctionError_alorsReponseException() throws Exception {
        //given
        ReponseService<List<Fonction>> reponse = new ReponseService(CodeReponse.ERROR, emptyList(), new RuntimeException());
        doReturn(reponse).when(fonctionService).findByLibelle(anyString());
        //when + then
        Assertions.assertThrows(ServletException.class, () -> mvc.perform(post("/rechercheFonctions", new RechercheFonctionDTO("test"))
                .param("libelle", "test")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)));
    }
}
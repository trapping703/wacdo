package com.gdu.wacdo.controllers.affectation;

import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Affectation;
import com.gdu.wacdo.model.Employe;
import com.gdu.wacdo.model.Fonction;
import com.gdu.wacdo.model.Restaurant;
import com.gdu.wacdo.services.AffectationService;
import com.gdu.wacdo.services.EmployeService;
import com.gdu.wacdo.services.FonctionService;
import com.gdu.wacdo.services.RestaurantService;
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

@WebMvcTest(GestionAffectationController.class)
class GestionAffectationControllerTest {

    @Autowired
    private MockMvc mvc;
    @TestBean
    private ModelMapper modelMapper;
    @MockitoBean
    private AffectationService affectationService;
    @MockitoBean
    private  RestaurantService restaurantService;
    @MockitoBean
    private  FonctionService fonctionService;
    @MockitoBean
    private  EmployeService employeService;

    static ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @BeforeEach
    void setUp() {
        doReturn(new ReponseService(CodeReponse.OK, List.of(new Fonction()))).when(fonctionService).findAll();
        doReturn(new ReponseService(CodeReponse.OK, List.of(new Employe()))).when(employeService).findAll();
        doReturn(new ReponseService(CodeReponse.OK, List.of(new Restaurant()))).when(restaurantService).findAll();
        doReturn(new ReponseService(CodeReponse.OK, new Restaurant())).when(restaurantService).findById(1);
        doReturn(new ReponseService(CodeReponse.OK, new Employe())).when(employeService).findById(1);
        doReturn(new ReponseService(CodeReponse.OK, new Fonction())).when(fonctionService).findById(1);
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenCreerAffectationDepuisRestaurant_alorsReponse200() throws Exception {
        //when + then
        mvc.perform(get("/creerAffectation?restaurant=1")
                .contentType(MediaType.TEXT_HTML)).andExpect(MockMvcResultMatchers.status()
                .is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeExists("affectationDTO"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenCreerAffectationDepuisEmploye_alorsReponse200() throws Exception {
        //when + then
        mvc.perform(get("/creerAffectation?employe=1")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeExists("affectationDTO"));
    }


    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenAffectationCreer_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Affectation>> reponse = new ReponseService(CodeReponse.OK, new Affectation(LocalDate.now(), LocalDate.now(), new Employe(), new Restaurant(), new Fonction()));
        doReturn(reponse).when(affectationService).save(any(Affectation.class));
        //when + then
        mvc.perform(post("/creerAffectation")
                        .param("dateDebut", "2025-08-02")
                        .param("dateFin", "2025-08-02")
                        .param("employe", "1")
                        .param("restaurant", "1")
                        .param("fonction", "1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeExists("messageEnregistrement"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenAffectationCreerEmpty_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Affectation>> reponse = new ReponseService(CodeReponse.EMPTY, new Affectation(LocalDate.now(), LocalDate.now(), new Employe(), new Restaurant(), new Fonction()));
        doReturn(reponse).when(affectationService).save(any(Affectation.class));
        //when + then
        mvc.perform(post("/creerAffectation")
                        .param("dateDebut", "2025-08-02")
                        .param("dateFin", "2025-08-02")
                        .param("employe", "1")
                        .param("restaurant", "1")
                        .param("fonction", "1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeExists("messageNonEnregistrement"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenAffectationCreerError_alorsReponse200() throws Exception {
        //given
        ReponseService<Affectation> reponse = new ReponseService(CodeReponse.ERROR, new Affectation(LocalDate.now(), LocalDate.now(), new Employe(), new Restaurant(), new Fonction()));
        doReturn(reponse).when(affectationService).save(any(Affectation.class));
        //when + then
        Assertions.assertThrows(ServletException.class, () -> mvc.perform(post("/creerAffectation")
                .param("dateDebut", "2025-08-02")
                .param("dateFin", "2025-08-02")
                .param("employe", "1")
                .param("restaurant", "1")
                .param("fonction", "1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenAffectationCreerFormError_alorsReponse200() throws Exception {
        //given
        //when + then
        mvc.perform(post("/creerAffectation")
                        .param("dateDebut", "2025-08-02")
                        .param("dateFin", "2025-08-02")
                        .param("employe", "1")
                        .param("restaurant", "1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("messageEnregistrement"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("messageNonEnregistrement"));
    }


    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenEditerAffectation_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Affectation>> reponse = new ReponseService(CodeReponse.OK, new Affectation(LocalDate.now(), LocalDate.now(), new Employe(), new Restaurant(), new Fonction()));
        doReturn(reponse).when(affectationService).findById(1);
        //when + then
        mvc.perform(get("/editerAffectation/1")
                .contentType(MediaType.TEXT_HTML)).andExpect(MockMvcResultMatchers.status()
                .is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenAffectationEditerFormError_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Affectation>> reponse = new ReponseService(CodeReponse.OK, new Affectation(LocalDate.now(), LocalDate.now(), new Employe(), new Restaurant(), new Fonction()));
        doReturn(reponse).when(affectationService).save(any(Affectation.class));
        //when + then
        mvc.perform(post("/editerAffectation")
                        .param("id", "1")
                        .param("dateDebut", "2025-08-02")
                        .param("dateFin", "2025-08-02")
                        .param("employe", "1")
                        .param("restaurant", "1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("messageEnregistrement"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("messageNonEnregistrement"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenAffectationEditer_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Affectation>> reponse = new ReponseService(CodeReponse.OK, new Affectation(LocalDate.now(), LocalDate.now(), new Employe(), new Restaurant(), new Fonction()));
        doReturn(reponse).when(affectationService).save(any(Affectation.class));
        doReturn(new ReponseService(CodeReponse.OK, new Affectation(LocalDate.now(), LocalDate.now(), new Employe(), new Restaurant(), new Fonction()))).when(affectationService).findById(1);
        //when + then
        mvc.perform(post("/editerAffectation")
                        .param("id", "1")
                        .param("dateDebut", "2025-08-02")
                        .param("dateFin", "2025-08-02")
                        .param("employe", "1")
                        .param("restaurant", "1")
                        .param("fonction", "1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeExists("messageEnregistrement"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenAffectationEditerEmpty_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Affectation>> reponse = new ReponseService(CodeReponse.EMPTY, new Affectation(LocalDate.now(), LocalDate.now(), new Employe(), new Restaurant(), new Fonction()));
        doReturn(reponse).when(affectationService).save(any(Affectation.class));
        doReturn(new ReponseService(CodeReponse.OK, new Affectation(LocalDate.now(), LocalDate.now(), new Employe(), new Restaurant(), new Fonction()))).when(affectationService).findById(1);
        //when + then
        mvc.perform(post("/editerAffectation")
                        .param("id", "1")
                        .param("dateDebut", "2025-08-02")
                        .param("dateFin", "2025-08-02")
                        .param("employe", "1")
                        .param("restaurant", "1")
                        .param("fonction", "1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeExists("messageNonEnregistrement"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenAffectationEditerError_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Affectation>> reponse = new ReponseService(CodeReponse.ERROR, new Affectation(LocalDate.now(), LocalDate.now(), new Employe(), new Restaurant(), new Fonction()));
        doReturn(reponse).when(affectationService).save(any(Affectation.class));
        //when + then
        Assertions.assertThrows(ServletException.class, () -> mvc.perform(post("/editerAffectation")
                .param("id", "1")
                .param("dateDebut", "2025-08-02")
                .param("dateFin", "2025-08-02")
                .param("employe", "1")
                .param("restaurant", "1")
                .param("fonction", "1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)));
    }
}
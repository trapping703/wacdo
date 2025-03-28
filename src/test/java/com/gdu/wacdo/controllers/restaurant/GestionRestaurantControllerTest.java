package com.gdu.wacdo.controllers.restaurant;

import com.gdu.wacdo.controllers.restaurant.GestionRestaurantController;
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

@WebMvcTest(GestionRestaurantController.class)
class GestionRestaurantControllerTest {

    @Autowired
    private MockMvc mvc;
    @TestBean
    private ModelMapper modelMapper;
    @MockitoBean
    private RestaurantService restaurantService;
    @MockitoBean
    private AffectationService affectationService;
    @MockitoBean
    private FonctionService fonctionService;
    @MockitoBean
    private EmployeService employeService;

    static ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @BeforeEach
    void setUp() {
        doReturn(new ReponseService(CodeReponse.OK, List.of(new Fonction()))).when(fonctionService).findAll();
        doReturn(new ReponseService(CodeReponse.OK, List.of(new Employe()))).when(employeService).findAll();
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenCreerRestaurant_alorsReponse200() throws Exception {
        //when + then
        mvc.perform(get("/creerRestaurant")
                .contentType(MediaType.TEXT_HTML)).andExpect(MockMvcResultMatchers.status()
                .is2xxSuccessful());
    }


    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenRestaurantCreer_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Restaurant>> reponse = new ReponseService(CodeReponse.OK, new Restaurant());
        doReturn(reponse).when(restaurantService).save(any(Restaurant.class));
        ReponseService<List<Affectation>> reponseAffectations = new ReponseService(CodeReponse.OK, List.of(new Affectation(LocalDate.now(), LocalDate.now(), new Employe(), new Restaurant(), new Fonction())));
        doReturn(reponseAffectations).when(affectationService).findByDateFinIsNullAndRestaurantIs(any(Restaurant.class));
        //when + then
        mvc.perform(post("/creerRestaurant")
                        .param("nom", "test")
                        .param("ville", "test")
                        .param("codePostal", "57489")
                        .param("adresse", "test")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeExists("messageEnregistrement"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenRestaurantCreerEmpty_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Restaurant>> reponse = new ReponseService(CodeReponse.EMPTY, new Restaurant());
        doReturn(reponse).when(restaurantService).save(any(Restaurant.class));
        //when + then
        mvc.perform(post("/creerRestaurant")
                        .param("nom", "test")
                        .param("ville", "test")
                        .param("codePostal", "57489")
                        .param("adresse", "test")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeExists("messageNonEnregistrement"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenRestaurantCreerError_alorsReponse200() throws Exception {
        //given
        ReponseService<Restaurant> reponse = new ReponseService(CodeReponse.ERROR, new Restaurant());
        doReturn(reponse).when(restaurantService).save(any(Restaurant.class));
        //when + then
        Assertions.assertThrows(ServletException.class, () -> mvc.perform(post("/creerRestaurant")
                .param("nom", "test")
                .param("ville", "test")
                .param("codePostal", "57147")
                .param("adresse", "test")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenRestaurantCreerFormError_alorsReponse200() throws Exception {
        //given
        //when + then
        mvc.perform(post("/creerRestaurant")
                        .param("nom", "test")
                        .param("ville", "test")
                        .param("codePostal", "57")
                        .param("adresse", "test")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("messageEnregistrement"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("messageNonEnregistrement"));
    }


    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenEditerRestaurant_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Restaurant>> reponse = new ReponseService(CodeReponse.OK, new Restaurant());
        doReturn(reponse).when(restaurantService).findById(1);
        //when + then
        mvc.perform(get("/editerRestaurant/1")
                .contentType(MediaType.TEXT_HTML)).andExpect(MockMvcResultMatchers.status()
                .is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenRestaurantEditerFormError_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Restaurant>> reponse = new ReponseService(CodeReponse.OK, new Restaurant());
        doReturn(reponse).when(restaurantService).save(any(Restaurant.class));
        //when + then
        mvc.perform(post("/editerRestaurant")
                        .param("id", "1")
                        .param("nom", "test")
                        .param("ville", "test")
                        .param("codePostal", "57")
                        .param("adresse", "test")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("messageEnregistrement"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("messageNonEnregistrement"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenRestaurantEditer_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Restaurant>> reponse = new ReponseService(CodeReponse.OK, new Restaurant());
        doReturn(reponse).when(restaurantService).save(any(Restaurant.class));
        doReturn(new ReponseService(CodeReponse.OK, new Restaurant())).when(restaurantService).findById(1);
        ReponseService<List<Affectation>> reponseAffectations = new ReponseService(CodeReponse.OK, List.of(new Affectation(LocalDate.now(), LocalDate.now(), new Employe(), new Restaurant(), new Fonction())));
        doReturn(reponseAffectations).when(affectationService).findByDateFinIsNullAndRestaurantIs(any(Restaurant.class));
        //when + then
        mvc.perform(post("/editerRestaurant")
                        .param("id", "1")
                        .param("nom", "test")
                        .param("ville", "test")
                        .param("codePostal", "57489")
                        .param("adresse", "test")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeExists("messageEnregistrement"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenRestaurantEditerEmpty_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Restaurant>> reponse = new ReponseService(CodeReponse.EMPTY, new Restaurant());
        doReturn(reponse).when(restaurantService).save(any(Restaurant.class));
        doReturn(new ReponseService(CodeReponse.OK, new Restaurant())).when(restaurantService).findById(1);
        //when + then
        mvc.perform(post("/editerRestaurant")
                        .param("id", "1")
                        .param("nom", "test")
                        .param("ville", "test")
                        .param("codePostal", "57489")
                        .param("adresse", "test")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeExists("messageNonEnregistrement"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenRestaurantEditerError_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Restaurant>> reponse = new ReponseService(CodeReponse.ERROR, new Restaurant());
        doReturn(reponse).when(restaurantService).save(any(Restaurant.class));
        //when + then
        Assertions.assertThrows(ServletException.class, () -> mvc.perform(post("/editerRestaurant")
                .param("nom", "1")
                .param("nom", "test")
                .param("ville", "test")
                .param("codePostal", "57489")
                .param("adresse", "test")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)));
    }
}
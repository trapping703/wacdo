package com.gdu.wacdo.controllers.restaurant;

import com.gdu.wacdo.controllers.restaurant.DetailRestaurantController;
import com.gdu.wacdo.dto.form.RechercheAffectationDetailsRestaurantDTO;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.*;
import com.gdu.wacdo.model.Restaurant;
import com.gdu.wacdo.services.AffectationService;
import com.gdu.wacdo.services.RestaurantService;
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

import java.time.LocalDate;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(DetailRestaurantController.class)
public class DetailsRestaurantControllerTest {

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

    static ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givendetailRestaurantTrouvee_alorsReponse200() throws Exception {
        //given
        ReponseService<Restaurant> reponse = new ReponseService(CodeReponse.OK, new Restaurant());
        doReturn(reponse).when(restaurantService).findById(1);
        ReponseService<List<Fonction>> reponseFonction = new ReponseService(CodeReponse.OK, List.of(new Fonction()));
        doReturn(reponseFonction).when(fonctionService).findAll();
        ReponseService<List<Affectation>> reponseAffectations = new ReponseService(CodeReponse.OK, List.of(new Affectation(LocalDate.now(), LocalDate.now(), new Employe(), new Restaurant(), new Fonction())));
        doReturn(reponseAffectations).when(affectationService).findByDateFinIsNullAndRestaurantIs(any(Restaurant.class));
        //when + then
        mvc.perform(get("/detailRestaurant/1")
                .contentType(MediaType.TEXT_HTML)).andExpect(MockMvcResultMatchers.status()
                .is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenRechercheRestaurantTrouvee_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Restaurant>> reponse = new ReponseService(CodeReponse.OK, List.of(new Restaurant()));
        doReturn(reponse).when(restaurantService).findById(1);
        ReponseService<List<Fonction>> reponseFonction = new ReponseService(CodeReponse.OK, List.of(new Fonction()));
        doReturn(reponseFonction).when(fonctionService).findAll();
        ReponseService<List<Affectation>> reponseAffectations = new ReponseService(CodeReponse.OK, List.of(new Affectation(LocalDate.now(), LocalDate.now(), new Employe(), new Restaurant(), new Fonction())));
        doReturn(reponseAffectations).when(affectationService).findAffectationsPourRechercheDetailsRestaurant(any(RechercheAffectationDetailsRestaurantDTO.class), any(Integer.class));
        //when + then
        mvc.perform(post("/detailRestaurant/1")
                        .param("dateDebut", "2025-05-05")
                        .param("employe_id", "1")
                        .param("fonction_id", "1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenRechercheRestaurantEmpty_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Restaurant>> reponse = new ReponseService(CodeReponse.OK, List.of(new Restaurant()));
        doReturn(reponse).when(restaurantService).findById(1);
        ReponseService<List<Fonction>> reponseFonction = new ReponseService(CodeReponse.OK, List.of(new Fonction()));
        doReturn(reponseFonction).when(fonctionService).findAll();
        ReponseService<List<Affectation>> reponseAffectations = new ReponseService(CodeReponse.EMPTY, emptyList());
        doReturn(reponseAffectations).when(affectationService).findAffectationsPourRechercheDetailsRestaurant(any(RechercheAffectationDetailsRestaurantDTO.class), any(Integer.class));
        //when + then
        mvc.perform(post("/detailRestaurant/1")
                        .param("dateDebut", "2025-05-05")
                        .param("fonction_id", "1")
                        .param("employe_id", "1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenRechercheRestaurantError_alorsReponseException() throws Exception {
        //given
        ReponseService<List<Restaurant>> reponse = new ReponseService(CodeReponse.OK, List.of(new Restaurant()));
        doReturn(reponse).when(restaurantService).findById(1);
        ReponseService<List<Fonction>> reponseFonction = new ReponseService(CodeReponse.OK, List.of(new Fonction()));
        doReturn(reponseFonction).when(fonctionService).findAll();
        ReponseService<List<Affectation>> reponseAffectations = new ReponseService(CodeReponse.ERROR, null, new RuntimeException());
        doReturn(reponseAffectations).when(affectationService).findAffectationsPourRechercheDetailsRestaurant(any(RechercheAffectationDetailsRestaurantDTO.class), any(Integer.class));
        //when + then
        Assertions.assertThrows(ServletException.class, () -> mvc.perform(post("/detailRestaurant/1")
                .param("dateDebut", "2025-05-05")
                .param("fonction_id", "1")
                .param("employe_id", "1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)));
    }
}
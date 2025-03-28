package com.gdu.wacdo.controllers.affectation;

import com.gdu.wacdo.dto.form.RechercheAffectationDTO;
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

@WebMvcTest(ListeAffectationController.class)
public class ListeAffectationControllerTest {

    @Autowired
    private MockMvc mvc;
    @TestBean
    private ModelMapper modelMapper;
    @MockitoBean
    private FonctionService fonctionService;
    @MockitoBean
    private EmployeService employeService;
    @MockitoBean
    private RestaurantService restaurantService;
    @MockitoBean
    private AffectationService affectationService;

    static ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenListeAffectationTrouvee_alorsReponse200() throws Exception {
        //given

        ReponseService<List<Restaurant>> reponseRestaurant = new ReponseService(CodeReponse.OK, List.of(new Restaurant()));
        doReturn(reponseRestaurant).when(restaurantService).findAll();
        ReponseService<List<Fonction>> reponseFonction = new ReponseService(CodeReponse.OK, List.of(new Fonction()));
        doReturn(reponseFonction).when(fonctionService).findAll();
        ReponseService<List<Employe>> reponseEmploye = new ReponseService(CodeReponse.OK, List.of(new Employe()));
        doReturn(reponseEmploye).when(employeService).findAll();
        ReponseService<List<Affectation>> reponse = new ReponseService(CodeReponse.OK, List.of(new Affectation(LocalDate.now(), LocalDate.now(), new Employe(), new Restaurant(), new Fonction())));
        doReturn(reponse).when(affectationService).findAll();
        //when + then
        mvc.perform(get("/listeAffectations")
                .contentType(MediaType.TEXT_HTML)).andExpect(MockMvcResultMatchers.status()
                .is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenServiceError_alorsReponseException() throws Exception {
        //given
        ReponseService<List<Affectation>> reponse = new ReponseService(CodeReponse.ERROR, null, new RuntimeException());
        doReturn(reponse).when(affectationService).findAll();
        //when + then
        Assertions.assertThrows(ServletException.class, () -> mvc.perform(get("/listeAffectations")));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenRechercheAffectationTrouvee_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Restaurant>> reponseRestaurant = new ReponseService(CodeReponse.OK, List.of(new Restaurant()));
        doReturn(reponseRestaurant).when(restaurantService).findAll();
        ReponseService<List<Fonction>> reponseFonction = new ReponseService(CodeReponse.OK, List.of(new Fonction()));
        doReturn(reponseFonction).when(fonctionService).findAll();
        ReponseService<List<Employe>> reponseEmploye = new ReponseService(CodeReponse.OK, List.of(new Employe()));
        doReturn(reponseEmploye).when(employeService).findAll();
        ReponseService<List<Affectation>> reponse = new ReponseService(CodeReponse.OK, List.of(new Affectation(LocalDate.now(), LocalDate.now(), new Employe(), new Restaurant(), new Fonction())));
        doReturn(reponse).when(affectationService).findAffectationsPourRechercheListeAffection(any(RechercheAffectationDTO.class));
        //when + then
        mvc.perform(post("/rechercheAffectations")
                        .param("ville", "test")
                        .param("dateDebut", "2025-05-05")
                        .param("dateFin", "2025-05-05")
                        .param("fonction_id", "5")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenRechercheAffectationEmpty_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Affectation>> reponse = new ReponseService(CodeReponse.EMPTY, emptyList());
        doReturn(reponse).when(affectationService).findAffectationsPourRechercheListeAffection(any(RechercheAffectationDTO.class));
        ReponseService<List<Restaurant>> reponseRestaurant = new ReponseService(CodeReponse.OK, List.of(new Restaurant()));
        doReturn(reponseRestaurant).when(restaurantService).findAll();
        ReponseService<List<Fonction>> reponseFonction = new ReponseService(CodeReponse.OK, List.of(new Fonction()));
        doReturn(reponseFonction).when(fonctionService).findAll();
        ReponseService<List<Employe>> reponseEmploye = new ReponseService(CodeReponse.OK, List.of(new Employe()));
        doReturn(reponseEmploye).when(employeService).findAll();
        //when + then
        mvc.perform(post("/rechercheAffectations")
                        .param("ville", "test")
                        .param("dateDebut", "2025-05-05")
                        .param("dateFin", "2025-05-05")
                        .param("fonction_id", "5")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeExists("recherchevide"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenRechercheAffectationError_alorsReponseException() throws Exception {
        //given
        ReponseService<List<Affectation>> reponse = new ReponseService(CodeReponse.ERROR, emptyList(), new RuntimeException());
        doReturn(reponse).when(affectationService).findAffectationsPourRechercheListeAffection(any(RechercheAffectationDTO.class));
        //when + then
        Assertions.assertThrows(ServletException.class, () -> mvc.perform(post("/rechercheAffectations")
                .param("ville", "test")
                .param("dateDebut", "2025-05-05")
                .param("dateFin", "2025-05-05")
                .param("fonction_id", "5")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)));
    }
}
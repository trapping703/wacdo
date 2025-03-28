package com.gdu.wacdo.controllers.restaurant;

import com.gdu.wacdo.dto.form.RechercheRestaurantDTO;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Restaurant;
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

import java.util.List;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(ListeRestaurantController.class)
public class ListeRestaurantControllerTest {

    @Autowired
    private MockMvc mvc;
    @TestBean
    private ModelMapper modelMapper;
    @MockitoBean
    private RestaurantService restaurantService;

    static ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenListeRestaurantTrouvee_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Restaurant>> reponse = new ReponseService(CodeReponse.OK, List.of(new Restaurant()));
        doReturn(reponse).when(restaurantService).findAll();
        //when + then
        mvc.perform(get("/listeRestaurants")
                .contentType(MediaType.TEXT_HTML)).andExpect(MockMvcResultMatchers.status()
                .is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenServiceError_alorsReponseException() throws Exception {
        //given
        ReponseService<List<Restaurant>> reponse = new ReponseService(CodeReponse.ERROR, null, new RuntimeException());
        doReturn(reponse).when(restaurantService).findAll();
        //when + then
        Assertions.assertThrows(ServletException.class, () -> mvc.perform(get("/listeRestaurants")));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenRechercheRestaurantTrouvee_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Restaurant>> reponse = new ReponseService(CodeReponse.OK, List.of(new Restaurant()));
        doReturn(reponse).when(restaurantService).findByRechercheRestaurant(any(RechercheRestaurantDTO.class));
        //when + then
        mvc.perform(post("/rechercheRestaurants")
                        .param("nom", "test")
                        .param("ville", "test")
                        .param("codePostal", "45879")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenRechercheRestaurantEmpty_alorsReponse200() throws Exception {
        //given
        ReponseService<List<Restaurant>> reponse = new ReponseService(CodeReponse.EMPTY, emptyList());
        doReturn(reponse).when(restaurantService).findByRechercheRestaurant(any(RechercheRestaurantDTO.class));
        //when + then
        mvc.perform(post("/rechercheRestaurants")
                        .param("nom", "test")
                        .param("ville", "test")
                        .param("codePostal", "45879")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.model().attributeExists("recherchevide"));
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenRechercheRestaurantError_alorsReponseException() throws Exception {
        //given
        ReponseService<List<Restaurant>> reponse = new ReponseService(CodeReponse.ERROR, emptyList(), new RuntimeException());
        doReturn(reponse).when(restaurantService).findByRechercheRestaurant(any(RechercheRestaurantDTO.class));
        //when + then
        Assertions.assertThrows(ServletException.class, () -> mvc.perform(post("/rechercheRestaurants")
                .param("nom", "test")
                .param("ville", "test")
                .param("codePostal", "45879")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)));
    }
}
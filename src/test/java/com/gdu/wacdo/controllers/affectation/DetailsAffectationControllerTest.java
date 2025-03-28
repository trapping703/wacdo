package com.gdu.wacdo.controllers.affectation;

import com.gdu.wacdo.controllers.affectation.DetailsAffectationController;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Affectation;
import com.gdu.wacdo.model.Employe;
import com.gdu.wacdo.model.Fonction;
import com.gdu.wacdo.model.Restaurant;
import com.gdu.wacdo.services.AffectationService;
import com.gdu.wacdo.status.CodeReponse;
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

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(DetailsAffectationController.class)
public class DetailsAffectationControllerTest {

    @Autowired
    private MockMvc mvc;
    @TestBean
    private ModelMapper modelMapper;
    @MockitoBean
    private AffectationService affectationService;

    static ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Test
    @WithMockUser(username = "admin", password = "pa$$", roles = {"USER", "ADMIN"})
    public void givenDetailsAffectation_alorsReponse200() throws Exception {
        //given
        ReponseService<Affectation> reponse = new ReponseService(CodeReponse.OK, new Affectation(LocalDate.now(), LocalDate.now(), new Employe(), new Restaurant(), new Fonction()));
        doReturn(reponse).when(affectationService).findById(1);
        //when + then
        mvc.perform(get("/detailAffectation/1")
                .contentType(MediaType.TEXT_HTML)).andExpect(MockMvcResultMatchers.status()
                .is2xxSuccessful());
    }

}
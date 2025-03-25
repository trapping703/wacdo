package com.gdu.wacdo.services;

import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Restaurant;
import com.gdu.wacdo.repositories.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class RestaurantServiceTest {

    @Autowired
    private ModelMapper modelMapper;
    @Mock
    private RestaurantRepository restaurantRepository;
    @InjectMocks
    private RestaurantService restaurantService;

    @Test
    void givenRestaurantSansID_whenSaveSucess_thenReturnRestaurantAvecIdEtReponseOK() {
        //given
        Restaurant restaurant = new Restaurant();
        int idAttendu = 1;
        Mockito.doAnswer(invocation -> {
            Restaurant restaurant1 = invocation.getArgument(0);
            restaurant1.setId(idAttendu);
            return restaurant1;
        }).when(restaurantRepository).save(restaurant);
        //when
        ReponseService<Restaurant> reponse = restaurantService.save(restaurant);
        //then
        assertThat(reponse.isOk()).isTrue();
        assertThat(reponse.getObjetRetour().getId()).isEqualTo(idAttendu);
        verify(restaurantRepository, Mockito.times(1)).save(restaurant);
        verifyNoMoreInteractions(restaurantRepository);
    }

    @Test
    void givenRestaurantSansID_whenSaveFail_thenReturnRestaurantSansIdEtReponsError() {
        //given
        Restaurant restaurant = new Restaurant();
        Mockito.doReturn(restaurant).when(restaurantRepository).save(restaurant);
        //when
        ReponseService<Restaurant> reponse = restaurantService.save(restaurant);
        //then
        assertThat(reponse.isError()).isTrue();
        assertThat(reponse.getObjetRetour().getId()).isNull();
        verify(restaurantRepository, Mockito.times(1)).save(restaurant);
        verifyNoMoreInteractions(restaurantRepository);
    }

    @Test
    void givenException_whenSave_thenReturnReponsErrorEtEx() {
        //given
        Restaurant restaurant = new Restaurant();
        Exception exception = new RuntimeException();
        Mockito.doThrow(exception).when(restaurantRepository).save(restaurant);
        //when
        ReponseService<Restaurant> reponse = restaurantService.save(restaurant);
        //then
        assertThat(reponse.isError()).isTrue();
        assertThat(reponse.getException()).isEqualTo(exception);
        verify(restaurantRepository, Mockito.times(1)).save(restaurant);
        verifyNoMoreInteractions(restaurantRepository);
    }

    @Test
    void GivenListRestaurant_whenFindAll_thenReturnListRestaurantEtReponseOK() {
        //given
        List<Restaurant> restaurants = new ArrayList<>();
        doReturn(restaurants).when(restaurantRepository).findAll();
        //when
        ReponseService<List<Restaurant>> reponse = restaurantService.findAll();
        //then
        assertThat(reponse.isOk()).isTrue();
        assertThat(reponse.getData()).isEqualTo(restaurants);
        verify(restaurantRepository, Mockito.times(1)).findAll();
        verifyNoMoreInteractions(restaurantRepository);
    }

    @Test
    void givenIdRestaurantExistant_whenFindById_thenReturnRestaurantEtReponseOK() {
        //given
        Restaurant restaurant = new Restaurant();
        doReturn(Optional.of(restaurant)).when(restaurantRepository).findById(1);
        //when
        ReponseService<Restaurant> reponse = restaurantService.findById(1);
        //then
        assertThat(reponse.isOk()).isTrue();
        assertThat(reponse.getData()).isEqualTo(restaurant);
        verify(restaurantRepository, Mockito.times(1)).findById(1);
        verifyNoMoreInteractions(restaurantRepository);
    }

    @Test
    void givenIdRestaurantNonExistant_whenFindById_thenReturnReponseEmpty() {
        //given
        doReturn(Optional.empty()).when(restaurantRepository).findById(1);
        //when
        ReponseService reponse = restaurantService.findById(1);
        //then
        assertThat(reponse.isEmpty()).isTrue();
        assertThat(reponse.getData()).isEqualTo(1);
        verify(restaurantRepository, Mockito.times(1)).findById(1);
        verifyNoMoreInteractions(restaurantRepository);
    }

    @Test
    void givenExcpetion_whenFindById_thenReturnReponseError() {
        //given
        Exception exception = new RuntimeException();
        Mockito.doThrow(exception).when(restaurantRepository).findById(1);
        //when
        ReponseService reponse = restaurantService.findById(1);
        //then
        assertThat(reponse.isError()).isTrue();
        assertThat(reponse.getException()).isEqualTo(exception);
        verify(restaurantRepository, Mockito.times(1)).findById(1);
        verifyNoMoreInteractions(restaurantRepository);
    }
}
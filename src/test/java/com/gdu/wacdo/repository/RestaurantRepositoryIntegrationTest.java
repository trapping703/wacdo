package com.gdu.wacdo.repository;

import com.gdu.wacdo.model.Restaurant;
import com.gdu.wacdo.repositories.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestEntityManager
public class RestaurantRepositoryIntegrationTest {

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Transactional
    @Rollback
    public void givenNewRestaurant_whenSave_thenCorrect() {
        //given
        Restaurant restaurant = new Restaurant();
        restaurant.setNom("testRestaurant");
        //then
        restaurantRepository.save(restaurant);
        //when
        assertThat(restaurant.getId()).isNotNull();
        assertThat(entityManager.find(Restaurant.class, restaurant.getId())).isEqualTo(restaurant);
    }
}
package com.mspp.restaurantservice.restaurant.repository;

import com.mspp.restaurantservice.restaurant.entity.Restaurant;
import com.mspp.restaurantservice.restaurant.specification.RestaurantSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Transactional
class RestaurantRepositoryIntegrationTest {

    @Container
    @ServiceConnection
    static MySQLContainer mysql = new MySQLContainer("mysql:8.0.40")
                    .withDatabaseName("restaurant_test_db")
                    .withUsername("test")
                    .withPassword("test");

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void shouldSaveAndFindRestaurantById() {

        Restaurant restaurant = new Restaurant();
        restaurant.setName("Spice Garden");
        restaurant.setDescription("Authentic Indian restaurant");
        restaurant.setCuisine("Indian");
        restaurant.setPhone("416-555-1234");
        restaurant.setEmail("spicegarden@example.com");
        restaurant.setOpeningTime(LocalTime.of(11, 0));
        restaurant.setClosingTime(LocalTime.of(22, 0));
        restaurant.setActive(true);

        Restaurant savedRestaurant =
                restaurantRepository.save(restaurant);

        Optional<Restaurant> result =
                restaurantRepository.findById(savedRestaurant.getId());

        assertTrue(result.isPresent());

        Restaurant foundRestaurant = result.get();

        assertEquals("Spice Garden", foundRestaurant.getName());
        assertEquals("Indian", foundRestaurant.getCuisine());
        assertNotNull(foundRestaurant.getCreatedAt());
        assertNotNull(foundRestaurant.getUpdatedAt());
    }

    @Test
    void shouldReturnTrueWhenRestaurantEmailExists() {

        Restaurant restaurant = createRestaurant(
                "Spice Garden",
                "Indian",
                "spicegarden@example.com",
                true
        );

        restaurantRepository.saveAndFlush(restaurant);

        boolean exists =
                restaurantRepository.existsByEmail("spicegarden@example.com");

        assertTrue(exists);
    }

    @Test
    void shouldFilterRestaurantsByCuisineActiveAndName() {

        Restaurant restaurant1 = createRestaurant(
                "Spice Garden",
                "Indian",
                "spice@example.com",
                true
        );

        Restaurant restaurant2 = createRestaurant(
                "Punjab Palace",
                "Indian",
                "punjab@example.com",
                false
        );

        Restaurant restaurant3 = createRestaurant(
                "Spice Italia",
                "Italian",
                "italia@example.com",
                true
        );

        restaurantRepository.saveAllAndFlush(
                List.of(restaurant1, restaurant2, restaurant3)
        );

        Specification<Restaurant> specification =
                Specification
                        .where(RestaurantSpecification.hasCuisine("Indian"))
                        .and(RestaurantSpecification.hasActive(true))
                        .and(RestaurantSpecification.hasName("Spice"));

        List<Restaurant> result =
                restaurantRepository.findAll(specification);

        assertEquals(1, result.size());

        Restaurant foundRestaurant = result.get(0);

        assertEquals("Spice Garden", foundRestaurant.getName());
        assertEquals("Indian", foundRestaurant.getCuisine());
        assertTrue(foundRestaurant.getActive());
    }

    @Test
    void shouldRejectDuplicateRestaurantEmail() {

        Restaurant restaurant1 = createRestaurant(
                "Spice Garden",
                "Indian",
                "duplicate@example.com",
                true
        );

        Restaurant restaurant2 = createRestaurant(
                "Another Restaurant",
                "Italian",
                "duplicate@example.com",
                true
        );

        restaurantRepository.saveAndFlush(restaurant1);
        assertThrows(
                DataIntegrityViolationException.class,
                () -> restaurantRepository.saveAndFlush(restaurant2)
        );
    }

    private Restaurant createRestaurant(String name, String cuisine, String email, boolean active) {

        Restaurant restaurant = new Restaurant();

        restaurant.setName(name);
        restaurant.setDescription("Test restaurant");
        restaurant.setCuisine(cuisine);
        restaurant.setPhone("416-555-1234");
        restaurant.setEmail(email);
        restaurant.setOpeningTime(LocalTime.of(11, 0));
        restaurant.setClosingTime(LocalTime.of(22, 0));
        restaurant.setActive(active);

        return restaurant;
    }
}

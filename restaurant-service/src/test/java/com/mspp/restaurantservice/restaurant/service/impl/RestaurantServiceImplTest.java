package com.mspp.restaurantservice.restaurant.service.impl;

import com.mspp.restaurantservice.exception.BusinessValidationException;
import com.mspp.restaurantservice.exception.DuplicateResourceException;
import com.mspp.restaurantservice.exception.ResourceNotFoundException;
import com.mspp.restaurantservice.restaurant.dto.request.RestaurantRequest;
import com.mspp.restaurantservice.restaurant.dto.request.RestaurantSearchRequest;
import com.mspp.restaurantservice.restaurant.dto.response.PageResponse;
import com.mspp.restaurantservice.restaurant.dto.response.RestaurantResponse;
import com.mspp.restaurantservice.restaurant.entity.Restaurant;
import com.mspp.restaurantservice.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    @Test
    void shouldCreateRestaurantSuccessfully() {

        RestaurantRequest request = new RestaurantRequest();
        request.setName("Spice Garden");
        request.setDescription("Authentic Indian restaurant");
        request.setCuisine("Indian");
        request.setPhone("416-555-1234");
        request.setEmail("spicegarden@example.com");
        request.setOpeningTime(LocalTime.of(11, 0));
        request.setClosingTime(LocalTime.of(22, 0));
        request.setActive(true);

        when(restaurantRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(restaurantRepository.save(any(Restaurant.class)))
                .thenAnswer(invocation -> {
                    Restaurant restaurant = invocation.getArgument(0);
                    restaurant.setId(1L);
                    return restaurant;
                });

        RestaurantResponse response = restaurantService.createRestaurant(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Spice Garden", response.getName());
        assertEquals("Indian", response.getCuisine());
        assertEquals("spicegarden@example.com", response.getEmail());
        assertTrue(response.getActive());
        verify(restaurantRepository).existsByEmail(request.getEmail());
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        RestaurantRequest request = new RestaurantRequest();
        request.setName("Spice Garden");
        request.setCuisine("Indian");
        request.setPhone("416-555-1234");
        request.setEmail("spicegarden@example.com");
        request.setOpeningTime(LocalTime.of(11, 0));
        request.setClosingTime(LocalTime.of(22, 0));
        request.setActive(true);

        when(restaurantRepository.existsByEmail(request.getEmail())).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class, () -> restaurantService.createRestaurant(request));

        assertEquals(
                "Restaurant with email '" + request.getEmail() + "' already exists",
                exception.getMessage()
        );
        verify(restaurantRepository).existsByEmail(request.getEmail());
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenOperatingHoursAreInvalid() {

        RestaurantRequest request = new RestaurantRequest();
        request.setName("Spice Garden");
        request.setCuisine("Indian");
        request.setPhone("416-555-1234");
        request.setEmail("spicegarden@example.com");
        request.setOpeningTime(LocalTime.of(22, 0));
        request.setClosingTime(LocalTime.of(11, 0));
        request.setActive(true);

        BusinessValidationException exception = assertThrows(
                BusinessValidationException.class,
                () -> restaurantService.createRestaurant(request)
        );

        assertEquals("Opening time must be before closing time", exception.getMessage());
        verifyNoInteractions(restaurantRepository);
    }

    @Test
    void shouldReturnRestaurantWhenIdExists() {

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Spice Garden");
        restaurant.setDescription("Authentic Indian restaurant");
        restaurant.setCuisine("Indian");
        restaurant.setPhone("416-555-1234");
        restaurant.setEmail("spicegarden@example.com");
        restaurant.setOpeningTime(LocalTime.of(11, 0));
        restaurant.setClosingTime(LocalTime.of(22, 0));
        restaurant.setActive(true);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        RestaurantResponse response = restaurantService.getRestaurantById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Spice Garden", response.getName());
        assertEquals("Indian", response.getCuisine());
        verify(restaurantRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenRestaurantIdDoesNotExist() {

        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> restaurantService.getRestaurantById(99L)
        );

        assertEquals("Restaurant not found with id: 99", exception.getMessage());

        verify(restaurantRepository).findById(99L);
    }

    @Test
    void shouldUpdateRestaurantSuccessfully() {

        Restaurant existingRestaurant = new Restaurant();
        existingRestaurant.setId(1L);
        existingRestaurant.setName("Old Name");
        existingRestaurant.setCuisine("Indian");
        existingRestaurant.setPhone("416-111-1111");
        existingRestaurant.setEmail("old@example.com");
        existingRestaurant.setOpeningTime(LocalTime.of(10, 0));
        existingRestaurant.setClosingTime(LocalTime.of(20, 0));
        existingRestaurant.setActive(true);

        RestaurantRequest request = new RestaurantRequest();
        request.setName("Spice Garden Updated");
        request.setDescription("Updated description");
        request.setCuisine("Punjabi");
        request.setPhone("416-555-9999");
        request.setEmail("updated@example.com");
        request.setOpeningTime(LocalTime.of(11, 0));
        request.setClosingTime(LocalTime.of(23, 0));
        request.setActive(true);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(existingRestaurant));

        when(restaurantRepository.save(any(Restaurant.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RestaurantResponse response = restaurantService.updateRestaurant(1L, request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Spice Garden Updated", response.getName());
        assertEquals("Punjabi", response.getCuisine());
        assertEquals("updated@example.com", response.getEmail());
        assertEquals(LocalTime.of(23, 0), response.getClosingTime());

        verify(restaurantRepository).findById(1L);
        verify(restaurantRepository).save(existingRestaurant);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingRestaurant() {

        RestaurantRequest request = new RestaurantRequest();
        request.setName("Spice Garden");
        request.setCuisine("Indian");
        request.setPhone("416-555-1234");
        request.setEmail("spicegarden@example.com");
        request.setOpeningTime(LocalTime.of(11, 0));
        request.setClosingTime(LocalTime.of(22, 0));
        request.setActive(true);

        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> restaurantService.updateRestaurant(99L, request)
        );

        assertEquals("Restaurant not found with id: 99", exception.getMessage()
        );

        verify(restaurantRepository).findById(99L);
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void shouldDeleteRestaurantSuccessfully() {

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        restaurantService.deleteRestaurant(1L);

        verify(restaurantRepository).findById(1L);
        verify(restaurantRepository).delete(restaurant);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingRestaurant() {

        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> restaurantService.deleteRestaurant(99L)
        );

        assertEquals("Restaurant not found with id: 99", exception.getMessage());

        verify(restaurantRepository).findById(99L);
        verify(restaurantRepository, never())
                .delete(any(Restaurant.class));
    }

    @Test
    void shouldReturnPaginatedRestaurants() {

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Spice Garden");
        restaurant.setCuisine("Indian");
        restaurant.setPhone("416-555-1234");
        restaurant.setEmail("spice@example.com");
        restaurant.setOpeningTime(LocalTime.of(11, 0));
        restaurant.setClosingTime(LocalTime.of(22, 0));
        restaurant.setActive(true);

        Page<Restaurant> restaurantPage = new PageImpl<>(
                        List.of(restaurant),
                        PageRequest.of(0, 10),
                        1);

        RestaurantSearchRequest searchRequest = new RestaurantSearchRequest();
        searchRequest.setCuisine("Indian");
        searchRequest.setActive(true);

        when(restaurantRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(restaurantPage);

        PageResponse<RestaurantResponse> response = restaurantService.getAllRestaurants(
                        0,
                        10,
                        "name",
                        "asc",
                        searchRequest
                );

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals("Spice Garden", response.getContent().get(0).getName());

        assertEquals(0, response.getPage());
        assertEquals(10, response.getSize());
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getTotalPages());

        verify(restaurantRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void shouldCreateCorrectPageableWithSorting() {

        Page<Restaurant> restaurantPage = new PageImpl<>(List.of());

        RestaurantSearchRequest searchRequest = new RestaurantSearchRequest();

        when(restaurantRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(restaurantPage);

        restaurantService.getAllRestaurants(2, 5, "name", "desc", searchRequest);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(restaurantRepository).findAll(any(Specification.class), pageableCaptor.capture());

        Pageable pageable = pageableCaptor.getValue();

        assertEquals(2, pageable.getPageNumber());
        assertEquals(5, pageable.getPageSize());

        Sort.Order order = pageable.getSort().getOrderFor("name");

        assertNotNull(order);
        assertEquals(Sort.Direction.DESC, order.getDirection());
    }

    @Test
    void shouldThrowExceptionWhenSortFieldIsInvalid() {

        RestaurantSearchRequest searchRequest = new RestaurantSearchRequest();

        BusinessValidationException exception = assertThrows(
                BusinessValidationException.class,
                () -> restaurantService.getAllRestaurants(
                        0,
                        10,
                        "namee",
                        "asc",
                        searchRequest
                )
        );

        assertEquals("Invalid sort field: namee", exception.getMessage());
        verifyNoInteractions(restaurantRepository);
    }

    @Test
    void shouldThrowExceptionWhenSortDirectionIsInvalid() {

        RestaurantSearchRequest searchRequest = new RestaurantSearchRequest();

        BusinessValidationException exception = assertThrows(
                BusinessValidationException.class,
                () -> restaurantService.getAllRestaurants(
                        0,
                        10,
                        "name",
                        "sideways",
                        searchRequest
                )
        );

        assertEquals("Sort direction must be either 'asc' or 'desc'", exception.getMessage());
        verifyNoInteractions(restaurantRepository);
    }
}
package com.mspp.restaurantservice.restaurant.controller;

import com.mspp.restaurantservice.exception.DuplicateResourceException;
import com.mspp.restaurantservice.exception.ResourceNotFoundException;
import com.mspp.restaurantservice.restaurant.dto.request.RestaurantSearchRequest;
import com.mspp.restaurantservice.restaurant.dto.response.PageResponse;
import com.mspp.restaurantservice.restaurant.service.RestaurantService;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.mspp.restaurantservice.restaurant.dto.request.RestaurantRequest;
import com.mspp.restaurantservice.restaurant.dto.response.RestaurantResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import java.time.LocalTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestaurantService restaurantService;

    @Test
    void shouldCreateRestaurantAndReturn201() throws Exception {

        RestaurantResponse response = RestaurantResponse.builder()
                .id(1L)
                .name("Spice Garden")
                .description("Authentic Indian restaurant")
                .cuisine("Indian")
                .phone("416-555-1234")
                .email("spicegarden@example.com")
                .openingTime(LocalTime.of(11, 0))
                .closingTime(LocalTime.of(22, 0))
                .active(true)
                .build();

        when(restaurantService.createRestaurant(any(RestaurantRequest.class)))
                .thenReturn(response);

        String requestBody = """
            {
              "name": "Spice Garden",
              "description": "Authentic Indian restaurant",
              "cuisine": "Indian",
              "phone": "416-555-1234",
              "email": "spicegarden@example.com",
              "openingTime": "11:00:00",
              "closingTime": "22:00:00",
              "active": true
            }
            """;

        mockMvc.perform(post("/api/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Spice Garden"))
                .andExpect(jsonPath("$.cuisine").value("Indian"))
                .andExpect(jsonPath("$.email")
                        .value("spicegarden@example.com"))
                .andExpect(jsonPath("$.active").value(true));

        verify(restaurantService)
                .createRestaurant(any(RestaurantRequest.class));
    }

    @Test
    void shouldReturn400WhenRequestIsInvalid() throws Exception{
        String request = """
            {
              "name": "",
              "description": "Invalid restaurant",
              "cuisine": "",
              "phone": "",
              "email": "invalid-email",
              "openingTime": null,
              "closingTime": null,
              "active": null
            }
            """;

        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.cuisine").exists())
                .andExpect(jsonPath("$.errors.phone").exists())
                .andExpect(jsonPath("$.errors.email").exists())
                .andExpect(jsonPath("$.errors.openingTime").exists())
                .andExpect(jsonPath("$.errors.closingTime").exists())
                .andExpect(jsonPath("$.errors.active").exists());

        verifyNoInteractions(restaurantService);
    }

    @Test
    void shouldReturnRestaurantWhenIdExists() throws Exception{
        RestaurantResponse response = RestaurantResponse.builder()
                .id(1L)
                .name("Spice Garden")
                .cuisine("Indian")
                .email("spicegarden@example.com")
                .active(true)
                .build();

        when(restaurantService.getRestaurantById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Spice Garden"))
                .andExpect(jsonPath("$.cuisine").value("Indian"))
                .andExpect(jsonPath("$.active").value(true));

        verify(restaurantService).getRestaurantById(1L);
    }

    @Test
    void shouldReturn404WhenRestaurantDoesNotExist() throws Exception {

        when(restaurantService.getRestaurantById(99L))
                .thenThrow(
                        new ResourceNotFoundException(
                                "Restaurant not found with id: 99"
                        )
                );

        mockMvc.perform(get("/api/v1/restaurants/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Restaurant not found with id: 99"));

        verify(restaurantService).getRestaurantById(99L);
    }

    @Test
    void shouldReturn409WhenRestaurantEmailAlreadyExists() throws Exception {

        when(restaurantService.createRestaurant(any(RestaurantRequest.class)))
                .thenThrow(
                        new DuplicateResourceException(
                                "Restaurant with email 'spicegarden@example.com' already exists"
                        )
                );

        String requestBody = """
            {
              "name": "Spice Garden",
              "description": "Authentic Indian restaurant",
              "cuisine": "Indian",
              "phone": "416-555-1234",
              "email": "spicegarden@example.com",
              "openingTime": "11:00:00",
              "closingTime": "22:00:00",
              "active": true
            }
            """;

        mockMvc.perform(post("/api/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message")
                        .value("Restaurant with email 'spicegarden@example.com' already exists"));

        verify(restaurantService)
                .createRestaurant(any(RestaurantRequest.class));
    }

    @Test
    void shouldUpdateRestaurantAndReturn200() throws Exception {

        RestaurantResponse response = RestaurantResponse.builder()
                .id(1L)
                .name("Spice Garden Updated")
                .description("Updated description")
                .cuisine("Punjabi")
                .phone("416-555-9999")
                .email("updated@example.com")
                .openingTime(LocalTime.of(11, 0))
                .closingTime(LocalTime.of(23, 0))
                .active(true)
                .build();

        when(restaurantService.updateRestaurant(
                eq(1L),
                any(RestaurantRequest.class)
        )).thenReturn(response);

        String requestBody = """
            {
              "name": "Spice Garden Updated",
              "description": "Updated description",
              "cuisine": "Punjabi",
              "phone": "416-555-9999",
              "email": "updated@example.com",
              "openingTime": "11:00:00",
              "closingTime": "23:00:00",
              "active": true
            }
            """;

        mockMvc.perform(put("/api/v1/restaurants/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Spice Garden Updated"))
                .andExpect(jsonPath("$.cuisine").value("Punjabi"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));

        verify(restaurantService).updateRestaurant(
                eq(1L),
                any(RestaurantRequest.class)
        );
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistingRestaurant() throws Exception {

        when(restaurantService.updateRestaurant(
                eq(99L),
                any(RestaurantRequest.class)
        )).thenThrow(
                new ResourceNotFoundException(
                        "Restaurant not found with id: 99"
                )
        );

        String requestBody = """
            {
              "name": "Spice Garden",
              "description": "Updated description",
              "cuisine": "Indian",
              "phone": "416-555-1234",
              "email": "spicegarden@example.com",
              "openingTime": "11:00:00",
              "closingTime": "22:00:00",
              "active": true
            }
            """;

        mockMvc.perform(put("/api/v1/restaurants/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Restaurant not found with id: 99"));

        verify(restaurantService).updateRestaurant(
                eq(99L),
                any(RestaurantRequest.class)
        );
    }

    @Test
    void shouldDeleteRestaurantAndReturn204() throws Exception {

        mockMvc.perform(delete("/api/v1/restaurants/1"))
                .andExpect(status().isNoContent());

        verify(restaurantService).deleteRestaurant(1L);
    }

    @Test
    void shouldReturn404WhenDeletingNonExistingRestaurant() throws Exception {

        doThrow(
                new ResourceNotFoundException(
                        "Restaurant not found with id: 99"
                )
        ).when(restaurantService)
                .deleteRestaurant(99L);

        mockMvc.perform(delete("/api/v1/restaurants/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Restaurant not found with id: 99"));

        verify(restaurantService).deleteRestaurant(99L);
    }

    @Test
    void shouldReturnPaginatedRestaurantsWithFilters() throws Exception {

        RestaurantResponse restaurant = RestaurantResponse.builder()
                .id(1L)
                .name("Spice Garden")
                .cuisine("Indian")
                .email("spicegarden@example.com")
                .active(true)
                .build();

        PageResponse<RestaurantResponse> pageResponse =
                PageResponse.<RestaurantResponse>builder()
                        .content(List.of(restaurant))
                        .page(0)
                        .size(10)
                        .totalElements(1)
                        .totalPages(1)
                        .first(true)
                        .last(true)
                        .build();

        when(restaurantService.getAllRestaurants(
                eq(0),
                eq(10),
                eq("name"),
                eq("asc"),
                any(RestaurantSearchRequest.class)
        )).thenReturn(pageResponse);

        mockMvc.perform(get("/api/v1/restaurants")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "name")
                        .param("direction", "asc")
                        .param("cuisine", "Indian")
                        .param("active", "true")
                        .param("name", "Spice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Spice Garden"))
                .andExpect(jsonPath("$.content[0].cuisine").value("Indian"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));

        ArgumentCaptor<RestaurantSearchRequest> captor =
                ArgumentCaptor.forClass(RestaurantSearchRequest.class);

        verify(restaurantService).getAllRestaurants(
                eq(0),
                eq(10),
                eq("name"),
                eq("asc"),
                captor.capture()
        );

        RestaurantSearchRequest searchRequest = captor.getValue();

        assertEquals("Indian", searchRequest.getCuisine());
        assertEquals(true, searchRequest.getActive());
        assertEquals("Spice", searchRequest.getName());
    }

    @Test
    void shouldReturn400WhenPageSizeIsInvalid() throws Exception {

        mockMvc.perform(get("/api/v1/restaurants")
                        .param("page", "0")
                        .param("size", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verifyNoInteractions(restaurantService);
    }

    @Test
    void shouldReturn400WhenPageNumberIsNegative() throws Exception {

        mockMvc.perform(get("/api/v1/restaurants")
                        .param("page", "-1")
                        .param("size", "10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verifyNoInteractions(restaurantService);
    }

    @Test
    void shouldReturn400WhenSearchNameIsTooLong() throws Exception {

        String longName = "a".repeat(101);

        mockMvc.perform(get("/api/v1/restaurants")
                        .param("name", longName))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.name")
                        .value("Name must not exceed 100 characters"));

        verifyNoInteractions(restaurantService);
    }
}
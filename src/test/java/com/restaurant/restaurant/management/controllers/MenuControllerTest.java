package com.restaurant.restaurant.management.controllers;

import com.restaurant.restaurant.management.dto.DishResponseDto;
import com.restaurant.restaurant.management.dto.MenuResponseDto;
import com.restaurant.restaurant.management.models.Dish;
import com.restaurant.restaurant.management.models.Menu;
import com.restaurant.restaurant.management.services.MenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MenuControllerTest {

    private WebTestClient webTestClient;
    private MenuService menuService;
    private Menu menu;
    private MenuResponseDto menuResponseDto;
    private Dish dish;
    private DishResponseDto dishResponseDto;

    @BeforeEach
    void setup() {
        menuService = mock(MenuService.class);
        webTestClient = WebTestClient.bindToController(new MenuController(menuService)).build();

        menu = new Menu();
        menu.setIdMenu(1L);
        menu.setMenuName("menu del viernes");

        menuResponseDto = new MenuResponseDto();
        menuResponseDto.setIdMenu(1L);
        menuResponseDto.setMenuName("menu del viernes");

        dish = new Dish();
        dish.setIdDish(1L);
        dish.setDishName("Panzerotti");
        dish.setPrice(12.99);
        dish.setDescription("Delicioso panzerotti");
        dish.setPopular(false);

        dishResponseDto = new DishResponseDto();
        dishResponseDto.setIdDish(1L);
        dishResponseDto.setDishName("Panzerotti");
        dishResponseDto.setPrice(12.99);
        dishResponseDto.setDescription("Delicioso panzerotti");
        dishResponseDto.setIsPopular(false);
    }

    @Test
    @DisplayName("Agregar menu")
    void addMenu() {
        when(menuService.addMenu(any(Menu.class))).thenReturn(menu);

        webTestClient.post()
                .uri("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(menuResponseDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MenuResponseDto.class)
                .value(dto -> {
                    assertEquals(menuResponseDto.getIdMenu(), dto.getIdMenu());
                    assertEquals(menuResponseDto.getMenuName(), dto.getMenuName());
                });

        verify(menuService).addMenu(any(Menu.class));
    }

    @Test
    @DisplayName("Obtener menu por id")
    void getMenuById() {
        when(menuService.getMenu(anyLong())).thenReturn(Optional.of(menu));

        webTestClient.get()
                .uri("/api/menus/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(MenuResponseDto.class)
                .value(dto -> {
                    assertEquals(menuResponseDto.getIdMenu(), dto.getIdMenu());
                    assertEquals(menuResponseDto.getMenuName(), dto.getMenuName());
                });

        verify(menuService).getMenu(anyLong());
    }

    @Test
    @DisplayName("Obtener todos los menus")
    void getAllMenus() {
        when(menuService.getAllMenus()).thenReturn(List.of(menu));

        webTestClient.get()
                .uri("/api/menus")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(MenuResponseDto.class)
                .hasSize(1)
                .value(list -> {
                    assertEquals(menuResponseDto.getIdMenu(), list.get(0).getIdMenu());
                    assertEquals(menuResponseDto.getMenuName(), list.get(0).getMenuName());
                });

        verify(menuService).getAllMenus();
    }

    @Test
    @DisplayName("Actualizar menu")
    void updateMenu() {
        when(menuService.updateMenu(anyLong(), any(Menu.class))).thenReturn(menu);

        webTestClient.put()
                .uri("/api/menus/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(menuResponseDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(MenuResponseDto.class)
                .value(dto -> {
                    assertEquals(menuResponseDto.getIdMenu(), dto.getIdMenu());
                    assertEquals(menuResponseDto.getMenuName(), dto.getMenuName());
                });

        verify(menuService).updateMenu(anyLong(), any(Menu.class));
    }

    @Test
    @DisplayName("Eliminar menu")
    void deleteMenu() {
        doNothing().when(menuService).deleteMenu(anyLong());

        webTestClient.delete()
                .uri("/api/menus/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();

        verify(menuService).deleteMenu(anyLong());
    }

    @Test
    @DisplayName("Agregar un plato a un menu")
    void addDishToMenu() {
        when(menuService.addDishToMenu(anyLong(), any(Dish.class))).thenReturn(dish);

        webTestClient.post()
                .uri("/api/menus/{idMenu}/dishes", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dishResponseDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DishResponseDto.class)
                .value(dto -> {
                    assertEquals(dishResponseDto.getIdDish(), dto.getIdDish());
                    assertEquals(dishResponseDto.getDishName(), dto.getDishName());
                    assertEquals(dishResponseDto.getPrice(), dto.getPrice());
                });

        verify(menuService).addDishToMenu(anyLong(), any(Dish.class));
    }

    @Test
    @DisplayName("Obtener todos los platos de un menu")
    void getAllDishesFromMenu() {
        when(menuService.getAllDishesFromMenu(anyLong())).thenReturn(List.of(dish));

        webTestClient.get()
                .uri("/api/menus/{idMenu}/dishes", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(DishResponseDto.class)
                .hasSize(1)
                .value(list -> {
                    assertEquals(dishResponseDto.getIdDish(), list.get(0).getIdDish());
                    assertEquals(dishResponseDto.getDishName(), list.get(0).getDishName());
                });

        verify(menuService).getAllDishesFromMenu(anyLong());
    }

    @Test
    @DisplayName("Obtener un plato de un menu por id")
    void getDishFromMenu() {
        when(menuService.getDishFromMenu(anyLong(), anyLong())).thenReturn(Optional.of(dish));

        webTestClient.get()
                .uri("/api/menus/{idMenu}/dishes/{idDish}", 1L, 1L)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DishResponseDto.class)
                .value(dto -> {
                    assertEquals(dishResponseDto.getIdDish(), dto.getIdDish());
                    assertEquals(dishResponseDto.getDishName(), dto.getDishName());
                });

        verify(menuService).getDishFromMenu(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Eliminar un plato de un menu")
    void removeDishFromMenu() {
        doNothing().when(menuService).removeDishFromMenu(anyLong(), anyLong());

        webTestClient.delete()
                .uri("/api/menus/{idMenu}/dishes/{idDish}", 1L, 1L)
                .exchange()
                .expectStatus().isNoContent();

        verify(menuService).removeDishFromMenu(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Actualizar un plato en un menu")
    void updateDishInMenu() {
        when(menuService.updateDishInMenu(anyLong(), anyLong(), any(Dish.class))).thenReturn(dish);

        webTestClient.put()
                .uri("/api/menus/{idMenu}/dishes/{idDish}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dishResponseDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Dish.class)
                .value(updatedDish -> {
                    assertEquals(dish.getIdDish(), updatedDish.getIdDish());
                    assertEquals(dish.getDishName(), updatedDish.getDishName());
                });

        verify(menuService).updateDishInMenu(anyLong(), anyLong(), any(Dish.class));
    }
}

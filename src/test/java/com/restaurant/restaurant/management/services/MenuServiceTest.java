package com.restaurant.restaurant.management.services;

import com.restaurant.restaurant.management.models.Dish;
import com.restaurant.restaurant.management.models.Menu;
import com.restaurant.restaurant.management.repositories.DishRepository;
import com.restaurant.restaurant.management.repositories.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class MenuServiceTest {

    private MenuRepository menuRepository;
    private DishRepository dishRepository;
    private MenuService menuService;

    private Menu menu;
    private Dish dish;

    @BeforeEach
    void setUp() {
        menuRepository = mock(MenuRepository.class);
        dishRepository = mock(DishRepository.class);
        menuService = new MenuService(menuRepository, dishRepository);

        menu = new Menu();
        menu.setIdMenu(1L);
        menu.setMenuName("Menu del viernes");

        dish = new Dish();
        dish.setIdDish(1L);
        dish.setDishName("Panzerotti");
        dish.setPrice(12.99);
        dish.setDescription("Delicioso panzerotti");
        dish.setPopular(false);
    }

    @Test
    @DisplayName("Agregar un menu")
    void addMenu() {
        when(menuRepository.save(any(Menu.class))).thenReturn(menu);

        Menu result = menuService.addMenu(menu);

        assertNotNull(result);
        assertEquals(menu.getIdMenu(), result.getIdMenu());
        assertEquals(menu.getMenuName(), result.getMenuName());
        verify(menuRepository).save(any(Menu.class));
    }

    @Test
    @DisplayName("Obtener todos los menus")
    void getAllMenus() {
        when(menuRepository.findAll()).thenReturn(List.of(menu));

        List<Menu> result = menuService.getAllMenus();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(menu.getIdMenu(), result.get(0).getIdMenu());
        verify(menuRepository).findAll();
    }

    @Test
    @DisplayName("Obtener un menu por id")
    void getMenu() {
        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(menu));

        Optional<Menu> result = menuService.getMenu(1L);

        assertTrue(result.isPresent());
        assertEquals(menu.getIdMenu(), result.get().getIdMenu());
        verify(menuRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Actualizar un menu")
    void updateMenu() {
        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(menu));
        when(menuRepository.save(any(Menu.class))).thenReturn(menu);

        Menu updatedMenu = new Menu();
        updatedMenu.setMenuName("Menu del sabado");

        Menu result = menuService.updateMenu(1L, updatedMenu);

        assertNotNull(result);
        assertEquals("Menu del sabado", result.getMenuName());
        verify(menuRepository).findById(anyLong());
        verify(menuRepository).save(any(Menu.class));
    }

    @Test
    @DisplayName("Eliminar un menu")
    void deleteMenu() {
        doNothing().when(menuRepository).deleteById(anyLong());

        menuService.deleteMenu(1L);

        verify(menuRepository).deleteById(anyLong());
    }

    @Test
    @DisplayName("Agregar plato a un menu")
    void addDishToMenu() {
        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(menu));
        when(dishRepository.save(any(Dish.class))).thenReturn(dish);
        when(menuRepository.save(any(Menu.class))).thenReturn(menu);

        Dish result = menuService.addDishToMenu(1L, dish);

        assertNotNull(result);
        assertEquals(dish.getDishName(), result.getDishName());
        verify(menuRepository).findById(anyLong());
        verify(dishRepository).save(any(Dish.class));
        verify(menuRepository).save(any(Menu.class));
    }

    @Test
    @DisplayName("Obtener todos los platos de un menu")
    void getAllDishesFromMenu() {
        menu.setDishes(List.of(dish));
        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(menu));

        List<Dish> result = menuService.getAllDishesFromMenu(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dish.getDishName(), result.get(0).getDishName());
        verify(menuRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Obtener un plato de un menu por id")
    void getDishFromMenu() {
        menu.setDishes(List.of(dish));
        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(menu));

        Optional<Dish> result = menuService.getDishFromMenu(1L, 1L);

        assertTrue(result.isPresent());
        assertEquals(dish.getDishName(), result.get().getDishName());
        verify(menuRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Eliminar un plato de un menu")
    void removeDishFromMenu() {
        menu.setDishes(new ArrayList<>(List.of(dish))); // Usar lista mutable
        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(menu));
        doNothing().when(dishRepository).delete(any(Dish.class));

        menuService.removeDishFromMenu(1L, 1L);

        verify(menuRepository).findById(anyLong());
        verify(dishRepository).delete(any(Dish.class));
    }


    @Test
    @DisplayName("Actualizar un plato en un menu")
    void updateDishInMenu() {
        menu.setDishes(List.of(dish));
        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(menu));

        Dish updatedDish = new Dish();
        updatedDish.setDishName("Arroz chino");
        updatedDish.setPrice(15.0);

        Dish result = menuService.updateDishInMenu(1L, 1L, updatedDish);

        assertNotNull(result);
        assertEquals("Arroz chino", result.getDishName());
        assertEquals(15.0, result.getPrice());
        verify(menuRepository).findById(anyLong());
    }
}

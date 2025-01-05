package com.restaurant.restaurant.management.controllers;

import com.restaurant.restaurant.management.dto.MenuResponseDto;
import com.restaurant.restaurant.management.dtoConverter.MenuMapper;
import com.restaurant.restaurant.management.models.Menu;
import com.restaurant.restaurant.management.models.Dish;
import com.restaurant.restaurant.management.services.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<MenuResponseDto> addMenu(@RequestBody Menu menu) {
        Menu newMenu = menuService.addMenu(menu);
        return ResponseEntity.ok(MenuMapper.toDto(newMenu));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuResponseDto> getMenu(@PathVariable Long id) {
        return menuService.getMenu(id)
                .map(menu -> ResponseEntity.ok(MenuMapper.toDto(menu)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<MenuResponseDto>> getAllMenus() {
        List<Menu> menus = menuService.getAllMenus();
        List<MenuResponseDto> response = menus.stream()
                .map(MenuMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuResponseDto> updateMenu(@PathVariable Long id, @RequestBody Menu menuUpdated) {
        try {
            Menu updatedMenu = menuService.updateMenu(id, menuUpdated);
            return ResponseEntity.ok(MenuMapper.toDto(updatedMenu));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idMenu}/dishes")
    public ResponseEntity<Dish> addDishToMenu(@PathVariable Long idMenu, @RequestBody Dish dish) {
        Dish addedDish = menuService.addDishToMenu(idMenu, dish);
        if (addedDish != null) {
            return ResponseEntity.ok(addedDish);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{idMenu}/dishes/{idDish}")
    public ResponseEntity<Void> removeDishFromMenu(@PathVariable Long idMenu, @PathVariable Long idDish) {
        menuService.removeDishFromMenu(idMenu, idDish);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idMenu}/dishes/{idDish}")
    public ResponseEntity<Dish> updateDishInMenu(@PathVariable Long idMenu, @PathVariable Long idDish, @RequestBody Dish dishUpdated) {
        Dish updatedDish = menuService.updateDishInMenu(idMenu, idDish, dishUpdated);
        if (updatedDish != null) {
            return ResponseEntity.ok(updatedDish);
        }
        return ResponseEntity.notFound().build();
    }
}


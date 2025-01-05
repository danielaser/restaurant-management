package com.restaurant.restaurant.management.dtoConverter;

import com.restaurant.restaurant.management.dto.MenuResponseDto;
import com.restaurant.restaurant.management.models.Menu;

import java.util.stream.Collectors;

public class MenuMapper {
    public static MenuResponseDto toDto(Menu menu) {
        MenuResponseDto dto = new MenuResponseDto();
        dto.setIdMenu(menu.getIdMenu());
        dto.setMenuName(menu.getMenuName());
        dto.setDishes(menu.getDishes().stream()
                .map(DishMapper::toDto)
                .collect(Collectors.toList()));
        return dto;
    }

    public static Menu toEntity(MenuResponseDto dto) {
        Menu menu = new Menu();
        menu.setIdMenu(dto.getIdMenu());
        menu.setMenuName(dto.getMenuName());
        menu.setDishes(dto.getDishes().stream()
                .map(DishMapper::toEntity)
                .collect(Collectors.toList()));
        return menu;
    }
}

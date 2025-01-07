package com.restaurant.restaurant.management.dtoConverter;

import com.restaurant.restaurant.management.dto.DishResponseDto;
import com.restaurant.restaurant.management.models.Dish;

public class DishMapper {
    public static DishResponseDto toDto(Dish dish) {
        DishResponseDto dto = new DishResponseDto();
        dto.setIdDish(dish.getIdDish());
        dto.setDishName(dish.getDishName());
        dto.setPrice(dish.getPrice());
        dto.setDescription(dish.getDescription());
        dto.setIsPopular(dish.isPopular());
        return dto;
    }

    public static Dish toEntity(DishResponseDto dto) {
        Dish dish = new Dish();
        dish.setIdDish(dto.getIdDish());
        dish.setDishName(dto.getDishName());
        dish.setPrice(dto.getPrice());
        dish.setDescription(dto.getDescription());
        return dish;
    }
}

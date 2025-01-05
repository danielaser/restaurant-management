package com.restaurant.restaurant.management.services;

import com.restaurant.restaurant.management.repositories.ClientRepository;
import com.restaurant.restaurant.management.repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

//    public MenuDto createMenu(MenuDto menuDto) {
//        Menu menu = MenuMapper.toEntity(menuDto);
//        return MenuMapper.toDto(menuRepository.save(menu));
//    }
//
//    public DishDto addDishToMenu(Long menuId, DishDto dishDto) {
//        Menu menu = menuRepository.findById(menuId)
//                .orElseThrow(() -> new RuntimeException("Menu not found"));
//        Dish dish = DishMapper.toEntity(dishDto);
//        dish.setMenu(menu);
//        dishRepository.save(dish);
//        return DishMapper.toDto(dish);
//    }

}

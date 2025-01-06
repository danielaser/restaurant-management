package com.restaurant.restaurant.management.services;

import com.restaurant.restaurant.management.models.Dish;
import com.restaurant.restaurant.management.models.Menu;
import com.restaurant.restaurant.management.repositories.ClientRepository;
import com.restaurant.restaurant.management.repositories.DishRepository;
import com.restaurant.restaurant.management.repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository, DishRepository dishRepository) {
        this.menuRepository = menuRepository;
        this.dishRepository = dishRepository;
    }

    public Menu addMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    public Optional<Menu> getMenu(Long id) {
        return menuRepository.findById(id);
    }

    public Menu updateMenu(Long id, Menu menuUpdated) {
        return menuRepository.findById(id).map(existingMenu -> {
            existingMenu.setMenuName(menuUpdated.getMenuName());
            existingMenu.setDishes(menuUpdated.getDishes());
            return menuRepository.save(existingMenu);
        }).orElseThrow(() -> new RuntimeException("El menu con id: " + id + " no pudo ser actualizado"));
    }

    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }

    public Dish addDishToMenu(Long idMenu, Dish dish) {
        Optional<Menu> menuOpt = menuRepository.findById(idMenu);
        if (menuOpt.isPresent()) {
            Menu menu = menuOpt.get();
            dish.setMenu(menu);
            Dish savedDish = dishRepository.save(dish);
            menu.getDishes().add(savedDish);
            menuRepository.save(menu);
            return savedDish;
        }
        return null;
    }

    public List<Dish> getAllDishesFromMenu(Long idMenu) {
        Optional<Menu> menuOpt = menuRepository.findById(idMenu);
        return menuOpt.map(Menu::getDishes).orElse(List.of());
    }

    public Optional<Dish> getDishFromMenu(Long idMenu, Long idDish) {
        Optional<Menu> menuOpt = menuRepository.findById(idMenu);
        if (menuOpt.isPresent()) {
            Menu menu = menuOpt.get();
            return menu.getDishes().stream()
                    .filter(dish -> dish.getIdDish().equals(idDish))
                    .findFirst();
        }
        return Optional.empty();
    }

    public void removeDishFromMenu(Long idMenu, Long idDish) {
        Optional<Menu> menuOpt = menuRepository.findById(idMenu);
        if (menuOpt.isPresent()) {
            Menu menu = menuOpt.get();
            menu.getDishes().removeIf(dish -> dish.getIdDish().equals(idDish));
            menuRepository.save(menu);
        }
    }

    public Dish updateDishInMenu(Long idMenu, Long idDish, Dish dishUpdated) {
        Optional<Menu> menuOpt = menuRepository.findById(idMenu);
        if (menuOpt.isPresent()) {
            Menu menu = menuOpt.get();
            Optional<Dish> dishOpt = menu.getDishes().stream()
                    .filter(d -> d.getIdDish().equals(idDish))
                    .findFirst();

            if (dishOpt.isPresent()) {
                Dish dish = dishOpt.get();
                dish.setDishName(dishUpdated.getDishName());
                dish.setPrice(dishUpdated.getPrice());
                dish.setDescription(dishUpdated.getDescription());
                menuRepository.save(menu);
                return dish;
            }
        }
        return null;
    }

}

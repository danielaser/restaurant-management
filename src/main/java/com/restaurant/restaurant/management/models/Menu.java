package com.restaurant.restaurant.management.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMenu;
    private String menuName;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<Dish> dishes;

    public Menu(Long idMenu, String menuName) {
        this.idMenu = idMenu;
        this.menuName = menuName;
    }

    public Menu() {
    }
}

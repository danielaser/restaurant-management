package com.restaurant.restaurant.management.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDish;
    private String dishName;
    private Double price;
    private String description;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    @JsonBackReference
    private Menu menu;

    public Dish(Long idDish, String dishName, Double price, String description, Menu menu) {
        this.idDish = idDish;
        this.dishName = dishName;
        this.price = price;
        this.description = description;
        this.menu = menu;
    }

    public Dish() {
    }
}

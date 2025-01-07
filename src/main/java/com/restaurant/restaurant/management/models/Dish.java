package com.restaurant.restaurant.management.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.restaurant.restaurant.management.observer.DishObserver;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    private boolean isPopular;


    @Transient
    private List<DishObserver> observers = new ArrayList<>();

    public Dish(Long idDish, String dishName, Double price, String description, Menu menu) {
        this.idDish = idDish;
        this.dishName = dishName;
        this.price = price;
        this.description = description;
        this.menu = menu;
    }

    public Dish() {
    }

    public void addObserver(DishObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(DishObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (DishObserver observer : observers) {
            observer.update(this);
        }
    }

    public void checkPopularity(int timesOrdered) {
        if (timesOrdered > 100 && !isPopular) {
            this.isPopular = true;
            this.price += this.price * 0.0573;
            notifyObservers();
        }
    }
}

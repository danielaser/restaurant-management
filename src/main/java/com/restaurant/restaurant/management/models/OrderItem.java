package com.restaurant.restaurant.management.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrderItem;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    public OrderItem(Long idOrderItem, Dish dish, Integer quantity) {
        this.idOrderItem = idOrderItem;
        this.dish = dish;
        this.quantity = quantity;
    }

    public OrderItem() {
    }
}

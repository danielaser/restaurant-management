package com.restaurant.restaurant.management.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private OrderRestaurant order;

    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    private Integer quantity;

    private Long idDish;

    public OrderItem(Long idOrderItem, OrderRestaurant order, Dish dish, Integer quantity) {
        this.idOrderItem = idOrderItem;
        this.order = order;
        this.dish = dish;
        this.quantity = quantity;
    }

    public OrderItem() {
    }
}

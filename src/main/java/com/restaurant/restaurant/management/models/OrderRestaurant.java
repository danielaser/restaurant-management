package com.restaurant.restaurant.management.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class OrderRestaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrder;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    private Double totalAmount;

    public OrderRestaurant(Long idOrder, Client client, List<OrderItem> orderItems, Double totalAmount) {
        this.idOrder = idOrder;
        this.client = client;
        this.orderItems = orderItems;
        this.totalAmount = totalAmount;
    }

    public OrderRestaurant() {
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);  // Establece la relación bidireccional
    }


//    public void removeOrderItem(OrderItem orderItem) {
//        orderItems.remove(orderItem);
//        orderItem.setOrderRestaurant(null);
//    }
}

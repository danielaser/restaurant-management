package com.restaurant.restaurant.management.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrder;
    private Double totalAmount;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    public Order(Long idOrder, Client client, List<OrderItem> orderItems, Double totalAmount) {
        this.idOrder = idOrder;
        this.client = client;
        this.orderItems = orderItems;
        this.totalAmount = totalAmount;
    }

    public Order() {
    }
}

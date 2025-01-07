package com.restaurant.restaurant.management.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private Double totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public OrderRestaurant(Long idOrder,  Double totalAmount, List<OrderItem> orderItems, Client client) {
        this.idOrder = idOrder;
        this.totalAmount = totalAmount;
        this.orderItems = orderItems;
        this.client = client;
    }

    public OrderRestaurant() {
    }

}

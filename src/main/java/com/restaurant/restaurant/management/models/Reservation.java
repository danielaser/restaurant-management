package com.restaurant.restaurant.management.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;
    private LocalDate dateTime;
    private Integer numberOfPeople;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private Double price;

    public Reservation(Long idReservation, LocalDate dateTime, Integer numberOfPeople, Client client, Double price) {
        this.idReservation = idReservation;
        this.dateTime = dateTime;
        this.numberOfPeople = numberOfPeople;
        this.client = client;
        this.price = price;
    }

    public Reservation() {
    }
}

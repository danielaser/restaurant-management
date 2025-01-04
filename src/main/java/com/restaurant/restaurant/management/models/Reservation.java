package com.restaurant.restaurant.management.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;
    private LocalDateTime dateTime;
    private Integer numberOfPeople;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;


    public Reservation(Long idReservation, LocalDateTime dateTime, Integer numberOfPeople, Client client) {
        this.idReservation = idReservation;
        this.dateTime = dateTime;
        this.numberOfPeople = numberOfPeople;
        this.client = client;
    }

    public Reservation() {
    }
}

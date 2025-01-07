package com.restaurant.restaurant.management.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClient;
    private String clientName;
    private String email;
    private String phoneNumber;
    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate registrationDate;
    private boolean frequentUser;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<OrderRestaurant> orders;

    private boolean isVIP;

    public Client(Long idClient, String clientName, String email, String phoneNumber, String address, LocalDate registrationDate, boolean isVIP) {
        this.idClient = idClient;
        this.clientName = clientName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.registrationDate = registrationDate;
        this.isVIP = isVIP;
    }

    public Client() {
    }
}

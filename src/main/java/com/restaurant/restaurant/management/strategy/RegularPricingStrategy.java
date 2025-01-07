package com.restaurant.restaurant.management.strategy;

import com.restaurant.restaurant.management.models.Reservation;

public class RegularPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(Reservation reservation) {
        double pricePerPerson = 20.0;
        return pricePerPerson * reservation.getNumberOfPeople();
    }
}

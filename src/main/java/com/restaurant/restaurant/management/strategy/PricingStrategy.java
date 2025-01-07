package com.restaurant.restaurant.management.strategy;

import com.restaurant.restaurant.management.models.Reservation;

public interface PricingStrategy {
    double calculatePrice(Reservation reservation);
}
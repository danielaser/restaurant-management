package com.restaurant.restaurant.management.observer;

import com.restaurant.restaurant.management.models.Dish;

public interface DishObserver {
    void update(Dish dish);
}

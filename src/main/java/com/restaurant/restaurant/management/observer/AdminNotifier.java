package com.restaurant.restaurant.management.observer;

import com.restaurant.restaurant.management.models.Dish;

public class AdminNotifier implements DishObserver {
    @Override
    public void update(Dish dish) {
        if (dish.isPopular()) {
            System.out.println("El plato " + dish.getDishName() + " es ahora popular, con un precio ajustado.");
        }
    }
}

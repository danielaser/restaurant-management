package com.restaurant.restaurant.management.chain;

import com.restaurant.restaurant.management.models.Dish;

public class IncreasePriceHandler extends DishHandler {
    @Override
    public void handle(Dish dish, int timesOrdered) {
        if (dish.isPopular() && dish.getPrice() > 0) {
            dish.setPrice(dish.getPrice() * 1.0573);
            System.out.println("El precio del plato " + dish.getDishName() + " ha aumentado en un 5.73%.");
        }

        if (nextHandler != null) {
            nextHandler.handle(dish, timesOrdered);
        }
    }
}

package com.restaurant.restaurant.management.chain;

import com.restaurant.restaurant.management.models.Dish;

public class DiscountHandler extends DishHandler {
    @Override
    public void handle(Dish dish, int timesOrdered) {
        if (dish.getPrice() > 20) {
            dish.setPrice(dish.getPrice() * 0.9);
            System.out.println("Se ha aplicado un descuento al plato.");
        }
        if (nextHandler != null) nextHandler.handle(dish, timesOrdered);
    }
}

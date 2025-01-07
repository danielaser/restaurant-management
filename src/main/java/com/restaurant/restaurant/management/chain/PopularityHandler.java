package com.restaurant.restaurant.management.chain;

import com.restaurant.restaurant.management.models.Dish;

public class PopularityHandler extends DishHandler {
    @Override
    public void handle(Dish dish, int timesOrdered) {
        if (timesOrdered > 100 && !dish.isPopular()) {
            dish.setPopular(true);
            dish.setPrice(dish.getPrice() * 1.0573);
        }
        if (nextHandler != null) nextHandler.handle(dish, timesOrdered);
    }
}


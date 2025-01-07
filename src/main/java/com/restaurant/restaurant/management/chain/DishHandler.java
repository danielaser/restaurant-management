package com.restaurant.restaurant.management.chain;

import com.restaurant.restaurant.management.models.Dish;

public abstract class DishHandler {
    protected DishHandler nextHandler;

    public void setNextHandler(DishHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract void handle(Dish dish, int timesOrdered);
}



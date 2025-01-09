package com.restaurant.restaurant.management.repositories;

import com.restaurant.restaurant.management.models.OrderRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderRestaurant, Long> {
    @Query("SELECT COUNT(o) FROM OrderRestaurant o JOIN o.dishes d WHERE d.idDish = :dishId")
    long countOrdersByDishId(@Param("dishId") Long dishId);
}

package com.restaurant.restaurant.management.repositories;

import com.restaurant.restaurant.management.models.OrderRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderRestaurant, Long> {

}

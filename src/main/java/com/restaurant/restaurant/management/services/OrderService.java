package com.restaurant.restaurant.management.services;

import com.restaurant.restaurant.management.repositories.MenuRepository;
import com.restaurant.restaurant.management.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

//    public OrderDto createOrder(OrderDto orderDto) {
//        Order order = OrderMapper.toEntity(orderDto);
//        order.setTotalAmount(order.getItems().stream()
//                .mapToDouble(item -> item.getPrice() * item.getQuantity())
//                .sum());
//        return OrderMapper.toDto(orderRepository.save(order));
//    }
}

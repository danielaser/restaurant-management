package com.restaurant.restaurant.management.controllers;

import com.restaurant.restaurant.management.dto.OrderItemResponseDto;
import com.restaurant.restaurant.management.dto.OrderResponseDto;
import com.restaurant.restaurant.management.dtoConverter.OrderItemMapper;
import com.restaurant.restaurant.management.dtoConverter.OrderMapper;
import com.restaurant.restaurant.management.models.Dish;
import com.restaurant.restaurant.management.models.OrderRestaurant;
import com.restaurant.restaurant.management.models.OrderItem;
import com.restaurant.restaurant.management.services.MenuService;
import com.restaurant.restaurant.management.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final MenuService menuService;

    @Autowired
    public OrderController(OrderService orderService, MenuService menuService) {
        this.orderService = orderService;
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> addOrder(@RequestBody OrderResponseDto orderDto) {
        OrderRestaurant orderRestaurant = OrderMapper.toEntity(orderDto);
        OrderRestaurant newOrder = orderService.addOrder(orderRestaurant);
        return ResponseEntity.ok(OrderMapper.toDto(newOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long id) {
        return orderService.getOrder(id)
                .map(order -> ResponseEntity.ok(OrderMapper.toDto(order)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        List<OrderRestaurant> orders = orderService.getAllOrders();
        List<OrderResponseDto> response = orders.stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable Long id, @RequestBody OrderResponseDto orderDto) {
        OrderRestaurant orderUpdated = OrderMapper.toEntity(orderDto);
        OrderRestaurant updatedOrder = orderService.updateOrder(id, orderUpdated);
        return ResponseEntity.ok(OrderMapper.toDto(updatedOrder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{idOrder}/items")
    public ResponseEntity<OrderItem> addOrderItem(@PathVariable Long idOrder, @RequestBody OrderItem orderItem) {
        OrderItem addedItem = orderService.addItemToOrder(idOrder, orderItem);
        if (addedItem != null) {
            return ResponseEntity.ok(addedItem);
        }
        return ResponseEntity.notFound().build();
    }

}

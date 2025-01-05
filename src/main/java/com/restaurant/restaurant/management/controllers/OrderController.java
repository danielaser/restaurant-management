package com.restaurant.restaurant.management.controllers;

import com.restaurant.restaurant.management.dto.OrderItemResponseDto;
import com.restaurant.restaurant.management.dto.OrderResponseDto;
import com.restaurant.restaurant.management.dtoConverter.OrderItemMapper;
import com.restaurant.restaurant.management.dtoConverter.OrderMapper;
import com.restaurant.restaurant.management.models.OrderRestaurant;
import com.restaurant.restaurant.management.models.OrderItem;
import com.restaurant.restaurant.management.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> addOrder(@RequestBody OrderRestaurant orderRestaurant) {
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
    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable Long id, @RequestBody OrderRestaurant orderUpdated) {
        try {
            OrderRestaurant updatedOrder = orderService.updateOrder(id, orderUpdated);
            return ResponseEntity.ok(OrderMapper.toDto(updatedOrder));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idOrder}/items")
    public ResponseEntity<OrderResponseDto> addItemToOrder(@PathVariable Long idOrder, @RequestBody OrderItemResponseDto orderItemResponseDto) {
        OrderItem orderItem = OrderItemMapper.toEntity(orderItemResponseDto);
        OrderItem addedItem = orderService.addItemToOrder(idOrder, orderItem);

        if (addedItem != null) {
            Optional<OrderRestaurant> updatedOrderOpt = orderService.getOrder(idOrder);
            if (updatedOrderOpt.isPresent()) {
                OrderRestaurant updatedOrder = updatedOrderOpt.get();
                OrderResponseDto updatedOrderDto = OrderMapper.toDto(updatedOrder);
                return ResponseEntity.ok(updatedOrderDto);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{idOrder}/items/{idOrderItem}")
    public ResponseEntity<Void> removeItemFromOrder(@PathVariable Long idOrder, @PathVariable Long idOrderItem) {
        orderService.removeItemFromOrder(idOrder, idOrderItem);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idOrder}/items/{idOrderItem}")
    public ResponseEntity<OrderResponseDto> updateItemInOrder(@PathVariable Long idOrder, @PathVariable Long idOrderItem, @RequestBody OrderItemResponseDto orderItemUpdatedDto) {
        OrderItem orderItemUpdated = OrderItemMapper.toEntity(orderItemUpdatedDto);
        OrderItem updatedItem = orderService.updateItemInOrder(idOrder, idOrderItem, orderItemUpdated);

        if (updatedItem != null) {
            Optional<OrderRestaurant> updatedOrderOpt = orderService.getOrder(idOrder);
            if (updatedOrderOpt.isPresent()) {
                OrderRestaurant updatedOrder = updatedOrderOpt.get();
                OrderResponseDto updatedOrderDto = OrderMapper.toDto(updatedOrder);
                return ResponseEntity.ok(updatedOrderDto);
            }
        }
        return ResponseEntity.notFound().build();
    }
}

package com.restaurant.restaurant.management.controllers;

import com.restaurant.restaurant.management.dto.OrderResponseDto;
import com.restaurant.restaurant.management.dtoConverter.OrderItemMapper;
import com.restaurant.restaurant.management.dtoConverter.OrderMapper;
import com.restaurant.restaurant.management.models.*;
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

    @PostMapping("/{clientName}")
    public ResponseEntity<OrderResponseDto> addOrder(@RequestBody OrderResponseDto orderDto, @PathVariable String clientName) {
        Optional<Client> clientOptional = orderService.getClientByName(clientName);
        if (!clientOptional.isPresent()) return ResponseEntity.notFound().build();
        OrderRestaurant order = OrderMapper.toEntity(orderDto);
        order.setClient(clientOptional.get());
        OrderRestaurant addedOrder = orderService.addOrder(order, clientOptional.get().getIdClient());
        return ResponseEntity.ok(OrderMapper.toDto(addedOrder));
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
        OrderRestaurant order = OrderMapper.toEntity(orderDto);
        order.setIdOrder(id);
        OrderRestaurant updatedOrder = orderService.updateOrder(id, order);
        return ResponseEntity.ok(OrderMapper.toDto(updatedOrder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idOrder}/orderItems")
    public ResponseEntity<?> addOrderItem(@PathVariable Long idOrder, @RequestBody OrderItem orderItem) {
        try {
            OrderItem addedItem = orderService.addItemToOrder(idOrder, orderItem);
            if (addedItem != null) return ResponseEntity.ok(addedItem);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El plato no existe o la orden no se encuentra.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al agregar el item al pedido: " + e.getMessage());
        }
    }

    @GetMapping("/{idOrder}/orderItems/{idOrderItem}")
    public ResponseEntity<?> getOrderItem(@PathVariable Long idOrder, @PathVariable Long idOrderItem) {
        Optional<OrderItem> orderItemOpt = orderService.getOrderItemById(idOrderItem);
        if (orderItemOpt.isPresent() && orderItemOpt.get().getOrder().getIdOrder().equals(idOrder))
            return ResponseEntity.ok(OrderItemMapper.toDto(orderItemOpt.get()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("OrderItem not found or does not belong to the order.");
    }

    @PutMapping("/{idOrder}/orderItems/{idOrderItem}")
    public ResponseEntity<?> updateOrderItem(@PathVariable Long idOrder, @PathVariable Long idOrderItem, @RequestBody OrderItem orderItem) {
        try {
            if (!orderService.getOrder(idOrder).isPresent())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found.");
            orderItem.setOrder(orderService.getOrder(idOrder).get());
            OrderItem updatedItem = orderService.updateOrderItem(idOrderItem, orderItem);
            return ResponseEntity.ok(OrderItemMapper.toDto(updatedItem));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating the OrderItem: " + e.getMessage());
        }
    }

    @DeleteMapping("/{idOrder}/orderItems/{idOrderItem}")
    public ResponseEntity<?> deleteOrderItem(@PathVariable Long idOrder, @PathVariable Long idOrderItem) {
        try {
            if (!orderService.getOrder(idOrder).isPresent())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found.");
            orderService.deleteOrderItem(idOrderItem);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting the OrderItem: " + e.getMessage());
        }
    }
}

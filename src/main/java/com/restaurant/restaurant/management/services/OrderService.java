package com.restaurant.restaurant.management.services;

import com.restaurant.restaurant.management.models.OrderRestaurant;
import com.restaurant.restaurant.management.models.OrderItem;
import com.restaurant.restaurant.management.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderRestaurant addOrder(OrderRestaurant orderRestaurant) {
        return orderRepository.save(orderRestaurant);
    }

    public List<OrderRestaurant> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<OrderRestaurant> getOrder(Long id) {
        return orderRepository.findById(id);
    }

    public OrderRestaurant updateOrder(Long id, OrderRestaurant orderUpdated) {
        return orderRepository.findById(id).map(existingOrder -> {
            existingOrder.setClient(orderUpdated.getClient());
            existingOrder.setOrderItems(orderUpdated.getOrderItems());
            existingOrder.setTotalAmount(orderUpdated.getTotalAmount());
            return orderRepository.save(existingOrder);
        }).orElseThrow(() -> new RuntimeException("El pedido con id: " + id + " no pudo ser actualizado"));
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public OrderItem addItemToOrder(Long idOrder, OrderItem orderItem) {
        Optional<OrderRestaurant> orderOpt = orderRepository.findById(idOrder);
        if (orderOpt.isPresent()) {
            OrderRestaurant order = orderOpt.get();
            orderItem.setDish(orderItem.getDish());
            order.getOrderItems().add(orderItem);
            orderRepository.save(order);
            return orderItem;
        }
        return null;
    }

    public void removeItemFromOrder(Long idOrder, Long idOrderItem) {
        Optional<OrderRestaurant> orderOpt = orderRepository.findById(idOrder);
        if (orderOpt.isPresent()) {
            OrderRestaurant order = orderOpt.get();
            order.getOrderItems().removeIf(item -> item.getIdOrderItem().equals(idOrderItem));
            orderRepository.save(order);
        }
    }

    public OrderItem updateItemInOrder(Long idOrder, Long idOrderItem, OrderItem orderItemUpdated) {
        Optional<OrderRestaurant> orderOpt = orderRepository.findById(idOrder);
        if (orderOpt.isPresent()) {
            OrderRestaurant order = orderOpt.get();
            Optional<OrderItem> itemOpt = order.getOrderItems().stream()
                    .filter(item -> item.getIdOrderItem().equals(idOrderItem))
                    .findFirst();

            if (itemOpt.isPresent()) {
                OrderItem item = itemOpt.get();
                item.setQuantity(orderItemUpdated.getQuantity());
                item.setDish(orderItemUpdated.getDish());
                orderRepository.save(order);
                return item;
            }
        }
        return null;
    }
}

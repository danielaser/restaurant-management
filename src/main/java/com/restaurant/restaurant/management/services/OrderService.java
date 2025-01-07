package com.restaurant.restaurant.management.services;

import com.restaurant.restaurant.management.dto.OrderItemResponseDto;
import com.restaurant.restaurant.management.dto.OrderResponseDto;
import com.restaurant.restaurant.management.models.*;
import com.restaurant.restaurant.management.repositories.ClientRepository;
import com.restaurant.restaurant.management.repositories.DishRepository;
import com.restaurant.restaurant.management.repositories.OrderItemRepository;
import com.restaurant.restaurant.management.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;
    private final ClientRepository clientRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, DishRepository dishRepository, ClientRepository clientRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.dishRepository = dishRepository;
        this.clientRepository = clientRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public OrderRestaurant addOrder(OrderRestaurant orderRestaurant) {
        Optional<Client> clientOpt = clientRepository.findById(orderRestaurant.getClient().getIdClient());
        if (clientOpt.isPresent()) {
            orderRestaurant.setClient(clientOpt.get());
            return orderRepository.save(orderRestaurant);
        }
        throw new RuntimeException("Cliente no encontrado");
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
        // Buscar la Order por id
        Optional<OrderRestaurant> orderOpt = orderRepository.findById(idOrder);
        if (!orderOpt.isPresent()) {
            throw new RuntimeException("Order not found with id: " + idOrder);
        }

        // Buscar el Dish por id
        Optional<Dish> dishOpt = dishRepository.findById(orderItem.getDish().getIdDish());
        if (!dishOpt.isPresent()) {
            throw new RuntimeException("Dish not found with id: " + orderItem.getDish().getIdDish());
        }

        // Asociar el Dish al OrderItem
        orderItem.setDish(dishOpt.get());  // Establecer el Dish al OrderItem

        // Asociar la Order al OrderItem
        OrderRestaurant order = orderOpt.get();
        orderItem.setOrder(order);  // Establecer la Order al OrderItem

        // Guardar el OrderItem y asociarlo a la Order
        OrderItem savedItem = orderItemRepository.save(orderItem);
        order.getOrderItems().add(savedItem);  // Agregar el OrderItem a la lista de OrderItems de la Order
        orderRepository.save(order);  // Guardar la Order con el nuevo OrderItem

        return savedItem;
    }

}

package com.restaurant.restaurant.management.services;

import com.restaurant.restaurant.management.chain.DishHandler;
import com.restaurant.restaurant.management.chain.IncreasePriceHandler;
import com.restaurant.restaurant.management.chain.PopularityHandler;
import com.restaurant.restaurant.management.models.*;
import com.restaurant.restaurant.management.observer.AdminNotifier;
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

    public Optional<Client> getClientByName(String clientName) {
        return clientRepository.findAll().stream()
                .filter(client -> client.getClientName().equalsIgnoreCase(clientName))
                .findFirst();
    }

    public OrderRestaurant addOrder(OrderRestaurant orderRestaurant, Long clientId) {
        Optional<Client> clientOpt = clientRepository.findById(clientId);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            orderRestaurant.setClient(client);
            long orderCount = orderRepository.countOrdersByClientId(clientId);
            isFrecuentClient(orderCount, client);
            applyDiscount(orderRestaurant, client);
            return orderRepository.save(orderRestaurant);
        } else throw new RuntimeException("El cliente con ID: " + clientId + " no fue encontrado");
    }

    private static void applyDiscount(OrderRestaurant orderRestaurant, Client client) {
        if (client.isFrequentUser()) {
            double discount = 0.0238;
            orderRestaurant.setTotalAmount(orderRestaurant.getTotalAmount() * (1 - discount));
        }
    }

    private void isFrecuentClient(long orderCount, Client client) {
        if (orderCount >= 10 && !client.isFrequentUser()) {
            client.setFrequentUser(true);
            clientRepository.save(client);
        }
    }

    public List<OrderRestaurant> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<OrderRestaurant> getOrder(Long id) {
        return orderRepository.findById(id);
    }

    public OrderRestaurant updateOrder(Long id, OrderRestaurant orderUpdated) {
        return orderRepository.findById(id).map(existingOrder -> {
            existingOrder.setTotalAmount(orderUpdated.getTotalAmount());
            existingOrder.getOrderItems().clear();
            getItems(orderUpdated, existingOrder);
            return orderRepository.save(existingOrder);
        }).orElseThrow(() -> new RuntimeException("El pedido con id: " + id + " no pudo ser actualizado"));
    }

    private static void getItems(OrderRestaurant orderUpdated, OrderRestaurant existingOrder) {
        if (orderUpdated.getOrderItems() != null) {
            orderUpdated.getOrderItems().forEach(item -> {
                item.setOrder(existingOrder);
                existingOrder.getOrderItems().add(item);
            });
        }
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public OrderItem addItemToOrder(Long idOrder, OrderItem orderItem) {
        Optional<OrderRestaurant> orderOpt = orderRepository.findById(idOrder);
        if (orderOpt.isPresent()) {
            OrderRestaurant order = orderOpt.get();

            // Verificar si el plato existe y asociarlo con el OrderItem
            Optional<Dish> dishOpt = dishRepository.findById(orderItem.getIdDish());
            if (dishOpt.isPresent()) {
                Dish dish = dishOpt.get();

                // Aplicar patrones de popularidad y precio antes de agregar el plato
                addPatterns(dish);

                // Asociar el plato con el item y con el pedido
                orderItem.setDish(dish);
                orderItem.setOrder(order);

                // Guardar el OrderItem y actualizar la lista de platos en el pedido
                OrderItem savedItem = orderItemRepository.save(orderItem);
                order.getOrderItems().add(savedItem);
                orderRepository.save(order);

                return savedItem;
            } else {
                throw new RuntimeException("Dish not found with id: " + orderItem.getIdDish());
            }
        }
        return null;
    }

    private void addPatterns(Dish dish) {
        if (dish.getObservers().isEmpty()) {
            AdminNotifier adminNotifier = new AdminNotifier();
            dish.addObserver(adminNotifier);
        }

        DishHandler popularityHandler = new PopularityHandler();
        DishHandler increasePriceHandler = new IncreasePriceHandler();
        popularityHandler.setNextHandler(increasePriceHandler);

        long timesOrdered = orderRepository.countOrdersByDishId(dish.getIdDish());
        popularityHandler.handle(dish, (int) timesOrdered);
    }

    public Optional<OrderItem> getOrderItemById(Long idOrderItem) {
        return orderItemRepository.findById(idOrderItem);
    }

    public OrderItem updateOrderItem(Long idOrderItem, OrderItem updatedOrderItem) {
        return orderItemRepository.findById(idOrderItem).map(existingOrderItem -> {
            Optional<Dish> dishOpt = dishRepository.findById(updatedOrderItem.getIdDish());
            if (dishOpt.isPresent()) existingOrderItem.setDish(dishOpt.get());
            else throw new RuntimeException("Dish not found with id: " + updatedOrderItem.getIdDish());
            existingOrderItem.setQuantity(updatedOrderItem.getQuantity());
            return orderItemRepository.save(existingOrderItem);
        }).orElseThrow(() -> new RuntimeException("OrderItem with id: " + idOrderItem + " not found"));
    }

    public void deleteOrderItem(Long idOrderItem) {
        if (orderItemRepository.existsById(idOrderItem)) orderItemRepository.deleteById(idOrderItem);
        else throw new RuntimeException("OrderItem with id: " + idOrderItem + " not found");
    }
}

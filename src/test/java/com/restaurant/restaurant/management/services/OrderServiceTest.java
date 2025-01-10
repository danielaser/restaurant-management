package com.restaurant.restaurant.management.services;

import com.restaurant.restaurant.management.models.*;
import com.restaurant.restaurant.management.observer.AdminNotifier;
import com.restaurant.restaurant.management.repositories.ClientRepository;
import com.restaurant.restaurant.management.repositories.DishRepository;
import com.restaurant.restaurant.management.repositories.OrderItemRepository;
import com.restaurant.restaurant.management.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class OrderServiceTest {

    private OrderService orderService;
    private OrderRestaurant order;
    private Client client;
    private OrderItem orderItem;
    private Dish dish;

    private ClientRepository clientRepository;
    private OrderRepository orderRepository;
    private DishRepository dishRepository;
    private OrderItemRepository orderItemRepository;

    @BeforeEach
    void setup() {
        clientRepository = mock(ClientRepository.class);
        orderRepository = mock(OrderRepository.class);
        dishRepository = mock(DishRepository.class);
        orderItemRepository = mock(OrderItemRepository.class);

        orderService = new OrderService(orderRepository, dishRepository, clientRepository, orderItemRepository);

        order = new OrderRestaurant();
        order.setIdOrder(1L);
        order.setOrderItems(new ArrayList<>());
        order.setTotalAmount(100.0);

        client = new Client();
        client.setClientName("Dani");
        client.setIdClient(1L);

        dish = new Dish();
        dish.setIdDish(1L);
        dish.setDishName("Plato Test");
        dish.setPrice(10.0);

        orderItem = new OrderItem();
        orderItem.setIdOrderItem(1L);
        orderItem.setIdDish(dish.getIdDish());
        orderItem.setQuantity(2);
    }

    @Test
    @DisplayName("Agregar un pedido correctamente")
    void addOrder() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));
        when(orderRepository.countOrdersByClientId(anyLong())).thenReturn(5L);
        when(orderRepository.save(any(OrderRestaurant.class))).thenReturn(order);

        OrderRestaurant savedOrder = orderService.addOrder(order, client.getIdClient());

        assertNotNull(savedOrder);
        assertEquals(order.getIdOrder(), savedOrder.getIdOrder());
        verify(orderRepository).save(any(OrderRestaurant.class));
    }

    @Test
    @DisplayName("Agregar un item a una orden correctamente")
    void addItemToOrder() {

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        OrderItem savedItem = orderService.addItemToOrder(order.getIdOrder(), orderItem);

        assertNotNull(savedItem);
        assertEquals(orderItem.getIdOrderItem(), savedItem.getIdOrderItem());
        verify(orderItemRepository).save(any(OrderItem.class));
    }

    @Test
    @DisplayName("Obtener un pedido por id correctamente")
    void getOrderById() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        Optional<OrderRestaurant> foundOrder = orderService.getOrder(order.getIdOrder());

        assertTrue(foundOrder.isPresent());
        assertEquals(order.getIdOrder(), foundOrder.get().getIdOrder());
    }

    @Test
    @DisplayName("Actualizar un pedido correctamente")
    void updateOrder() {
        OrderRestaurant updatedOrder = new OrderRestaurant();
        updatedOrder.setTotalAmount(120.0);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(OrderRestaurant.class))).thenReturn(updatedOrder);

        OrderRestaurant savedOrder = orderService.updateOrder(order.getIdOrder(), updatedOrder);

        assertNotNull(savedOrder);
        assertEquals(updatedOrder.getTotalAmount(), savedOrder.getTotalAmount());
        verify(orderRepository).save(any(OrderRestaurant.class));
    }

    @Test
    @DisplayName("Eliminar un pedido correctamente")
    void deleteOrder() {
        doNothing().when(orderRepository).deleteById(anyLong());

        orderService.deleteOrder(order.getIdOrder());

        verify(orderRepository).deleteById(anyLong());
    }

    @Test
    @DisplayName("Agregar un item a una orden cuando no se encuentra el plato")
    void addOrderItemNotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(dishRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> orderService.addItemToOrder(order.getIdOrder(), orderItem));
        assertEquals("Dish not found with id: " + orderItem.getIdDish(), thrown.getMessage());
    }

    @Test
    @DisplayName("Actualizar un item de una orden correctamente")
    void updateOrderItem() {
        OrderItem updatedOrderItem = new OrderItem();
        updatedOrderItem.setQuantity(3);
        updatedOrderItem.setIdDish(dish.getIdDish());

        when(orderItemRepository.findById(anyLong())).thenReturn(Optional.of(orderItem));
        when(dishRepository.findById(anyLong())).thenReturn(Optional.of(dish));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(updatedOrderItem);

        OrderItem savedItem = orderService.updateOrderItem(orderItem.getIdOrderItem(), updatedOrderItem);

        assertNotNull(savedItem);
        assertEquals(updatedOrderItem.getQuantity(), savedItem.getQuantity());
        verify(orderItemRepository).save(any(OrderItem.class));
    }

    @Test
    @DisplayName("Eliminar un item de una orden correctamente")
    void deleteOrderItem() {
        when(orderItemRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(orderItemRepository).deleteById(anyLong());

        orderService.deleteOrderItem(orderItem.getIdOrderItem());

        verify(orderItemRepository).deleteById(anyLong());
        verify(orderItemRepository).existsById(anyLong());
    }

    @Test
    @DisplayName("Obtener todos los pedidos correctamente")
    void getAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<OrderRestaurant> orders = orderService.getAllOrders();

        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(order.getIdOrder(), orders.get(0).getIdOrder());
    }

    @Test
    @DisplayName("No agregar item a una orden cuando no se encuentra la orden")
    void addOrderItemOrderNotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                orderService.addItemToOrder(order.getIdOrder(), orderItem)
        );
        assertEquals("El pedido con id: " + order.getIdOrder() + " no fue encontrado", thrown.getMessage());
    }

    @Test
    @DisplayName("Aplicar descuento correctamente para un cliente frecuente")
    void applyDiscount() {
        Client frequentClient = new Client();
        frequentClient.setIdClient(2L);
        frequentClient.setClientName("Alice");
        frequentClient.setFrequentUser(true);

        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(frequentClient));
        when(orderRepository.save(any(OrderRestaurant.class))).thenReturn(order);

        orderService.addOrder(order, frequentClient.getIdClient());

        assertEquals(100.0 * (1 - 0.0238), order.getTotalAmount());
    }

    @Test
    @DisplayName("El cliente no es frecuente y no se aplica descuento")
    void noApplyDiscountForNonFrequentClient() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));
        when(orderRepository.save(any(OrderRestaurant.class))).thenReturn(order);

        orderService.addOrder(order, client.getIdClient());

        assertEquals(100.0, order.getTotalAmount());
    }

    @Test
    @DisplayName("No encontrar un pedido por id")
    void getOrderNotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<OrderRestaurant> foundOrder = orderService.getOrder(order.getIdOrder());

        assertFalse(foundOrder.isPresent());
    }

    @Test
    @DisplayName("Error al eliminar un item de una orden cuando no se encuentra el item")
    void deleteOrderItemNotFound() {
        when(orderItemRepository.existsById(anyLong())).thenReturn(false);

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                orderService.deleteOrderItem(orderItem.getIdOrderItem())
        );
        assertEquals("OrderItem with id: " + orderItem.getIdOrderItem() + " not found", thrown.getMessage());
    }

    @Test
    @DisplayName("Obtener cliente por nombre correctamente")
    void getClientByName() {
        when(clientRepository.findAll()).thenReturn(List.of(client));

        Optional<Client> foundClient = orderService.getClientByName("Dani");

        assertTrue(foundClient.isPresent());
        assertEquals(client.getClientName(), foundClient.get().getClientName());
    }

    @Test
    @DisplayName("No agregar un pedido cuando el cliente no existe")
    void addOrderClientNotFound() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                orderService.addOrder(order, 3L)
        );
        assertEquals("El cliente con ID: 3 no fue encontrado", thrown.getMessage());
    }

    @Test
    @DisplayName("Marcar cliente como frecuente y guardar en el repositorio")
    void isFrequentClient() {
        client.setFrequentUser(false);

        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));
        when(orderRepository.countOrdersByClientId(anyLong())).thenReturn(10L);

        orderService.addOrder(order, client.getIdClient());

        assertTrue(client.isFrequentUser());
        verify(clientRepository).save(client);
    }


    @Test
    @DisplayName("Actualizar items de una orden correctamente")
    void getItems() {
        OrderRestaurant existingOrder = new OrderRestaurant();
        existingOrder.setOrderItems(new ArrayList<>());

        OrderItem newItem = new OrderItem();
        newItem.setIdOrderItem(1L);
        newItem.setQuantity(2);

        OrderRestaurant updatedOrder = new OrderRestaurant();
        updatedOrder.setOrderItems(List.of(newItem));

        orderService.getItems(updatedOrder, existingOrder);

        assertEquals(1, existingOrder.getOrderItems().size());
        assertEquals(newItem.getIdOrderItem(), existingOrder.getOrderItems().get(0).getIdOrderItem());
    }

    @Test
    @DisplayName("Obtener un item de la orden por id")
    void getOrderItemById() {
        when(orderItemRepository.findById(anyLong())).thenReturn(Optional.of(orderItem));

        Optional<OrderItem> foundOrderItem = orderService.getOrderItemById(orderItem.getIdOrderItem());

        assertTrue(foundOrderItem.isPresent());
        assertEquals(orderItem.getIdOrderItem(), foundOrderItem.get().getIdOrderItem());
    }

    @Test
    @DisplayName("No actualizar item de una orden cuando el plato no existe")
    void updateOrderItemDishNotFound() {
        OrderItem updatedOrderItem = new OrderItem();
        updatedOrderItem.setIdDish(5L);

        when(orderItemRepository.findById(anyLong())).thenReturn(Optional.of(orderItem));
        when(dishRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                orderService.updateOrderItem(orderItem.getIdOrderItem(), updatedOrderItem)
        );
        assertEquals("Dish not found with id: " + updatedOrderItem.getIdDish(), thrown.getMessage());
    }

    @Test
    @DisplayName("Marcar cliente como frecuente cuando tiene mas de 10 pedidos")
    void markClientAsFrequentWhenOrderCountIsAbove10() {
        client.setFrequentUser(false);
        when(orderRepository.countOrdersByClientId(client.getIdClient())).thenReturn(11L);
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));
        when(orderRepository.save(any(OrderRestaurant.class))).thenReturn(order);

        orderService.addOrder(order, client.getIdClient());

        assertTrue(client.isFrequentUser());
        verify(clientRepository).save(client);
    }

    @Test
    @DisplayName("No marcar cliente como frecuente cuando tiene menos de 10 pedidos")
    void doNotMarkClientAsFrequentWhenOrderCountIsBelow10() {
        client.setFrequentUser(false);
        when(orderRepository.countOrdersByClientId(client.getIdClient())).thenReturn(9L);
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));
        when(orderRepository.save(any(OrderRestaurant.class))).thenReturn(order);

        orderService.addOrder(order, client.getIdClient());

        assertFalse(client.isFrequentUser());
        verify(clientRepository, never()).save(client);
    }

    @Test
    @DisplayName("No marcar cliente como frecuente si ya lo es")
    void doNotMarkClientAsFrequentWhenAlreadyFrequent() {
        client.setFrequentUser(true);
        when(orderRepository.countOrdersByClientId(client.getIdClient())).thenReturn(15L);
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));
        when(orderRepository.save(any(OrderRestaurant.class))).thenReturn(order);

        orderService.addOrder(order, client.getIdClient());

        assertTrue(client.isFrequentUser());
        verify(clientRepository, never()).save(client);
    }

    @Test
    @DisplayName("Agregar items a la orden si los items no son null")
    void addItemsToOrderIfOrderItemsIsNotNull() {
        OrderItem newItem = new OrderItem();
        newItem.setIdOrderItem(1L);
        newItem.setQuantity(2);

        OrderRestaurant updatedOrder = new OrderRestaurant();
        updatedOrder.setOrderItems(List.of(newItem));

        OrderRestaurant existingOrder = new OrderRestaurant();
        existingOrder.setOrderItems(new ArrayList<>());

        orderService.getItems(updatedOrder, existingOrder);

        assertEquals(1, existingOrder.getOrderItems().size());
        assertEquals(newItem.getIdOrderItem(), existingOrder.getOrderItems().get(0).getIdOrderItem());
    }

    @Test
    @DisplayName("No agregar items a la orden si los items son null")
    void doNotAddItemsToOrderIfOrderItemsIsNull() {
        OrderRestaurant updatedOrder = new OrderRestaurant();
        updatedOrder.setOrderItems(null);

        OrderRestaurant existingOrder = new OrderRestaurant();
        existingOrder.setOrderItems(new ArrayList<>());

        orderService.getItems(updatedOrder, existingOrder);
        assertEquals(0, existingOrder.getOrderItems().size());
    }

    @Test
    @DisplayName("Verificar que se agrega el patrón cuando no hay observadores")
    void addPatternsWhenDishHasNoObservers() {
        Dish dishWithoutObservers = new Dish();
        dishWithoutObservers.setIdDish(1L);
        dishWithoutObservers.setDishName("Plato Test");
        dishWithoutObservers.setPrice(10.0);
        dishWithoutObservers.setObservers(new ArrayList<>());

        when(dishRepository.findById(anyLong())).thenReturn(Optional.of(dishWithoutObservers));
        when(orderRepository.countOrdersByDishId(dishWithoutObservers.getIdDish())).thenReturn(100L);
        orderService.addPatterns(dishWithoutObservers);

        assertFalse(dishWithoutObservers.getObservers().isEmpty(), "El plato debe tener observadores");
        assertEquals(1, dishWithoutObservers.getObservers().size(), "Debería tener solo un observador");

        verify(dishRepository, times(1)).save(dishWithoutObservers);
    }

    @Test
    @DisplayName("No agregar observador si el plato ya tiene observadores")
    void doNotAddObserverIfDishHasObservers() {
        dish.setObservers(List.of(new AdminNotifier()));
        orderService.addPatterns(dish);

        assertEquals(1, dish.getObservers().size());
    }
}

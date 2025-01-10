package com.restaurant.restaurant.management.controllers;

import com.restaurant.restaurant.management.dto.OrderItemResponseDto;
import com.restaurant.restaurant.management.dto.OrderResponseDto;
import com.restaurant.restaurant.management.dtoConverter.OrderItemMapper;
import com.restaurant.restaurant.management.dtoConverter.OrderMapper;
import com.restaurant.restaurant.management.models.Client;
import com.restaurant.restaurant.management.models.Dish;
import com.restaurant.restaurant.management.models.OrderItem;
import com.restaurant.restaurant.management.models.OrderRestaurant;
import com.restaurant.restaurant.management.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    private WebTestClient webTestClient;
    private OrderService orderService;
    private OrderRestaurant order;
    private OrderResponseDto orderResponseDto;
    private Client client;
    private OrderItem orderItem;
    private OrderItemResponseDto orderItemResponseDto;
    private Dish dish;

    @BeforeEach
    void setup() {
        orderService = mock(OrderService.class);
        webTestClient = WebTestClient.bindToController(new OrderController(orderService)).build();

        order = new OrderRestaurant();
        order.setIdOrder(1L);
        order.setTotalAmount(100.0);

        orderResponseDto = new OrderResponseDto();
        orderResponseDto.setIdOrder(1L);
        orderResponseDto.setTotalAmount(100.0);

        client = new Client();
        client.setClientName("John");
        client.setIdClient(1L);

        dish = new Dish();
        dish.setIdDish(1L);

        orderItem = new OrderItem();
        orderItem.setIdOrderItem(1L);
        orderItem.setDish(dish);
        orderItem.setQuantity(2);

        orderItemResponseDto = new OrderItemResponseDto();
        orderItemResponseDto.setIdOrderItem(1L);
        orderItemResponseDto.setQuantity(2);
        orderItemResponseDto.setIdDish(1L);
    }

    @Test
    @DisplayName("Agregar un pedido")
    void addOrder() {
        when(orderService.getClientByName("John")).thenReturn(Optional.of(client));
        when(orderService.addOrder(any(OrderRestaurant.class), anyLong())).thenReturn(order);

        order.setClient(client);
        orderResponseDto.setClientName(client.getClientName());

        webTestClient.post()
                .uri("/api/orders/John")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(orderResponseDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderResponseDto.class)
                .value(dto -> {
                    assertEquals(orderResponseDto.getIdOrder(), dto.getIdOrder());
                    assertEquals(orderResponseDto.getTotalAmount(), dto.getTotalAmount());
                    assertEquals(orderResponseDto.getClientName(), dto.getClientName());
                });

        verify(orderService).addOrder(any(OrderRestaurant.class), eq(client.getIdClient()));
    }

    @Test
    @DisplayName("Obtener un pedido por id")
    void getOrderById() {
        when(orderService.getOrder(anyLong())).thenReturn(Optional.of(order));

        webTestClient.get()
                .uri("/api/orders/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(OrderResponseDto.class)
                .value(dto -> {
                    assertEquals(orderResponseDto.getIdOrder(), dto.getIdOrder());
                    assertEquals(orderResponseDto.getTotalAmount(), dto.getTotalAmount());
                });

        verify(orderService).getOrder(anyLong());
    }

    @Test
    @DisplayName("Obtener todos los pedidos")
    void getAllOrders() {
        when(orderService.getAllOrders()).thenReturn(List.of(order));

        webTestClient.get()
                .uri("/api/orders")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(OrderResponseDto.class)
                .hasSize(1)
                .value(list -> {
                    assertEquals(orderResponseDto.getIdOrder(), list.get(0).getIdOrder());
                    assertEquals(orderResponseDto.getTotalAmount(), list.get(0).getTotalAmount());
                });

        verify(orderService).getAllOrders();
    }

    @Test
    @DisplayName("Actualizar un pedido")
    void updateOrder() {
        when(orderService.updateOrder(anyLong(), any(OrderRestaurant.class))).thenReturn(order);

        webTestClient.put()
                .uri("/api/orders/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(orderResponseDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(OrderResponseDto.class)
                .value(dto -> {
                    assertEquals(orderResponseDto.getIdOrder(), dto.getIdOrder());
                    assertEquals(orderResponseDto.getTotalAmount(), dto.getTotalAmount());
                });

        verify(orderService).updateOrder(anyLong(), any(OrderRestaurant.class));
    }

    @Test
    @DisplayName("Eliminar un pedido")
    void deleteOrder() {
        doNothing().when(orderService).deleteOrder(anyLong());

        webTestClient.delete()
                .uri("/api/orders/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();

        verify(orderService).deleteOrder(anyLong());
    }

    @Test
    @DisplayName("Agregar un item a una orden")
    void addOrderItem() {
        when(orderService.addItemToOrder(anyLong(), any(OrderItem.class))).thenReturn(orderItem);

        webTestClient.post()
                .uri("/api/orders/{idOrder}/orderItems", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(orderItemResponseDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderItemResponseDto.class)
                .value(dto -> {
                    assertEquals(orderItemResponseDto.getIdOrderItem(), dto.getIdOrderItem());
                    assertEquals(orderItemResponseDto.getQuantity(), dto.getQuantity());
                    assertEquals(orderItemResponseDto.getIdDish(), dto.getIdDish());
                });

        verify(orderService).addItemToOrder(anyLong(), any(OrderItem.class));
    }

    @Test
    @DisplayName("Obtener un item de una orden por id")
    void getOrderItem() {
        orderItem.setOrder(order);
        when(orderService.getOrderItemById(anyLong())).thenReturn(Optional.of(orderItem));

        webTestClient.get()
                .uri("/api/orders/{idOrder}/orderItems/{idOrderItem}", 1L, 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderItemResponseDto.class)
                .value(dto -> {
                    assertEquals(orderItemResponseDto.getIdOrderItem(), dto.getIdOrderItem());
                    assertEquals(orderItemResponseDto.getQuantity(), dto.getQuantity());
                    assertEquals(orderItemResponseDto.getIdDish(), dto.getIdDish());
                });

        verify(orderService).getOrderItemById(anyLong());
    }

    @Test
    @DisplayName("Actualizar un item de una orden")
    void updateOrderItem() {
        when(orderService.getOrder(anyLong())).thenReturn(Optional.of(order));
        when(orderService.updateOrderItem(anyLong(), any(OrderItem.class))).thenReturn(orderItem);

        webTestClient.put()
                .uri("/api/orders/{idOrder}/orderItems/{idOrderItem}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(orderItemResponseDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderItemResponseDto.class)
                .value(dto -> {
                    assertEquals(orderItemResponseDto.getIdOrderItem(), dto.getIdOrderItem());
                    assertEquals(orderItemResponseDto.getQuantity(), dto.getQuantity());
                    assertEquals(orderItemResponseDto.getIdDish(), dto.getIdDish());
                });

        verify(orderService).updateOrderItem(anyLong(), any(OrderItem.class));
    }

    @Test
    @DisplayName("Eliminar un item de una orden")
    void deleteOrderItem() {
        when(orderService.getOrder(1L)).thenReturn(Optional.of(order));
        doNothing().when(orderService).deleteOrderItem(anyLong());

        webTestClient.delete()
                .uri("/api/orders/{idOrder}/orderItems/{idOrderItem}", 1L, 1L)
                .exchange()
                .expectStatus().isNoContent();

        verify(orderService).deleteOrderItem(anyLong());
    }

    @Test
    @DisplayName("No agregar item a una orden cuando no se encuentra el plato")
    void addOrderItemNotFound() {
        when(orderService.addItemToOrder(anyLong(), any(OrderItem.class))).thenReturn(null);

        webTestClient.post()
                .uri("/api/orders/{idOrder}/orderItems", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(orderItemResponseDto)
                .exchange()
                .expectStatus().isNotFound();

        verify(orderService).addItemToOrder(anyLong(), any(OrderItem.class));
    }

    @Test
    @DisplayName("No encontrar item de la orden por id")
    void getOrderItemNotFound() {
        when(orderService.getOrderItemById(anyLong())).thenReturn(Optional.empty());

        webTestClient.get()
                .uri("/api/orders/{idOrder}/orderItems/{idOrderItem}", 1L, 1L)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value(message -> assertEquals("OrderItem not found or does not belong to the order.", message));

        verify(orderService).getOrderItemById(anyLong());
    }

    @Test
    @DisplayName("No actualizar item de la orden cuando no se encuentra la orden")
    void updateOrderItemOrderNotFound() {
        when(orderService.getOrder(anyLong())).thenReturn(Optional.empty());

        webTestClient.put()
                .uri("/api/orders/{idOrder}/orderItems/{idOrderItem}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(orderItemResponseDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value(message -> assertEquals("Order not found.", message));

        verify(orderService, never()).updateOrderItem(anyLong(), any(OrderItem.class));
    }

    @Test
    @DisplayName("No eliminar item de la orden cuando no se encuentra la orden")
    void deleteOrderItemOrderNotFound() {
        when(orderService.getOrder(anyLong())).thenReturn(Optional.empty());

        webTestClient.delete()
                .uri("/api/orders/{idOrder}/orderItems/{idOrderItem}", 1L, 1L)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value(message -> assertEquals("Order not found.", message));

        verify(orderService, never()).deleteOrderItem(anyLong());
    }

    @Test
    @DisplayName("Mapear OrderItemResponseDto a entidad OrderItem")
    void toEntityOrderItem() {
        OrderItem orderItem = OrderItemMapper.toEntity(orderItemResponseDto);

        assertNotNull(orderItem);
        assertEquals(orderItemResponseDto.getIdOrderItem(), orderItem.getIdOrderItem());
        assertNotNull(orderItem.getDish());
        assertEquals(orderItemResponseDto.getIdDish(), orderItem.getDish().getIdDish());
        assertEquals(orderItemResponseDto.getQuantity(), orderItem.getQuantity());
    }

    @Test
    @DisplayName("Mapear OrderResponseDto a entidad OrderRestaurant")
    void toEntityOrder() {
        orderResponseDto.setOrderItems(List.of(orderItemResponseDto));
        OrderRestaurant order = OrderMapper.toEntity(orderResponseDto);

        assertNotNull(order);
        assertEquals(orderResponseDto.getIdOrder(), order.getIdOrder());
        assertEquals(orderResponseDto.getTotalAmount(), order.getTotalAmount());
        assertNotNull(order.getOrderItems());
        assertEquals(orderResponseDto.getOrderItems().size(), order.getOrderItems().size());

        for (int i = 0; i < order.getOrderItems().size(); i++) {
            OrderItem orderItem = order.getOrderItems().get(i);
            OrderItemResponseDto orderItemResponseDto = orderResponseDto.getOrderItems().get(i);

            assertEquals(orderItemResponseDto.getIdOrderItem(), orderItem.getIdOrderItem());
            assertNotNull(orderItem.getDish());
            assertEquals(orderItemResponseDto.getIdDish(), orderItem.getDish().getIdDish());
            assertEquals(orderItemResponseDto.getQuantity(), orderItem.getQuantity());
        }
    }

    @Test
    @DisplayName("Error interno al agregar un item a una orden")
    void addOrderItemInternalError() {
        when(orderService.addItemToOrder(anyLong(), any(OrderItem.class))).thenThrow(new RuntimeException("Database error"));

        webTestClient.post()
                .uri("/api/orders/{idOrder}/orderItems", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(orderItemResponseDto)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody(String.class)
                .value(message -> assertNull(message));

        verify(orderService).addItemToOrder(anyLong(), any(OrderItem.class));
    }


    @Test
    @DisplayName("Error interno al actualizar un item de una orden")
    void updateOrderItemInternalError() {
        when(orderService.getOrder(anyLong())).thenReturn(Optional.of(order));
        when(orderService.updateOrderItem(anyLong(), any(OrderItem.class))).thenThrow(new RuntimeException("Update error"));

        webTestClient.put()
                .uri("/api/orders/{idOrder}/orderItems/{idOrderItem}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(orderItemResponseDto)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody(String.class)
                .value(message -> assertEquals("Error updating the OrderItem: Update error", message));

        verify(orderService).updateOrderItem(anyLong(), any(OrderItem.class));
    }

    @Test
    @DisplayName("Error interno al eliminar un item de una orden")
    void deleteOrderItemInternalError() {
        when(orderService.getOrder(anyLong())).thenReturn(Optional.of(order));
        doThrow(new RuntimeException("Delete error")).when(orderService).deleteOrderItem(anyLong());

        webTestClient.delete()
                .uri("/api/orders/{idOrder}/orderItems/{idOrderItem}", 1L, 1L)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody(String.class)
                .value(message -> assertEquals("Error deleting the OrderItem: Delete error", message));

        verify(orderService).deleteOrderItem(anyLong());
    }
}
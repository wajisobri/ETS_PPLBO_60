package com.programmingtechie.orderservice.service;

import com.programmingtechie.orderservice.dto.*;
import com.programmingtechie.orderservice.model.Ingredient;
import com.programmingtechie.orderservice.model.Order;
import com.programmingtechie.orderservice.model.OrderLineItems;
import com.programmingtechie.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public OrderResponse placeOrder(String username, String cafeId, OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderStatus(Order.OrderStatus.Created);

        // Map order line items from the incoming request to a list of DTOs and integrate with menu service to get ingredients
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsList()
                .stream()
                .map(orderLineItem -> {
                    MenuResponse menuResponse = webClientBuilder.build()
                            .get()
                            .uri(uriBuilder -> uriBuilder
                                    .scheme("http")
                                    .host("menu-service")
                                    .path("/api/menu/get")
                                    .queryParam("menuId", orderLineItem.getMenuId())
                                    .build())
                            .retrieve()
                            .bodyToMono(MenuResponse.class)
                            .block();

                    // Map menu response to order line item DTO and add ingredients to it
                    OrderLineItems orderLineItemDto = mapToDto(orderLineItem);
                    orderLineItemDto.setMenuId(orderLineItem.getMenuId());
                    orderLineItemDto.setMenuName(menuResponse.getName());
                    orderLineItemDto.setMenuPrice(menuResponse.getPrice());
                    orderLineItemDto.setQuantity(orderLineItem.getQuantity());
                    return orderLineItemDto;
                })
                .toList();

        // Set order line items
        order.setOrderLineItemsList(orderLineItems);
        order.setUsername(username);
        order.setRestaurantId(cafeId);

        // Check inventory for all required ingredients
        boolean allIngredientsInStock = checkIngredientsInStock(cafeId, orderLineItems);

        // If all ingredients are in stock, save the order
        if (allIngredientsInStock) {
            orderRepository.save(order);
            return OrderResponse.builder()
                    .code(200)
                    .message("Order placed")
                    .data(order)
                    .build();
        } else {
            throw new IllegalArgumentException("Menu is not in stock, please try again later");
        }
    }

    private boolean checkIngredientsInStock(String cafeId, List<OrderLineItems> orderLineItemsList) {
        List<Ingredient> ingredientList = new ArrayList<>();

        for (OrderLineItems orderLineItem : orderLineItemsList) {
            // Retrieve ingredient list for each menu item
            MenuResponse menuResponse = webClientBuilder.build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("http")
                            .host("menu-service")
                            .path("/api/menu/get")
                            .queryParam("menuId", orderLineItem.getMenuId())
                            .build())
                    .retrieve()
                    .bodyToMono(MenuResponse.class)
                    .block();
            if (menuResponse != null) {
                List<Ingredient> requiredIngredients = menuResponse.getRequiredIngredient();

                // Add all ingredients to the list
                if (requiredIngredients != null) {
                    for (Ingredient ingredient : requiredIngredients) {
                        Ingredient ingredientCopy = new Ingredient(ingredient);
                        ingredientCopy.setQuantity(ingredientCopy.getQuantity() * orderLineItem.getQuantity());
                        ingredientList.add(ingredientCopy);
                    }
                }
            }
        }

        // Check inventory for all ingredients
        for (Ingredient ingredient : ingredientList) {
            // Check inventory for each ingredient
            InventoryResponse inventoryResponse = webClientBuilder.build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("http")
                            .host("cafe-service")
                            .path("/api/cafe/ingredient/{cafeId}")
                            .queryParam("ingredientName", ingredient.getName())
                            .build(cafeId))
                    .retrieve()
                    .bodyToMono(InventoryResponse.class)
                    .block();

            if (inventoryResponse != null && inventoryResponse.getData().getQuantity() < ingredient.getQuantity()) {
                // If the required quantity is not available, return false
                return false;
            }
        }

        return true;
    }

    private OrderLineItems mapToDto(OrderLineItemsRequest orderLineItemsDto) {
        MenuResponse menuResponse = webClientBuilder.build().get()
                .uri("http://menu-service/api/menu/{menuId}", orderLineItemsDto.getMenuId())
                .retrieve()
                .bodyToMono(MenuResponse.class)
                .block();

        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setMenuId(orderLineItemsDto.getMenuId());
        orderLineItems.setMenuName(menuResponse.getName());
        orderLineItems.setMenuPrice(menuResponse.getPrice());

        return orderLineItems;
    }

    public OrdersResponse getAllOrders() {
        try {
            List<Order> orders = orderRepository.findAll();

            orders = orders.stream().map(this::mapToOrderResponse).toList();
            return OrdersResponse.builder()
                    .code(200)
                    .message("Orders retrieved")
                    .data(orders)
                    .build();
        } catch (DataAccessException e) {
            return OrdersResponse.builder()
                    .code(500)
                    .message("An error occurred while accessing the database")
                    .build();
        } catch (Exception e) {
            return OrdersResponse.builder()
                    .code(500)
                    .message("An error occurred while processing the request")
                    .build();
        }
    }

    private Order mapToOrderResponse(Order order) {
        return Order.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .username(order.getUsername())
                .restaurantId(order.getRestaurantId())
                .orderTime(order.getOrderTime())
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .orderLineItemsList(order.getOrderLineItemsList())
                .build();
    }

    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        try {
            Order order = orderRepository.findByOrderNumber(orderNumber);
            return OrderResponse.builder()
                    .code(200)
                    .message("Order with order number " + orderNumber + " retrieved")
                    .data(order)
                    .build();
        } catch (NoSuchElementException e) {
            return OrderResponse.builder()
                    .code(404)
                    .message(e.getMessage())
                    .build();
        } catch (DataAccessException e) {
            return OrderResponse.builder()
                    .code(500)
                    .message(e.getMessage())
                    .build();
        }
    }

    public OrderResponse cancelOrder(String orderNumber) {
        try {
            // Retrieve the order from the repository
            Order order = orderRepository.findByOrderNumber(orderNumber);

            // Check if the order exists
            if (order != null) {
                if(order.getOrderStatus() != Order.OrderStatus.Finished) {
                    // Set the order status to cancel
                    order.setOrderStatus(Order.OrderStatus.Cancelled);

                    // Save the updated order in the repository
                    orderRepository.save(order);

                    // Return a success response
                    return OrderResponse.builder()
                            .code(200)
                            .message("Order with order number " + orderNumber + " has been cancelled")
                            .data(order)
                            .build();
                } else {
                    // Return a bad request response
                    return OrderResponse.builder()
                            .code(400)
                            .message("Order with order number " + orderNumber + " cannot be cancelled because it has already been finished")
                            .build();
                }
            } else {
                // Return a not found response
                return OrderResponse.builder()
                        .code(404)
                        .message("Order with order number " + orderNumber + " not found")
                        .build();
            }
        } catch (DataAccessException e) {
            // Return an error response
            return OrderResponse.builder()
                    .code(500)
                    .message(e.getMessage())
                    .build();
        }
    }

    public OrderResponse changeOrderStatusToPaid(String orderNumber) {
        try {
            // Retrieve the order from the repository
            Order order = orderRepository.findByOrderNumber(orderNumber);

            // Check if the order exists
            if (order != null) {
                // Update the order status
                order.setOrderStatus(Order.OrderStatus.Paid);

                // Save the updated order in the repository
                orderRepository.save(order);

                // Return a success response
                return OrderResponse.builder()
                        .code(200)
                        .message("Order with order number " + orderNumber + " has been updated with new status: " + Order.OrderStatus.Paid)
                        .data(order)
                        .build();
            } else {
                // Return a not found response
                return OrderResponse.builder()
                        .code(404)
                        .message("Order with order number " + orderNumber + " not found")
                        .build();
            }
        } catch (DataAccessException e) {
            // Return an error response
            return OrderResponse.builder()
                    .code(500)
                    .message(e.getMessage())
                    .build();
        }

    }
}

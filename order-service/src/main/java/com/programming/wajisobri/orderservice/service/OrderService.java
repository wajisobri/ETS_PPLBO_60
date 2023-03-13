package com.programming.wajisobri.orderservice.service;

import com.programming.wajisobri.orderservice.config.RabbitMQConfig;
import com.programming.wajisobri.orderservice.dto.*;
import com.programming.wajisobri.orderservice.model.Ingredient;
import com.programming.wajisobri.orderservice.model.Order;
import com.programming.wajisobri.orderservice.model.OrderLineItems;
import com.programming.wajisobri.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    @Autowired
    private final AmqpTemplate amqpTemplate;

    @Transactional
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

        // Calculate total price of the order
        BigDecimal totalPrice = orderLineItems.stream()
                .map(item -> item.getMenuPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(totalPrice);

        // Check inventory for all required ingredients
        boolean allIngredientsInStock = checkIngredientsInStock(cafeId, orderLineItems);

        // If all ingredients are in stock, save the order
        if (allIngredientsInStock) {
            try {
                orderRepository.save(order);
                // Send payment request message to RabbitMQ
                OrderEvent orderEvent = new OrderEvent();
                orderEvent.setEventId(UUID.randomUUID().toString());
                orderEvent.setEventType(OrderEvent.EventType.Order_Created);
                HashMap<String, Object> eventData = new HashMap<>();
                eventData.put("order_number", order.getOrderNumber());
                eventData.put("username", order.getUsername());
                eventData.put("restaurant_id", order.getRestaurantId());
                eventData.put("order_time", order.getOrderTime());
                eventData.put("order_status", order.getOrderStatus());
                eventData.put("total_price", order.getTotalPrice());
                eventData.put("order_line_items", order.getOrderLineItemsList());
                orderEvent.setEventData(eventData);
                orderEvent.setEventTime(LocalDateTime.now());
                amqpTemplate.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE_NAME, RabbitMQConfig.ORDER_ROUTING_KEY, orderEvent);
                return OrderResponse.builder()
                        .code(200)
                        .message("Order placed")
                        .data(order)
                        .build();
            } catch (Exception e) {
                // Handle exception
                return OrderResponse.builder()
                        .code(500)
                        .message("Failed to place order: " + e.getMessage())
                        .build();
            }
        } else {
            return OrderResponse.builder()
                    .code(400)
                    .message("Menu is not in stock, please try again later")
                    .build();
        }
    }

    @Transactional
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
                        Ingredient ingredientCopy = new Ingredient();
                        ingredientCopy.setName(ingredient.getName());
                        ingredientCopy.setQuantity(ingredient.getQuantity() * orderLineItem.getQuantity());
                        ingredientCopy.setUnitOfMeasurement(ingredient.getUnitOfMeasurement());
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

            if (inventoryResponse != null && inventoryResponse.getData() != null && inventoryResponse.getData().getQuantity() != null) {
                BigDecimal currentQuantity = BigDecimal.valueOf(inventoryResponse.getData().getQuantity());
                BigDecimal requiredQuantity = BigDecimal.valueOf(ingredient.getQuantity());

                if (currentQuantity.compareTo(requiredQuantity) >= 0) {
                    // If the required quantity is available, reduce the inventory
                    BigDecimal newQuantity = currentQuantity.subtract(requiredQuantity);

                    IngredientResponse ingredientResponse = webClientBuilder.build()
                            .put()
                            .uri(uriBuilder -> uriBuilder
                                    .scheme("http")
                                    .host("cafe-service")
                                    .path("/api/cafe/ingredient/{cafeId}")
                                    .queryParam("ingredientName", ingredient.getName())
                                    .queryParam("reducedQuantity", newQuantity)
                                    .build(cafeId))
                            .retrieve()
                            .bodyToMono(IngredientResponse.class)
                            .block();

                    if(ingredientResponse.getCode() != 200) {
                        return false;
                    }

                } else {
                    // If the required quantity is not available, return false
                    return false;
                }
            } else {
                // If the inventory response is null, return false
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

    @Transactional
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

    @Transactional
    public void receivePaymentResponse(PaymentEvent paymentEvent) {
        log.info("Received payment response for order number: " + paymentEvent.getEventData().get("order_number"));
        if(paymentEvent.getEventType() == PaymentEvent.EventType.Payment_Created) {
            // change status order to unpaid
            Order retrievedOrder = orderRepository.findByOrderNumber(paymentEvent.getEventData().get("order_number").toString());
            retrievedOrder.setOrderStatus(Order.OrderStatus.Unpaid);
            orderRepository.save(retrievedOrder);
        } else if(paymentEvent.getEventType() == PaymentEvent.EventType.Payment_Finished) {
            // change status order to finished
            Order retrievedOrder = orderRepository.findByOrderNumber(paymentEvent.getEventData().get("order_number").toString());
            retrievedOrder.setOrderStatus(Order.OrderStatus.Paid);
            orderRepository.save(retrievedOrder);
        }
    }
}

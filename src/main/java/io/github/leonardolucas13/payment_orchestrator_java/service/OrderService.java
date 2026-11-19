package io.github.leonardolucas13.payment_orchestrator_java.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import io.github.leonardolucas13.payment_orchestrator_java.repository.OrderRepository;
import io.github.leonardolucas13.payment_orchestrator_java.model.Order;
import io.github.leonardolucas13.payment_orchestrator_java.dto.CreateOrderRequest;
import java.util.UUID;

@Service 
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setCustomerEmail(request.customerEmail());
        order.setAmountCents(request.amountCents());
        return orderRepository.save(order);
    }

    public Order getOrderById(UUID id) {
        return orderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }
}

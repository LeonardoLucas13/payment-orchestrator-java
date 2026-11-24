package io.github.leonardolucas13.payment_orchestrator_java.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.leonardolucas13.payment_orchestrator_java.model.IdempotencyKey;
import io.github.leonardolucas13.payment_orchestrator_java.model.Order;
import jakarta.validation.Valid;
import tools.jackson.databind.json.JsonMapper;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.github.leonardolucas13.payment_orchestrator_java.dto.CreateOrderRequest;
import io.github.leonardolucas13.payment_orchestrator_java.service.IdempotencyService;
import io.github.leonardolucas13.payment_orchestrator_java.service.OrderService;
import io.github.leonardolucas13.payment_orchestrator_java.util.HashUtils;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;
import java.util.Optional;



@RestController 
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final IdempotencyService idempotencyService;
    private final JsonMapper jsonMapper;
    
    public OrderController(
        OrderService orderService,
        IdempotencyService idempotencyService,
        JsonMapper jsonMapper
    ) {
        this.orderService = orderService;
        this.idempotencyService = idempotencyService;
        this.jsonMapper = jsonMapper;
    }

    @PostMapping
    public ResponseEntity<String> create(
        @RequestHeader("Idempotency-Key") String idempotencyKey,
        @Valid @RequestBody CreateOrderRequest request
    ) {
        String requestHash = HashUtils.sha256(jsonMapper.writeValueAsString(request));

        Optional<IdempotencyKey> existing = idempotencyService.reserve(idempotencyKey, requestHash);
        if (existing.isPresent()) {
            IdempotencyKey saved = existing.get();
            return ResponseEntity.status(saved.getResponseCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(saved.getResponseBody());
        }

        try {
            Order order = orderService.createOrder(request);
            String body = jsonMapper.writeValueAsString(order);
            idempotencyService.complete(idempotencyKey, HttpStatus.CREATED.value(), body);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body);
        } catch (RuntimeException e) {
            idempotencyService.release(idempotencyKey);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable UUID id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

}  

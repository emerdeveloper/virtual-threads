package com.emegonza.virtualthreads.apirest.controller;

import com.emegonza.virtualthreads.apirest.model.OrderDetails;
import com.emegonza.virtualthreads.apirest.model.OrderRequest;
import com.emegonza.virtualthreads.apirest.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{orderId}")
    public ResponseEntity<String> createOrder(
            @RequestBody OrderRequest request) {
        boolean success = orderService.createOrder(UUID.randomUUID().toString(),
                "PENDING", request.items());
        if (success) {
            return ResponseEntity.ok("Order created");
        } else {
            return ResponseEntity.badRequest().body("Invalid order create");
        }
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<String> updateOrder(
            @PathVariable String orderId,
            @RequestBody OrderRequest request) {
        boolean success = orderService.updateOrder(orderId, request.status(), request.items());
        if (success) {
            return ResponseEntity.ok("Order updated");
        } else {
            return ResponseEntity.badRequest().body("Invalid order update");
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetails> getOrderDetails(@PathVariable String orderId) {
        OrderDetails order = orderService.getOrderDetails(orderId);

        log.info("{}", Thread.currentThread());
        return ResponseEntity.ok(order);
    }

}

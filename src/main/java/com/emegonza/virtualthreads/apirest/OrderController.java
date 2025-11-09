package com.emegonza.virtualthreads.apirest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/orders", produces = MediaType.TEXT_PLAIN_VALUE)
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/{orderId}")
    public ResponseEntity<String> updateOrder(
            @PathVariable String orderId,
            @RequestBody OrderUpdateRequest request) {
        boolean success = orderService.updateOrder(orderId, request.status(), request.items());
        if (success) {
            return ResponseEntity.ok("Order updated");
        } else {
            return ResponseEntity.badRequest().body("Invalid order update");
        }
    }
}

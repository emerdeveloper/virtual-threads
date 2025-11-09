package com.emegonza.virtualthreads.apirest;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderService {

    private final Map<String, OrderDetails> orders = new ConcurrentHashMap<>();
    //ConcurrentHashMap to store locks
    private final ConcurrentHashMap<String, Object> orderLocks = new ConcurrentHashMap<>();
    private final DatabaseService dbService;

    public OrderService(DatabaseService dbService) {
        this.dbService = dbService;
    }

    public boolean updateOrder(String orderId, String status, List<OrderItems> items) {
        // Get or create a lock object specific to this orderId
        // computeIfAbsent is thread-safe and ensures only one lock object is created per ID
        Object lock = orderLocks.computeIfAbsent(orderId, k -> new Object());

        synchronized (lock) {
            OrderDetails currentOrder = orders.get(orderId);
            if (currentOrder == null)
                return false;

            // This simulates a slow database write that blocks
            OrderDetails orderUpdated = new OrderDetails(orderId, status, items);
            dbService.persistOrdersChange(orderId, status, orderUpdated);
            // update in-memory order
            orders.put(orderId, currentOrder);
            return true;
        }
    }

    public boolean createOrder(String orderId, String status, List<OrderItems> items) {
        // Get or create a lock object specific to this orderId
        // computeIfAbsent is thread-safe and ensures only one lock object is created per ID
        Object lock = orderLocks.computeIfAbsent(orderId, k -> new Object());

        synchronized (lock) {
            if (orders.containsKey(orderId))
                return false;

            // This simulates a slow database write that blocks
            OrderDetails newOrder = new OrderDetails(orderId, status, items);
            dbService.persistOrdersChange(orderId, status, newOrder);
            // update in-memory order
            orders.put(orderId, newOrder);
            return true;
        }
    }

    public OrderDetails getOrderDetails(String orderId) {
        dbService.getOrder(orderId);
        return orders.get(orderId);
    }
}

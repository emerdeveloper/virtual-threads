package com.emegonza.virtualthreads.apirest;

import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

    public void persistOrdersChange(String orderId, String status, OrderDetails items) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // This simulates a slow database get that blocks by 3 seconds
    public void getOrder(String orderId) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

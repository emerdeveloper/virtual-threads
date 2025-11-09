package com.emegonza.virtualthreads.apirest;

import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

    public void persistOrdersChange(OrderUpdateRequest order) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

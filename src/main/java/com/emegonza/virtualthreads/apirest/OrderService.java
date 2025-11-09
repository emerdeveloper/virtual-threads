package com.emegonza.virtualthreads.apirest;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    public boolean updateOrder(String orderId, String status, List<OrderItems> items) {
        return true;
    }
}

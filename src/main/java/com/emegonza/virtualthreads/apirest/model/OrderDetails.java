package com.emegonza.virtualthreads.apirest.model;

import java.util.List;

public record OrderDetails(
        String orderId,
        String status,
        List<OrderItems> items
) { }

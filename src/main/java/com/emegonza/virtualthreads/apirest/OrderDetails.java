package com.emegonza.virtualthreads.apirest;

import java.util.List;

public record OrderDetails(
        String orderId,
        String status,
        List<OrderItems> items
) { }

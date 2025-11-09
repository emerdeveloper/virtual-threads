package com.emegonza.virtualthreads.apirest;

import java.util.List;

public record OrderUpdateRequest(
        String status,
        List<OrderItems> items
) { }

record OrderItems(
        int quantity,
        String productId
) { }
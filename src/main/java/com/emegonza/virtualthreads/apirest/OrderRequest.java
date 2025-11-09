package com.emegonza.virtualthreads.apirest;

import java.util.List;

public record OrderUpdateRequest(
        String status,
        List<OrderDetails> items
) { }
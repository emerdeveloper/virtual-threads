package com.emegonza.virtualthreads.apirest;

public record OrderDetails(
        int quantity,
        String productId
) { }
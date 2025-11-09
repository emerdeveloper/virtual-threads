package com.emegonza.virtualthreads.apirest;

public record OrderItems(
        int quantity,
        String productId
) { }
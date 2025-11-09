package com.emegonza.virtualthreads.apirest.model;

public record OrderItems(
        int quantity,
        String productId
) { }
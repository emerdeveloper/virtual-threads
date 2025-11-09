package com.emegonza.virtualthreads.apirest.model;

import java.util.List;

public record OrderRequest(
        String status,
        List<OrderItems> items
) { }
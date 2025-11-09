package com.emegonza.virtualthreads.apirest;

import java.util.List;

public record OrderRequest(
        String status,
        List<OrderItems> items
) { }
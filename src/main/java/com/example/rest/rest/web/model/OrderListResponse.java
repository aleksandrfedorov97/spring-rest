package com.example.rest.rest.web.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderListResponse {
    List<OrderResponse> orders = new ArrayList<>();
}

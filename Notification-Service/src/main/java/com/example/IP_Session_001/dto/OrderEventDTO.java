package com.example.IP_Session_001.dto;

import lombok.Data;

@Data
public class OrderEventDTO {
    private Long id;
    private Long clientId;
    private Long productId;
    private Integer quantity;
    private Double totalPrice;
}

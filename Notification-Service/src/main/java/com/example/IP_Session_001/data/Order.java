package com.example.IP_Session_001.data;

import lombok.Data;
@Data
public class Order {
    private Long id;
    private Integer clientId;
    private Integer productId;
    private Integer quantity;
    private Double totalPrice;
}

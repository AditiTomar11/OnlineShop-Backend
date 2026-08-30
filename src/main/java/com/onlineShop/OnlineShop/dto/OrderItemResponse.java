package com.onlineShop.OnlineShop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class OrderItemResponse {
    private String productName;
    private Integer quantity;
    private BigDecimal priceAtPurchase;
}
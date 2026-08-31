package com.onlineShop.OnlineShop.dto;

import lombok.Data;

@Data
public class UpdateRoleRequest {
    private String role;   // "SUPERADMIN", "ADMIN", "CUSTOMER"
}
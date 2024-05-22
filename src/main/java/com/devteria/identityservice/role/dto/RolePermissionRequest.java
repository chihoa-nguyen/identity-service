package com.devteria.identityservice.role.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RolePermissionRequest {
    @NotNull(message = "Role name is not null")
    @NotBlank(message = "Role name is required")
    private String name;
    private Set<String> permissions;
}

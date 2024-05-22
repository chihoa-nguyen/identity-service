package com.devteria.identityservice.role.dto;

import com.devteria.identityservice.permission.Permission;
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

public class RoleRequest {
    @NotNull(message = "Role name is not null")
    @NotBlank(message = "Role name is required")
    private String name;
    @NotNull(message = "Role description is not null")
    @NotBlank(message = "Role description is required")
    private String description;
    @NotNull(message = "Permission list is not null")
    Set<String> permissions;
}

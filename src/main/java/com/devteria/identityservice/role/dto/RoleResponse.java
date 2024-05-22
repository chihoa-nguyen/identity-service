package com.devteria.identityservice.role.dto;

import com.devteria.identityservice.permission.Permission;
import com.devteria.identityservice.permission.dto.PermissionResponse;
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
public class RoleResponse {
    private String name;
    private String description;
    Set<PermissionResponse> permissions;
}

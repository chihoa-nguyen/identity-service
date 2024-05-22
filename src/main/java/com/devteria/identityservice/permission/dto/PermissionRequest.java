package com.devteria.identityservice.permission.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermissionRequest {
    @NotNull(message = "Permission name is not null")
    @NotBlank(message = "Permission name is required")
    private String name;
    @NotNull(message = "Permission description is not null")
    @NotBlank(message = "Permission description is required")
    private String description;
}

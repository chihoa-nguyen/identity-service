package com.devteria.identityservice.invalidatedtoken.dto;

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
public class RefreshRequest {
    @NotNull(message = "Token is required")
    @NotBlank(message ="Token is not empty")
    private String token;
}

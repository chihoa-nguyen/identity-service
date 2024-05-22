package com.devteria.identityservice.auth;

import com.devteria.identityservice.auth.dto.AuthResponse;
import com.devteria.identityservice.auth.dto.IntrospectResponse;
import com.devteria.identityservice.invalidatedtoken.dto.LogoutRequest;
import com.devteria.identityservice.invalidatedtoken.dto.RefreshRequest;
import com.devteria.identityservice.utils.ApiResponse;
import com.devteria.identityservice.auth.dto.AuthRequest;
import com.devteria.identityservice.auth.dto.IntrospectRequest;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;

    @PostMapping("/login")
    ApiResponse<AuthResponse> authenticate(@RequestBody @Valid AuthRequest request) throws JOSEException {
        var result = authService.authenticate(request);
        return ApiResponse.<AuthResponse>builder()
                .result(result)
                .build();
    }
    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody @Valid IntrospectRequest request) throws JOSEException, ParseException {
        var result = authService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody @Valid LogoutRequest request) throws JOSEException, ParseException {
        authService.logout(request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthResponse> refresh(@RequestBody @Valid RefreshRequest request) throws JOSEException, ParseException {
        var result = authService.refreshToken(request);
        return ApiResponse.<AuthResponse>builder()
                .result(result)
                .build();
    }
}

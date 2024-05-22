package com.devteria.identityservice.auth;

import com.devteria.identityservice.auth.dto.AuthRequest;
import com.devteria.identityservice.auth.dto.AuthResponse;
import com.devteria.identityservice.auth.dto.IntrospectRequest;
import com.devteria.identityservice.auth.dto.IntrospectResponse;
import com.devteria.identityservice.exception.AppException;
import com.devteria.identityservice.exception.ErrorCode;
import com.devteria.identityservice.invalidatedtoken.InvalidatedToken;
import com.devteria.identityservice.invalidatedtoken.InvalidatedTokenRepo;
import com.devteria.identityservice.invalidatedtoken.dto.LogoutRequest;
import com.devteria.identityservice.invalidatedtoken.dto.RefreshRequest;
import com.devteria.identityservice.user.User;
import com.devteria.identityservice.user.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final InvalidatedTokenRepo invalidatedTokenRepository;
    @NonFinal
    @Value("${jwt.signer-key}")
    private String SIGNER_KEY;
    @NonFinal
    @Value("${jwt.valid-duration}")
    private Long VALID_DURATION;
    @NonFinal
    @Value("${jwt.refreshable-duration}")
    private Long REFRESHABLE_DURATION;

    public AuthResponse authenticate(AuthRequest request) throws JOSEException {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        boolean authenticated = isCorrectPassword(request.getPassword(), user.getPassword());
        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);
        String token = generateToken(user,false);
        return AuthResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }
    private Boolean isCorrectPassword(String passwordInput, String password){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(passwordInput, password);
        return authenticated;
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var context = SecurityContextHolder.getContext();
        log.info(context.getAuthentication().toString());
        String token = request.getToken();
        boolean isValid = verifyToken(token);
        return IntrospectResponse.builder()
                .isValid(isValid)
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        disableToken(request.getToken());
    }

    public AuthResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        if (verifyToken(request.getToken()))
            disableToken(request.getToken());
        return getNewJwt(request.getToken());
    }

    private void disableToken(String token) throws ParseException {
        SignedJWT jwtToken = getSignedJWT(token);
        String jit = jwtToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = jwtToken.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
    }

    private AuthResponse getNewJwt(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        String username = signedJWT.getJWTClaimsSet().getSubject();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String newToken = generateToken(user, true);
        return AuthResponse.builder()
                .token(newToken)
                .authenticated(true)
                .build();
    }

    private SignedJWT getSignedJWT(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT;
    }

    private Boolean verifyToken(String token) throws JOSEException, ParseException {
        SignedJWT signedJWT = getSignedJWT(token);
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var isVerified = signedJWT.verify(verifier);
        if (!isVerified && expiryTime.after(new Date()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        return !isInvalidatedToken(signedJWT);
    }

    private Boolean isInvalidatedToken(SignedJWT signedJWT) throws ParseException {
        return invalidatedTokenRepository
                .existsById(signedJWT.getJWTClaimsSet().getJWTID());
    }

    private String generateToken(User user, Boolean isRefresh) throws JOSEException {
        Long expirationTime = isRefresh
                ? REFRESHABLE_DURATION
                : VALID_DURATION;
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("devteria.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(expirationTime, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
        return jwsObject.serialize();
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
                }
            });
        }
        return stringJoiner.toString();
    }
}

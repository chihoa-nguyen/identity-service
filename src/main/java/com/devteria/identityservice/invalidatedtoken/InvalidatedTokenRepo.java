package com.devteria.identityservice.invalidatedtoken;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidatedTokenRepo extends JpaRepository<InvalidatedToken, String> {

}

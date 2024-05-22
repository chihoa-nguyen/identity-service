package com.devteria.identityservice.permission;

import com.devteria.identityservice.permission.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, String> {

    Boolean existsByName(String name);
}

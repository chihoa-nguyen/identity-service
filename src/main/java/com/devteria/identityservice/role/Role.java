package com.devteria.identityservice.role;

import com.devteria.identityservice.permission.Permission;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role {
    @Id
    private String name;
    private String description;
    @ManyToMany(fetch = FetchType.EAGER)
    Set<Permission> permissions;
}

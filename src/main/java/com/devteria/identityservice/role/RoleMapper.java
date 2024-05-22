package com.devteria.identityservice.role;

import com.devteria.identityservice.permission.PermissionMapper;
import com.devteria.identityservice.role.dto.RoleRequest;
import com.devteria.identityservice.role.dto.RoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = PermissionMapper.class)
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    @Mapping(source = "permissions", target = "permissions")
    RoleResponse toRoleResponse(Role role);
//    @Mapping(target = "name", ignore = true)
//    void updateRole(@MappingTarget Role role,RoleRequest request);
}

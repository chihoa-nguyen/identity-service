package com.devteria.identityservice.user;

import com.devteria.identityservice.role.RoleMapper;
import com.devteria.identityservice.user.dto.UserCreationRequest;
import com.devteria.identityservice.user.dto.UserUpdateRequest;
import com.devteria.identityservice.user.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {
    User toUser(UserCreationRequest request);
    @Mapping(source = "roles", target = "roles")
    UserResponse toUserResponse(User user);
    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}

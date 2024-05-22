package com.devteria.identityservice.role;

import com.devteria.identityservice.exception.AppException;
import com.devteria.identityservice.exception.ErrorCode;
import com.devteria.identityservice.permission.Permission;
import com.devteria.identityservice.permission.PermissionRepository;
import com.devteria.identityservice.role.dto.RolePermissionRequest;
import com.devteria.identityservice.role.dto.RoleRequest;
import com.devteria.identityservice.role.dto.RoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;
    public RoleResponse createRole(RoleRequest request){
        if(roleRepository.existsById(request.getName())){
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }
        Role role = roleMapper.toRole(request);
        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }
    public RoleResponse getRole(String name){
        return roleMapper.toRoleResponse(roleRepository.findById(name)
               .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED)));
    }
    public List<RoleResponse> getAllRole(){
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }
//    public RoleResponse updateRole(RoleRequest request){
//        Role role = roleRepository.findById(request.getName())
//               .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
//        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
//        roleMapper.updateRole(role, permissions);
//        return roleMapper.toRoleResponse(roleRepository.save(role));
//    }
    public RoleResponse addPermissions(RolePermissionRequest request){
        Role role = roleRepository.findById(request.getName())
               .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
        role.getPermissions().addAll(permissions);
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }
    public RoleResponse removePermissions(RolePermissionRequest request){
        Role role = roleRepository.findById(request.getName())
               .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
        role.getPermissions().removeAll(permissions);
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }
    public void deleteRole(String name){
        roleRepository.deleteById(name);
    }
}

package com.devteria.identityservice.permission;

import com.devteria.identityservice.exception.AppException;
import com.devteria.identityservice.exception.ErrorCode;
import com.devteria.identityservice.permission.dto.PermissionRequest;
import com.devteria.identityservice.permission.dto.PermissionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;
    public PermissionResponse createPermission(PermissionRequest request){
        if(permissionRepository.existsByName(request.getName())){
            throw new AppException(ErrorCode.PERMISSION_EXISTED);
        }
        Permission permission = permissionMapper.toPermission(request);
        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }
    public List<PermissionResponse> getAllPermission(){
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream()
                .map(permissionMapper::toPermissionResponse).toList();
    }
    public PermissionResponse updatePermission(PermissionRequest request){
        Permission permission = permissionRepository.findById(request.getName())
                .orElseThrow(()-> new AppException(ErrorCode.PERMISSION_NOT_EXISTED));
        permissionMapper.updatePermission(permission, request);
        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }
    public void deletePermission(String name){
        permissionRepository.deleteById(name);
    }
}

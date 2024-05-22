package com.devteria.identityservice.role;

import com.devteria.identityservice.role.dto.RolePermissionRequest;
import com.devteria.identityservice.role.dto.RoleRequest;
import com.devteria.identityservice.role.dto.RoleResponse;
import com.devteria.identityservice.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;
    @PostMapping()
    ApiResponse<RoleResponse> create(@RequestBody @Valid RoleRequest request){
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.createRole(request))
                .build();
    }
    @GetMapping()
    ApiResponse<List<RoleResponse>> getAll(){
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAllRole())
                .build();
    }

    @GetMapping("/{name}")
    ApiResponse<RoleResponse> getById(@PathVariable String name){
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.getRole(name))
                .build();
    }
    @PostMapping("/add")
    ApiResponse<RoleResponse> addPermissions(@RequestBody @Valid RolePermissionRequest request){
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.addPermissions(request))
                .build();
    }
    @PostMapping("/remove")
    ApiResponse<RoleResponse> removePermissions(@RequestBody @Valid RolePermissionRequest request){
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.removePermissions(request))
                .build();
    }
    @DeleteMapping("/{name}")
    ApiResponse<Void> delete(@PathVariable String name){
        roleService.deleteRole(name);
        return ApiResponse.<Void>builder().build();
    }
}

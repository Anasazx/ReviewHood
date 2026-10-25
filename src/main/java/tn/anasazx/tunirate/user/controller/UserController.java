package tn.anasazx.tunirate.user.controller;

import tn.anasazx.tunirate.user.dto.ChangePasswordRequest;
import tn.anasazx.tunirate.user.dto.UserStatusRequest;
import tn.anasazx.tunirate.user.dto.AdminUserResponse;
import tn.anasazx.tunirate.enums.UserStatus;
import tn.anasazx.tunirate.enums.GlobalRole;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.anasazx.tunirate.security.SecurityUtils;
import tn.anasazx.tunirate.user.dto.AdminUpdateUserRequest;
import tn.anasazx.tunirate.user.dto.UpdateUserRequest;
import tn.anasazx.tunirate.user.dto.UserResponse;
import tn.anasazx.tunirate.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Get user by ID
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // Get all users
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


    // Update user profile
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody AdminUpdateUserRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(userService.updateUser(id, request, currentUserId));
    }

    // Paginated, filterable users table for the admin panel
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/op")
    public ResponseEntity<Page<AdminUserResponse>> getUsersAsAdmin(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) GlobalRole role,
            @RequestParam(required = false) UserStatus status,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(userService.getUsersAsAdmin(q, role, status, pageable));
    }

    // Suspend or reactivate an account; suspended users are rejected on every request
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<AdminUserResponse> updateUserStatus(@PathVariable Long id, @Valid @RequestBody UserStatusRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(userService.updateUserStatus(id, request.status(), currentUserId));
    }

    @PostMapping("/me/avatar")
    public UserResponse uploadAvatar(@RequestParam("file") MultipartFile file) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return userService.uploadAvatar(file, currentUserId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsers(@RequestParam String q) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(userService.searchUsers(q, currentUserId));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(userService.getCurrentUser(currentUserId));
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        userService.changePassword(request, currentUserId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMyProfile(@RequestBody UpdateUserRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(userService.updateMyProfile(request, currentUserId));
    }
}
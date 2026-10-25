package tn.anasazx.tunirate.user.service;

import tn.anasazx.tunirate.user.dto.AdminUserResponse;
import tn.anasazx.tunirate.enums.UserStatus;
import tn.anasazx.tunirate.enums.GlobalRole;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import tn.anasazx.tunirate.user.dto.UpdateUserRequest;
import tn.anasazx.tunirate.user.dto.UserResponse;
import tn.anasazx.tunirate.user.dto.AdminUpdateUserRequest;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
    UserResponse updateUser(Long id, AdminUpdateUserRequest request, Long currentUserId);
    UserResponse updateMyProfile(UpdateUserRequest request, Long currentUserId);
    List<UserResponse> searchUsers(String query, Long currentUserId);
    UserResponse getCurrentUser(Long currentUserId);

    Page<AdminUserResponse> getUsersAsAdmin(String q, GlobalRole role, UserStatus status, Pageable pageable);

    AdminUserResponse updateUserStatus(Long id, UserStatus status, Long currentUserId);
    UserResponse uploadAvatar(MultipartFile file, Long currentUserId);
}
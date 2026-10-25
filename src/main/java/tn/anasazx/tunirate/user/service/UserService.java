package tn.anasazx.tunirate.user.service;

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
    UserResponse uploadAvatar(MultipartFile file, Long currentUserId);
}
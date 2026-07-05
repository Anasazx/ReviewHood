package tn.anasazx.tunirate.user.service;

import org.springframework.web.multipart.MultipartFile;
import tn.anasazx.tunirate.user.dto.UserResponse;
import tn.anasazx.tunirate.user.entity.User;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
    UserResponse updateUser(Long id, User updatedUser);
    UserResponse createUser(User user);
    List<UserResponse> searchUsers(String query);
    UserResponse getCurrentUser();

    UserResponse uploadAvatar(MultipartFile file);


}
package tn.anasazx.tunirate.user.service;

import tn.anasazx.tunirate.user.dto.UserResponse;
import tn.anasazx.tunirate.user.entity.User;

import java.util.List;

public interface UserService {
    //TODO: i need to implement user DTOs here
    //  the return type should be user response dto
    //  the params needs to be user request dto
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
    UserResponse getUserByEmail(String email);
    UserResponse updateUser(Long id, User updatedUser);
    UserResponse createUser(User user);
    List<UserResponse> searchUsers(String query);

}
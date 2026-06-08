package tn.anasazx.tunirate.user.service;

import tn.anasazx.tunirate.user.dto.UserResponseDto;
import tn.anasazx.tunirate.user.entity.User;

import java.util.List;

public interface UserService {
    //TODO: i need to implement user DTOs here
    //  the return type should be user response dto
    //  the params needs to be user request dto
    List<UserResponseDto> getAllUsers();
    User getUserById(Long id);
    User getUserByEmail(String email);
    User updateUser(Long id, User updatedUser);
    User createUser(User user);

    User getUserEntityById(Long id);

}
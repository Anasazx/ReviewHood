package tn.anasazx.tunirate.user.service;

import tn.anasazx.tunirate.user.entity.User;

public interface UserService {

    User getUserById(Long id);

    User getUserByEmail(String email);

    User updateUser(Long id, User updatedUser);

}
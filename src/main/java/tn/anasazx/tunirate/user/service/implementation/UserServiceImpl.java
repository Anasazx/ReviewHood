package tn.anasazx.tunirate.user.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.repository.UserRepository;
import tn.anasazx.tunirate.user.service.UserService;

@Service

@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override

    public User getUserById(Long id) {

        return userRepository.findById(id)

                .orElseThrow(() -> new RuntimeException("User not found"));

    }

    @Override

    public User getUserByEmail(String email) {

        return userRepository.findByEmail(email)

                .orElseThrow(() -> new RuntimeException("User not found"));

    }

    @Override

    public User updateUser(Long id, User updatedUser) {

        User existingUser = getUserById(id);

        // update allowed fields only

        existingUser.setName(updatedUser.getName());

        return userRepository.save(existingUser);

    }

}
package tn.anasazx.tunirate.user.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.anasazx.tunirate.user.dto.UserResponse;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.mapper.UserMapper;
import tn.anasazx.tunirate.user.repository.UserRepository;
import tn.anasazx.tunirate.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse getUserById(Long id) {
        return UserMapper.toResponse(userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"))
        );
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        return UserMapper.toResponse(userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"))
        );
    }

    @Override
    public UserResponse updateUser(Long id, User updatedUser) {

        User existingUser = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // update allowed fields only
        existingUser.setName(updatedUser.getName());

        User savedUser = userRepository.save(existingUser);

        return UserMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse createUser(User user) {

        //Check if user exists
        if (userRepository.existsUserByEmailOrName(user.getEmail(), user.getName())) {
            throw new RuntimeException("Email or name already exists");
        }

        return UserMapper.toResponse(userRepository.save(user));
    }


    @Override
    public List<UserResponse> searchUsers(String query) {
        return userRepository
                .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query)
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }


}
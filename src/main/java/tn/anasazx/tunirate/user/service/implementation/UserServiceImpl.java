package tn.anasazx.tunirate.user.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.anasazx.tunirate.fileStorage.service.FileStorageService;
import tn.anasazx.tunirate.user.dto.UpdateUserRequest;
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
    private final FileStorageService fileStorageService;


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
    public UserResponse updateUser(Long id, User updatedUser) {

        User existingUser = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // update allowed fields only
        existingUser.setName(updatedUser.getName());

        User savedUser = userRepository.save(existingUser);

        return UserMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse updateMyProfile(UpdateUserRequest request, Long currentUserId) {

        User existingUser = userRepository.findById(currentUserId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Update country
        existingUser.setCountry(request.country());

        // Update email only if it changed
        if (!existingUser.getEmail().equals(request.email())) {
            existingUser.setEmail(request.email());
            existingUser.setEmailVerified(false);

            // TODO: Send verification email
        }

        // Update phone
        existingUser.setPhoneNumber(request.phoneNumber());

        User savedUser = userRepository.save(existingUser);

        return UserMapper.toResponse(savedUser);
    }

    @Override
    public List<UserResponse> searchUsers(String query, Long currentUserId) {
        return userRepository
                .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query)
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse getCurrentUser(Long currentUserId) {
        return UserMapper.toResponse(
                userRepository.findById(currentUserId)
                        .orElseThrow(
                                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                        )
        );

    }

    @Override
    public UserResponse uploadAvatar(MultipartFile file, Long currentUserId) {

        User currentUser = userRepository.findById(currentUserId).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                );


        String fileName = fileStorageService.saveFile(file);

        if (currentUser.getAvatarUrl() != null) {
            fileStorageService.deleteFile(currentUser.getAvatarUrl());
        }

        currentUser.setAvatarUrl(fileName);

        return UserMapper.toResponse(userRepository.save(currentUser));

    }

}
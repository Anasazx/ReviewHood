package tn.anasazx.tunirate.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.enums.GlobalRole;
import tn.anasazx.tunirate.exception.NoAuthenticatedUserException;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.repository.UserRepository;

@Component
public class SecurityUtils {

    private final UserRepository userRepository;


    public SecurityUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NoAuthenticatedUserException();
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof Long userId) {
            System.out.println("Current user id: " + userId);
            return userId;
        }
        if (principal instanceof User user) {
            System.out.println("Current user: " + user);
            return user.getId();
        }
        throw new NoAuthenticatedUserException();
    }

    public boolean isCurrentUserAdmin() {

        Long currentUserId = getCurrentUserId();

        User user = userRepository.findById(currentUserId).orElseThrow(NoAuthenticatedUserException::new);

        return user.getGlobalRole() == GlobalRole.ADMIN;
    }

}



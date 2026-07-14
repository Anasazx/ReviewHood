package tn.anasazx.tunirate.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import tn.anasazx.tunirate.enums.GlobalRole;
import tn.anasazx.tunirate.exception.NoAuthenticatedUserException;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.repository.UserRepository;

@Component
public class SecurityUtils {

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

    public static Long getCurrentUserIdOrNull() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof Long userId) {
            return userId;
        }
        if (principal instanceof User user) {
            return user.getId();
        }
        return null;
    }


}



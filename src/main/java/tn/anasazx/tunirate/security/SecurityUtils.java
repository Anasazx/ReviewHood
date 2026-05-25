package tn.anasazx.tunirate.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import tn.anasazx.tunirate.exception.NoAuthenticatedUserException;
import tn.anasazx.tunirate.user.entity.User;

public class SecurityUtils {


    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NoAuthenticatedUserException();
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            return user.getId();
        }
        throw new NoAuthenticatedUserException();
    }

}



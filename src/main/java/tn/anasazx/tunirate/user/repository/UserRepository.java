package tn.anasazx.tunirate.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.anasazx.tunirate.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByGoogleId(String googleId);

    Optional<User> findFirstById (Long id);

    boolean existsUserByEmail(String email);

    //This is added to perform user search
    List<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);


}

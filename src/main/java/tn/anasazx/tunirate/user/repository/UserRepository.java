package tn.anasazx.tunirate.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.anasazx.tunirate.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsUserByEmailOrName(String email, String name);
}

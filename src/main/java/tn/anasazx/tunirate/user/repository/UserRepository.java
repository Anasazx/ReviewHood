package tn.anasazx.tunirate.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.anasazx.tunirate.enums.GlobalRole;
import tn.anasazx.tunirate.enums.UserStatus;
import tn.anasazx.tunirate.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByGoogleId(String googleId);

    Optional<User> findFirstById (Long id);

    boolean existsUserByEmail(String email);

    // Current role of an active user, read on every authenticated request so role changes apply immediately
    @Query("SELECT u.globalRole FROM User u WHERE u.id = :id AND u.status = :status")
    Optional<GlobalRole> findGlobalRoleByIdAndStatus(@Param("id") Long id, @Param("status") UserStatus status);

    //This is added to perform user search
    List<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);


    @Query(value = """
    SELECT CAST(created_at AS date) AS day, COUNT(*) AS count
    FROM users
    WHERE created_at >= :startDate
    GROUP BY CAST(created_at AS date)
    ORDER BY day
    """, nativeQuery = true)
    List<Object[]> countUsersByDayRaw(@Param("startDate") LocalDateTime startDate);

}

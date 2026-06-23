package tn.anasazx.tunirate.user.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.anasazx.tunirate.actor.entity.Actor;
import tn.anasazx.tunirate.enums.GlobalRole;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email")
        }
)
@DiscriminatorValue("USER")
public class User extends Actor {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GlobalRole globalRole;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public User(String name, String email, String password, GlobalRole globalRole) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.globalRole = globalRole;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
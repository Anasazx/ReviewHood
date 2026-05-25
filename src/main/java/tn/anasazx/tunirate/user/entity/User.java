package tn.anasazx.tunirate.user.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.anasazx.tunirate.enums.GlobalRole;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email")
        }
)
public class User {
    public User(String name, String email, String password, GlobalRole globalRole){
        this.name = name;
        this.email = email;
        this.password = password;
        this.globalRole = globalRole;
    }
    //TODO: need to change the generation type in the production/future
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
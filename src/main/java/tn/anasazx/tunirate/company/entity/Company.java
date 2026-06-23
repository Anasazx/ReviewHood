package tn.anasazx.tunirate.company.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.anasazx.tunirate.actor.entity.Actor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "companies",
        indexes = {
                @Index(name = "idx_company_name", columnList = "name")
        }
)
@DiscriminatorValue("COMPANY")
public class Company extends Actor {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Boolean verified = false;

    @Column
    private String logoUrl;

    @Column
    private String bannerUrl;

}
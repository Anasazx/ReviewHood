package tn.anasazx.tunirate.company.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.anasazx.tunirate.actor.entity.Actor;
import tn.anasazx.tunirate.membership.entity.CompanyMember;
import tn.anasazx.tunirate.product.entity.Product;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<CompanyMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Product> Products = new ArrayList<>();


}
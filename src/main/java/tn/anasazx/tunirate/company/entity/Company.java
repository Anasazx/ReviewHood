package tn.anasazx.tunirate.company.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.anasazx.tunirate.CompanySocialLink.entity.CompanySocialLink;
import tn.anasazx.tunirate.actor.entity.Actor;
import tn.anasazx.tunirate.enums.CompanyStatus;
import tn.anasazx.tunirate.enums.Country;
import tn.anasazx.tunirate.enums.Industry;
import tn.anasazx.tunirate.membership.entity.CompanyMember;
import tn.anasazx.tunirate.product.entity.Product;
import tn.anasazx.tunirate.user.entity.User;

import java.time.LocalDateTime;
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
                @Index(name = "idx_company_name", columnList = "name"),
                @Index(name = "idx_company_status", columnList = "status"),
                @Index(name = "idx_company_country", columnList = "country")
        }
)
@DiscriminatorValue("COMPANY")
public class Company extends Actor {

    @Column(nullable = false, unique = true, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String logoUrl;

    @Column
    private String bannerUrl;

    @Column(length = 30)
    private String phoneNumber;

    @Column(length = 500)
    private String websiteUrl;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompanySocialLink> socialLinks = new ArrayList<>();

    @Column
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Country country;

    @Enumerated(EnumType.STRING)
    private Industry industry;

    @Enumerated(EnumType.STRING)
    private CompanyStatus status = CompanyStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by")
    private User verifiedBy;

    @Column
    private LocalDateTime verifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<CompanyMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @Column
    private LocalDateTime updatedAt;


    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
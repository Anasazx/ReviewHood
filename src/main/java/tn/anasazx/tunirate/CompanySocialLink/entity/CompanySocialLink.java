package tn.anasazx.tunirate.CompanySocialLink.entity;

import jakarta.persistence.*;
import lombok.Data;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.enums.SocialPlatform;

@Entity
@Table(name = "company_social_links")
@Data
public class CompanySocialLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialPlatform platform;

    @Column(nullable = false, length = 500)
    private String url;

}
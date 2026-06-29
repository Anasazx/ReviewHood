package tn.anasazx.tunirate.product.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.enums.ProductStatus;
import tn.anasazx.tunirate.subcategory.entity.Subcategory;
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
        name = "products",
        indexes = {
                @Index(name = "idx_product_name", columnList = "name"),
                @Index(name = "idx_company_id", columnList = "company_id")
        }
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;


    //I d'ont need a category here bcs a subcategory already belongs to a category
    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    // after some analyzing, I think the best option is to make the subcategory optional, where you can make a product then assign the subcategory to it
    private Subcategory subcategory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;


    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status = ProductStatus.DRAFT;


    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
package tn.anasazx.tunirate.product.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.anasazx.tunirate.subcategory.entity.Subcategory;

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


    //I d'ont need a category here bcs a subcategory is already belong to a category
    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    // after some analyzing I think the best option is to make the subcategory optional , where u can make a product then assign the subcategory to it
    private Subcategory subcategory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private tn.anasazx.tunirate.company.entity.Company company;




    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
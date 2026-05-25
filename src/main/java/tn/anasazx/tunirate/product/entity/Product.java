package tn.anasazx.tunirate.product.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.anasazx.tunirate.categories.category.entity.Category;
import tn.anasazx.tunirate.categories.subcategory.entity.Subcategory;

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
    // TODO: make nullable true, im setting it false for now for dev, because i dont want to drop my test db for now ,,, @JoinColumn(name = "subcategory_id", nullable = false)
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
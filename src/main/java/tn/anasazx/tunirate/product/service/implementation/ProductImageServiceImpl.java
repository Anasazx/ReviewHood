package tn.anasazx.tunirate.product.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.anasazx.tunirate.product.entity.Product;
import tn.anasazx.tunirate.product.entity.ProductImage;
import tn.anasazx.tunirate.product.repository.ProductImageRepository;
import tn.anasazx.tunirate.product.repository.ProductRepository;
import tn.anasazx.tunirate.product.service.ProductImageService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    @Override

    public ProductImage addImage(Long productId, String url) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        ProductImage image = new ProductImage();
        image.setUrl(url);
        image.setProduct(product);
        return productImageRepository.save(image);
    }

    @Override
    public void deleteImage(Long imageId) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        productImageRepository.delete(image);
    }

    @Override
    public void setMainImage(Long productId, Long imageId) {
        List<ProductImage> images = productImageRepository.findByProductId(productId);
        if (images.isEmpty()) {
            throw new RuntimeException("No images found for this product");
        }
        for (ProductImage img : images) {
            img.setMain(false);
        }
        ProductImage selected = images.stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Image not found in this product"));
        selected.setMain(true);
    }

    @Override
    public List<ProductImage> getImagesByProductId(Long productId) {
        return productImageRepository.findByProductId(productId);
    }
}
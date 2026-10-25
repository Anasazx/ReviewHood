package tn.anasazx.tunirate.product.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tn.anasazx.tunirate.fileStorage.service.FileStorageService;
import tn.anasazx.tunirate.product.dto.ProductImageResponse;
import tn.anasazx.tunirate.product.entity.Product;
import tn.anasazx.tunirate.product.entity.ProductImage;
import tn.anasazx.tunirate.product.mapper.ProductImageMapper;
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
    private final FileStorageService fileStorageService;

    @Override
    public ProductImageResponse addImage(Long productId, MultipartFile file) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 1. save the file on disk
        String fileName = fileStorageService.saveFile(file);

        // 2. create DB entity
        ProductImage image = new ProductImage();
        image.setUrl(fileName);
        image.setProduct(product);

        // 3. first image = main
        boolean hasImages = productImageRepository.existsByProductId(productId);
        image.setMain(!hasImages);

        // 4. save
        return ProductImageMapper.toResponse(productImageRepository.save(image));
    }

    @Override
    public void deleteImage(Long imageId) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));

        Product product = image.getProduct();
        product.removeImage(image);

        // Keep a main image as long as the product has images
        if (image.isMain()) {
            product.getImages().stream().findFirst().ifPresent(next -> next.setMain(true));
        }

        productRepository.save(product);
    }

    @Override
    public void setMainImage(Long productId, Long imageId) {

        ProductImage selected = productImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        if (selected.getProduct() == null ||
                !selected.getProduct().getId().equals(productId)) {
            throw new RuntimeException("Image does not belong to this product");
        }

        productImageRepository.clearMainImages(productId);

        selected.setMain(true);
        productImageRepository.save(selected);
    }

    @Override
    public List<ProductImageResponse> getImagesByProductId(Long productId) {
        return ProductImageMapper.toResponseList(productImageRepository.findByProductId(productId));
    }

}
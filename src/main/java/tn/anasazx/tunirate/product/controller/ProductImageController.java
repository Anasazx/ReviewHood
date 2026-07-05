package tn.anasazx.tunirate.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.anasazx.tunirate.product.dto.ProductImageResponse;
import tn.anasazx.tunirate.product.service.ProductImageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/productImage")
public class ProductImageController {

    private final ProductImageService productImageService;

    @GetMapping("/{productId}")
    ResponseEntity<List<ProductImageResponse>> getImagesByProductId(@PathVariable Long productId){
        return ResponseEntity.ok(productImageService.getImagesByProductId(productId));
    }
    @PostMapping("/{productId}")
    ResponseEntity<ProductImageResponse> addImage(@PathVariable Long productId, @RequestParam("file") MultipartFile file){
        System.out.println("Adding image to product " + productId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productImageService.addImage(productId, file));
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long imageId){
        productImageService.deleteImage(imageId);
        return ResponseEntity
                .noContent()
                .build();

    }


    @PostMapping("/{imageId}/main")
    ResponseEntity<Void> setMainImage(@PathVariable Long imageId, @RequestParam Long productId){
        productImageService.setMainImage(productId,  imageId);
        return ResponseEntity
                .noContent()
                .build();
    }



}

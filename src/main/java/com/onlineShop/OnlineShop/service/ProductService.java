package com.onlineShop.OnlineShop.service;

import com.onlineShop.OnlineShop.dto.ProductRequest;
import com.onlineShop.OnlineShop.dto.ProductResponse;
import com.onlineShop.OnlineShop.entity.Category;
import com.onlineShop.OnlineShop.entity.Product;
import com.onlineShop.OnlineShop.repository.CategoryRepository;
import com.onlineShop.OnlineShop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .imageUrl(request.getImageUrl())
                .category(category)
                .active(true)
                .build();

        return toResponse(productRepository.save(product));
    }

    public List<ProductResponse> getAllActiveProducts() {
        return productRepository.findByActiveTrue()
                .stream().map(this::toResponse).toList();
    }

    public List<ProductResponse> getAllProductsForAdmin() {
        return productRepository.findAll()
                .stream().map(this::toResponse).toList();
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return toResponse(product);
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        return toResponse(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setActive(false);   // soft delete — resume/interview mein "soft delete" pattern accha dikhta hai
        productRepository.save(product);
    }

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .imageUrl(product.getImageUrl())
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .active(product.isActive())
                .build();
    }
}
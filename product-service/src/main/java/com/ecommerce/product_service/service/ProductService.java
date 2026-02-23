package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.ProductRequest;
import com.ecommerce.product_service.dto.ProductResponse;
import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        try {

            List<Product> products = productRepository.findAll();
            List<ProductResponse> responses = new ArrayList<>();

            for (Product product : products) {
                ProductResponse response = new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStock()
                );
                responses.add(response);
            }
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<ProductResponse> getProductById(Long id) {
        try {

            Product product = productRepository.findById(id).get();
            ProductResponse response = new ProductResponse(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStock()
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<ProductResponse> getProductByName(String productName) {
        try {

            Product product = productRepository.findByName(productName);
            ProductResponse response = new ProductResponse(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStock()
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<ProductResponse> addProduct(ProductRequest productRequest) {
        try {

            Product product = Product.builder()
                    .name(productRequest.name())
                    .description(productRequest.description())
                    .price(productRequest.price())
                    .stock(productRequest.stock())
                    .build();

            Product savedProduct = productRepository.save(product);

            ProductResponse response = new ProductResponse(
                    savedProduct.getId(),
                    savedProduct.getName(),
                    savedProduct.getDescription(),
                    savedProduct.getPrice(),
                    savedProduct.getStock()
            );

            return new ResponseEntity<>(response , HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ProductResponse(0L,"","", BigDecimal.ZERO,-1), HttpStatus.BAD_REQUEST)    ;
    }

    public ResponseEntity<String> updateProduct(Long id, Product product) {
        try {
            productRepository.save(product);
            return new ResponseEntity<>("Product updated Successfully" , HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Failed to update Product", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> deleteProductById(Long id) {
        try {
            productRepository.deleteById(id);
            return new ResponseEntity<>("Product deleted Successfully" , HttpStatus.NO_CONTENT);
        } catch ( Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Failed to delete Product", HttpStatus.BAD_REQUEST);
    }
}

package com.ecommerce.product_service.service;

import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Product> getProductById(Long id) {
        try {
            Product product = productRepository.findById(id).get();
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Product> getProductByName(String productName) {
        try {
            Product product = productRepository.findByName(productName);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addProduct(Product product) {
        try {
            productRepository.save(product);
            return new ResponseEntity<>("Product added Successfully" , HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Failed to add Product", HttpStatus.BAD_REQUEST)    ;
    }

    public ResponseEntity<String> updateProduct(Product product) {
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
            return new ResponseEntity<>("Product deleted Successfully" , HttpStatus.OK);
        } catch ( Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Failed to delete Product", HttpStatus.BAD_REQUEST);
    }
}

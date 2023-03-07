package com.programmingtechie.productservice.controller;

import com.programmingtechie.productservice.dto.ProductRequest;
import com.programmingtechie.productservice.dto.ProductResponse;
import com.programmingtechie.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping(value="/products")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping(value="/product")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProduct(String id) {
        // TODO: Return Product
    }

    @PostMapping(value="/product")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
    }

    @PutMapping(value="/product")
    @ResponseStatus(HttpStatus.OK)
    public void updateProduct(@RequestBody ProductRequest productRequest) {
        // TODO: Update Product
    }

    @DeleteMapping(value="/product")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduct(@RequestBody String productId) {
        // TODO: Delete Product
    }

}

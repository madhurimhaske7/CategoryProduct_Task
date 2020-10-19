package com.project.test.controller;


import com.project.test.exception.ApiRequestException;
import com.project.test.model.Category;
import com.project.test.model.Product;
import com.project.test.model.User;
import com.project.test.repository.CategoryRepository;
import com.project.test.repository.ProductRepository;
import com.project.test.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/categories")
    public List<Category> listCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/categories/{id}")
    public Category findCategory(@PathVariable Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.get();
    }

    @PostMapping("/addProduct")
    public ResponseEntity<Object> addProduct(@RequestBody Product product) throws Exception {
        Optional<Category> category = categoryRepository.findById(product.getCategory().getId());
        if(!category.isPresent()) {
            throw new ApiRequestException("Category is not found.");
        }

        Optional<User> user = userRepository.findById(product.getSeller().getId());
        if(!user.isPresent())
            throw new ApiRequestException("User is not found.");

        category.get().addProduct(product);
        user.get().addProduct(product);
        product.setSeller(user.get());
        product.setCategory(category.get());
        Product newProduct = productRepository.save(product);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newProduct.getId()).toUri();

        return ResponseEntity.created(location).build();

    }

    @GetMapping("/categories/{name}/products")
    public List<Product> findProductsByCategoryName(@PathVariable String name) {
        return productRepository.findByCategoryNameContaining(name);
    }

    @GetMapping("/categoriesById/{id}/products")
    public List<Product> findProductsByCategoryId(@PathVariable Long id) {
        return productRepository.findByCategoryId(id);
    }

}

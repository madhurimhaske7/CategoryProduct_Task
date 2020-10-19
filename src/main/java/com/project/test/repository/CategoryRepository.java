package com.project.test.repository;


import com.project.test.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    public List<Category> findAll();

    public Optional<Category> findById(Long id);

}

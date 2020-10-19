package com.project.test.repository;

import com.project.test.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    public List<User> findAll();

    public Optional<User> findById(Long id);
}

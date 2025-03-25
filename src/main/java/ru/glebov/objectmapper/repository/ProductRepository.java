package ru.glebov.objectmapper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.glebov.objectmapper.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
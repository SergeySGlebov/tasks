package ru.glebov.objectmapper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.glebov.objectmapper.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
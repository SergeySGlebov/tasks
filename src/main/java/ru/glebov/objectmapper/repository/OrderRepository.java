package ru.glebov.objectmapper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.glebov.objectmapper.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
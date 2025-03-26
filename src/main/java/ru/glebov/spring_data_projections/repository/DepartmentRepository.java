package ru.glebov.spring_data_projections.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.glebov.spring_data_projections.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
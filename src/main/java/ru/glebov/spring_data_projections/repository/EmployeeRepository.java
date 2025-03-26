package ru.glebov.spring_data_projections.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.glebov.spring_data_projections.model.Employee;
import ru.glebov.spring_data_projections.model.EmployeeProjection;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    EmployeeProjection findProjectionById(Integer id);

}
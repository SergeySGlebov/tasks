package ru.glebov.spring_data_projections.service;

import org.springframework.stereotype.Service;
import ru.glebov.spring_data_projections.model.Employee;
import ru.glebov.spring_data_projections.model.EmployeeProjection;
import ru.glebov.spring_data_projections.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee get(Integer id) {
        return employeeRepository.getReferenceById(id);
    }

    public EmployeeProjection getProjection(Integer id) {
        return employeeRepository.findProjectionById(id);
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public boolean delete(Integer id) {
        return employeeRepository.findById(id).map(
                employee -> {
                    employeeRepository.delete(employee);
                    return true;
                }).orElse(false);
    }
}

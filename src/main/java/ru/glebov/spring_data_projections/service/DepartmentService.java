package ru.glebov.spring_data_projections.service;

import org.springframework.stereotype.Service;
import ru.glebov.spring_data_projections.model.Department;
import ru.glebov.spring_data_projections.repository.DepartmentRepository;

@Service
public class DepartmentService {

    private DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department get(Integer id) {
        return departmentRepository.getReferenceById(id);
    }

    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    public boolean delete(Integer id) {
        return departmentRepository.findById(id).map(
                department -> {
                    departmentRepository.delete(department);
                    return true;
                }).orElse(false);
    }

}

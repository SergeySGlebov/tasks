package ru.glebov.spring_data_projections.controller;

import org.springframework.web.bind.annotation.*;
import ru.glebov.spring_data_projections.model.Department;
import ru.glebov.spring_data_projections.service.DepartmentService;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/{id}")
    public Department get(@PathVariable Integer id) {
        return departmentService.get(id);
    }

    @PostMapping
    public Department save(@RequestBody Department department) {
        return departmentService.save(department);
    }

    @PutMapping
    public void update(@RequestBody Department department) {
        departmentService.save(department);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        departmentService.delete(id);
    }
}

package ru.glebov.spring_data_projections.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.glebov.spring_data_projections.model.Employee;
import ru.glebov.spring_data_projections.model.EmployeeProjection;
import ru.glebov.spring_data_projections.service.EmployeeService;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/projection/{id}")
    public EmployeeProjection getProjection(@PathVariable Integer id) {
        return employeeService.getProjection(id);
    }

    @GetMapping("/{id}")
    public Employee get(@PathVariable Integer id) {
        return employeeService.get(id);
    }

    @PostMapping
    public Employee save(@RequestBody Employee employee) {
        return employeeService.save(employee);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Employee employee) {
        employeeService.save(employee);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        employeeService.delete(id);
    }
}

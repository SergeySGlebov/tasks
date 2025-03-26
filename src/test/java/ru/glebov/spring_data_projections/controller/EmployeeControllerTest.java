package ru.glebov.spring_data_projections.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.glebov.spring_data_projections.model.Department;
import ru.glebov.spring_data_projections.model.Employee;
import ru.glebov.spring_data_projections.service.DepartmentService;
import ru.glebov.spring_data_projections.service.EmployeeService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeControllerTest {

    static final String REST_URL = "/api/employee";

    @Autowired
    EmployeeService employeeService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    MockMvc mvc;

    Department department;
    Employee employee;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setName("Департамент департаментов");
        department = departmentService.save(department);
        employee = new Employee();
        employee.setFirstName("Иван");
        employee.setLastName("Иванов");
        employee.setSalary(270000);
        employee.setPosition("Руководитель руководителей");
        employee.setDepartment(department);
        employee = employeeService.save(employee);
    }

    @Test
    void getProjection() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(REST_URL + "/projection/" + employee.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fullName").value("Иван Иванов"))
                .andExpect(jsonPath("$.departmentName").value("Департамент департаментов"))
                .andExpect(jsonPath("$.firstName").doesNotExist())
                .andExpect(jsonPath("$.salary").doesNotExist())
                .andExpect(jsonPath("$.department").doesNotExist())
                .andDo(print());
    }

    @Test
    void get() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(REST_URL + "/" + employee.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("Иван"))
                .andExpect(jsonPath("$.department.name").value("Департамент департаментов"))
                .andDo(print());
    }

    @Test
    void save()  throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        Employee secondEmployee = new Employee();
        secondEmployee.setFirstName("Федор");
        secondEmployee.setLastName("Федоров");
        secondEmployee.setSalary(135000);
        secondEmployee.setPosition("Заместитель руководителя руководителей");
        secondEmployee.setDepartment(department);
        String jsonResponse = mvc.perform(MockMvcRequestBuilders.post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondEmployee)))
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(secondEmployee)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(objectMapper.readValue(jsonResponse, Employee.class));
    }

    @Test
    void update()  throws Exception{
        employee.setSalary(400000);
        mvc.perform(MockMvcRequestBuilders.put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(employee)))
                .andExpect(status().isNoContent());
        assertThat(employee)
                .usingRecursiveComparison()
                .isEqualTo(employeeService.get(employee.getId()));
    }

    @Test
    void delete()  throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete(REST_URL + "/" + employee.getId()))
                .andExpect(status().isNoContent());
        assertThrows(JpaObjectRetrievalFailureException.class, () -> employeeService.get(employee.getId()));
    }
}
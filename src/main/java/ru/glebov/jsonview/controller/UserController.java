package ru.glebov.jsonview.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.glebov.jsonview.Views;
import ru.glebov.jsonview.model.User;
import ru.glebov.jsonview.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    @JsonView(Views.UserDetails.class)
    public User getUser(@PathVariable int id) {
        return userRepository.getReferenceById(id);
    }

    @GetMapping
    @JsonView(Views.UserSummary.class)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @PostMapping
    @JsonView(Views.UserDetails.class)
    public User save(@RequestBody @Valid User user) {
        return userRepository.save(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid User user) {
        userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        userRepository.delete(id);
    }

}

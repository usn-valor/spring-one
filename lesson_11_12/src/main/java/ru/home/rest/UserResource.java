package ru.home.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.home.controller.BadRequestException;
import ru.home.controller.NotFoundException;
import ru.home.service.user.UserRepr;
import ru.home.service.user.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "User Resource API", description = "API to manipulate User Resource...") // Swagger-аннотация
@CrossOrigin(origins = "http://localhost:63342") // аннотация для запуска фронтэнд-кода на JS
@RestController
@RequestMapping("/api/v1/user")
public class UserResource {

    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/all", produces = "application/json")
    public List<UserRepr> findAll() {
        return userService.findAll().stream().peek(u -> u.setPassword(null)).collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public UserRepr findById(@PathVariable("id") Long id) {
        UserRepr userRepr = userService.findById(id).orElseThrow(NotFoundException::new);
        userRepr.setPassword(null);
        return userRepr;
    }

    @GetMapping("filter")
    public Page<UserRepr> listPage(@RequestParam("usernameFilter") Optional<String> usernameFilter,
                           @RequestParam("ageMinFilter") Optional<Integer> ageMinFilter,
                           @RequestParam("ageMaxFilter") Optional<Integer> ageMaxFilter,
                           @Parameter(example = "1") @RequestParam("page") Optional<Integer> page, /* Swagger-аннотация
                           Parameter указывает на страницу по умолчанию. В нашем случае это 1 */
                           @RequestParam("size") Optional<Integer> size,
                           @RequestParam("sortField") Optional<String> sortField) {


        return userService.findWithFilter(
                usernameFilter.filter(s -> !s.isBlank()).orElse(null),
                ageMinFilter.orElse(null),
                ageMaxFilter.orElse(null),
                page.orElse(1) - 1,
                size.orElse(3),
                sortField.orElse(null)
        );
    }

    @Secured("SUPER_ADMIN")
    @PostMapping(consumes = "application/json")
    public UserRepr create(@RequestBody UserRepr userRepr) {
        if (userRepr.getId() != null)
            throw new BadRequestException();
        userService.save(userRepr);
        return userRepr;
    }

    @Secured("SUPER_ADMIN")
    @PutMapping(consumes = "application/json")
    public void update(@RequestBody UserRepr userRepr) {
        if (userRepr.getId() == null)
            throw new BadRequestException();
        userService.save(userRepr);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }

    @ExceptionHandler
    public ResponseEntity<String> notFoundException(NotFoundException ex) {
        return new ResponseEntity<>("Entity not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> badRequestException(BadRequestException ex) {
        return new ResponseEntity<>("Bad request", HttpStatus.NOT_FOUND);
    }
}

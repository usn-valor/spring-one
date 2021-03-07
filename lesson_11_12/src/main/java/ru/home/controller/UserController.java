package ru.home.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.home.persist.user.RoleRepository;
import ru.home.service.user.UserRepr;
import ru.home.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    private final RoleRepository roleRepository;

    @Autowired
    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String listPage(Model model, @RequestParam("usernameFilter") Optional<String> usernameFilter,
                                        @RequestParam("ageMinFilter") Optional<Integer> ageMinFilter,
                                        @RequestParam("ageMaxFilter") Optional<Integer> ageMaxFilter,
                                        @RequestParam("page") Optional<Integer> page,
                                        @RequestParam("size") Optional<Integer> size,
                                        @RequestParam("sortField") Optional<String> sortField) {
        logger.info("List page requested");

        Page<UserRepr> users = userService.findWithFilter(
                usernameFilter.filter(s -> !s.isBlank()).orElse(null),
                ageMinFilter.orElse(null),
                ageMaxFilter.orElse(null),
                page.orElse(1) - 1,
                size.orElse(3),
                sortField.orElse(null)
                );
        /*if (usernameFilter.isPresent() && !usernameFilter.get().isBlank())
            users = userService.findWithFilter(usernameFilter.get());
        else
            users = userService.findAll();*/
        model.addAttribute("users", users);
        return "user";
    }

    @GetMapping("/{id}")
    public String editPage(@PathVariable("id") Long id, Model model, Authentication auth, HttpServletRequest req) {
        logger.info("Edit page for id {} requested", id);

        //auth = SecurityContextHolder.getContext().getAuthentication();
        auth.getAuthorities().stream().anyMatch(ath -> ath.getAuthority().equals("ROLE_ADMIN"));
        req.isUserInRole("ROLE_ADMIN");

        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("user", userService.findById(id).orElseThrow(NotFoundException::new)); /* после всех
        изменений добавлен код orElseThrow(NotFoundException::new) */
        return "user_form";
    }

    @Secured({"SUPER_ADMIN"})
    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("user") UserRepr user, BindingResult result, Model model) {
        logger.info("Update endpoint requested");

        model.addAttribute("roles", roleRepository.findAll());
        if (result.hasErrors()) {
            return "user_form";
        }
        if (!user.getPassword().equals(user.getMatchingPassword())) {
            result.rejectValue("password", "", "Password not matching");
            return "user_form";
        }

        logger.info("Updating user with id {}", user.getId());
        userService.save(user);

        /*if (user.getId() != null) {
            logger.info("Updating user with id {}", user.getId());
            userService.update(user);
        } else {
            logger.info("Creating new user");
            userService.insert(user);
        }*/
        return "redirect:/user";
    }

    @Secured({"SUPER_ADMIN"})
    @GetMapping("/new")
    public String create(Model model) {
        logger.info("Create new user request");

        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("user", new UserRepr());
        return "user_form";
    }

    @DeleteMapping("/{id}")
    public String remove(@PathVariable("id") Long id) {
        logger.info("User delete request");

        userService.delete(id);
        return "redirect:/user";
    }

    @ExceptionHandler
    public ModelAndView notFoundExceptionHandler(NotFoundException ex) {
        ModelAndView mav = new ModelAndView("not_found");
        mav.setStatus(HttpStatus.NOT_FOUND);
        return mav;
    }

}

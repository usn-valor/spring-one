package ru.home.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.home.persist.User;
import ru.home.persist.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listPage(Model model) {
        logger.info("List page requested");

        model.addAttribute("users", userRepository.findAll());
        return "user";
    }

    @GetMapping("/{id}")
    public String editPage(@PathVariable("id") Long id, Model model) {
        logger.info("Edit page for id {} requested", id);

        model.addAttribute("user", userRepository.findById(id));
        return "user_form";
    }

    @PostMapping("/update")
    public String update(User user) {
        logger.info("Update endpoint requested");

        if (user.getId() != -1) {
            logger.info("Updating user with id {}", user.getId());
            userRepository.update(user);
        } else {
            logger.info("Creating new user");
            userRepository.insert(user);
        }
        return "redirect:/user";
    }

    @GetMapping("/new")
    public String create() {
        // TODO
        return null;
    }

    @GetMapping("/{id}/delete")
    public String remove(@PathVariable("id") Long id) {
        // TODO
        return null;
    }
}

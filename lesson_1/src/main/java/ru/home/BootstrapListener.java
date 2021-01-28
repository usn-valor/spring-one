package ru.home;

import ru.home.persist.User;
import ru.home.persist.UserRepository;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class BootstrapListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();

        UserRepository userRepository = new UserRepository();
        sc.setAttribute("userRepository", userRepository);

        userRepository.insert(new User("user1"));
        userRepository.insert(new User("user2"));
        userRepository.insert(new User("user3"));
    }
}

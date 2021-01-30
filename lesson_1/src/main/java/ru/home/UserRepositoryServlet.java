package ru.home;

import ru.home.persist.User;
import ru.home.persist.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/users/*")
public class UserRepositoryServlet extends HttpServlet {

    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        userRepository = (UserRepository) getServletContext().getAttribute("userRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println("<table style=\"width:50%; border: 1px solid black;\">");
        resp.getWriter().println("<tr><th>UserId</th>" + "\n" + "<th>UserName</th></tr>");
        for (User u: userRepository.findAll()) {
            resp.getWriter().println("<tr><td>" + u.getId() + "</td>" + "\n"
                    + "<td>" + u.getUserName() + "</td></tr>");
        }
        resp.getWriter().println("</table>");
    }
}

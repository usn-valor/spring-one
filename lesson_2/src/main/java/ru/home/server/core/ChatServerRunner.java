package ru.home.server.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.home.server.gui.ServerGUI;

import javax.swing.*;

public class ChatServerRunner {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ServerGUI gui = context.getBean("serverGUI", ServerGUI.class);
            }
        });
    }
}

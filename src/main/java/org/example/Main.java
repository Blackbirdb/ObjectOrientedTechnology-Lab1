package org.example;

import org.example.cli.CommandLineInterface;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Main.class, args);
        CommandLineInterface cli = context.getBean(CommandLineInterface.class);
        cli.start();
    }
}
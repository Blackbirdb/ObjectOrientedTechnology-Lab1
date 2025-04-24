package org.example;

import org.example.cli.CommandLineInterface;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {
    public static void main(String[] args) {
        new CommandLineInterface().start();
    }
}
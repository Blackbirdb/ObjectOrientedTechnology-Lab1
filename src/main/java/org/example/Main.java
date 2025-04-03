package org.example;

import org.example.cli.CommandLineInterface;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new CommandLineInterface().start();
    }
}
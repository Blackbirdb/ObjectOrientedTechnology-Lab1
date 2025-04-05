package org.example.cli;

import org.example.session.SessionManager;
import org.example.tools.utils.CommandTable;
import org.example.tools.utils.PathUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandLineInterfaceTest {
    private CommandLineInterface cli;
    private SessionManager sessionManager;
    private CommandTable commandTable;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        sessionManager = mock(SessionManager.class);
        commandTable = mock(CommandTable.class);
//        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void start_initializesCwdWhenEmpty() {
        InputStream inputStream = new ByteArrayInputStream("\nexit\n".getBytes());
        Scanner scanner = new Scanner(inputStream);
        cli = new CommandLineInterface(sessionManager, commandTable, scanner);
        when(sessionManager.cwdIsSet()).thenReturn(false, true);

        cli.start();

        verify(sessionManager).setCwd(System.getProperty("user.dir"));
    }

    @Test
    void start_setsCwdWhenValidPathProvided() {
        String validPath = "src/main/resources/testFiles";
        String command = validPath + "\nexit\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(validPath.getBytes()));
        cli = new CommandLineInterface(sessionManager, commandTable, scanner);
        when(sessionManager.cwdIsSet()).thenReturn(false, true);

        cli.start();

        verify(sessionManager).setCwd(validPath);
    }

    @Test
    void start_rejectsInvalidPath() {
        String invalidPath = "invalid::path";
        String command = invalidPath + "\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(command.getBytes()));
        cli = new CommandLineInterface(sessionManager, commandTable, scanner);
        when(sessionManager.cwdIsSet()).thenReturn(false, false);

        cli.start();

        verify(sessionManager, never()).setCwd(invalidPath);
    }
}
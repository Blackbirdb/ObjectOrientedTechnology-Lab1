package org.example.command;

import java.io.IOException;

public interface IrrevocableCommand {
    void execute() throws IOException;
}

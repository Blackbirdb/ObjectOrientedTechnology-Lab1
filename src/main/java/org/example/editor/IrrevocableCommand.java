package org.example.editor;

import java.io.IOException;

public interface IrrevocableCommand {
    void execute() throws IOException;
}

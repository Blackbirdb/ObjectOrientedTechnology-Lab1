package org.example.filesys;

import java.util.List;
import java.util.Stack;

import static org.example.utils.PrintTreeUtil.*;

class DirectoryPrinterVisitor implements FileSystemVisitor {
    private final StringBuilder output = new StringBuilder();
    private int depth = 0;
    private final Stack<Boolean> isLastStack = new Stack<>();

    @Override
    public void visit(FileNode file) {
        output.append(getIndentString(isLastStack, depth))
                .append(getConnectorString(file.isLastChild(), depth))
                .append(file.getPath().getFileName())
                .append(file.isOpen() ? "*" : "")
                .append("\n");
    }

    @Override
    public void visit(DirectoryNode directory) {

        output.append(getIndentString(isLastStack, depth))
                .append(getConnectorString(directory.isLastChild(), depth))
                .append(directory.getPath().getFileName())
                .append("/\n");

        List<FileSystemNode> children = directory.getChildren();

        depth++;
        isLastStack.push(directory.isLastChild());
        for (FileSystemNode child : children) {
            child.accept(this);
        }
        isLastStack.pop();
        depth--;

    }

    public String getOutput() {
        return output.toString();
    }

}
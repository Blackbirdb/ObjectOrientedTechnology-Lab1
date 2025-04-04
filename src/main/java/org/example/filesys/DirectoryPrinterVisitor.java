package org.example.filesys;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class DirectoryPrinterVisitor implements FileSystemVisitor {
    private StringBuilder output = new StringBuilder();
    private List<Boolean> isLastStack = new ArrayList<>();

    private String indent(int depth) {
        return " ".repeat(depth * 2);
    }
    @Override
    public void visit(FileNode file, int depth) {
        if (depth > 1) {
            isLastStack = isLastStack.subList(0, depth);
        }
        output.append(getIndentString(depth))
                .append(getConnectorString(file.isLastChild(), depth))
                .append(file.getPath().getFileName())
                .append("\n");
    }
    @Override
    public void visit(DirectoryNode directory, int depth) {

        if (depth > 0) {
            isLastStack = isLastStack.subList(0, depth);
        }

        output.append(getIndentString(depth))
                .append(getConnectorString(directory.isLastChild(), depth))
                .append(directory.getPath().getFileName())
                .append("/\n");

        isLastStack.add(directory.isLastChild());
    }
    public String getOutput() {
        return output.toString();
    }

    private String getIndentString(int depth) {
        StringBuilder sb = new StringBuilder();
        int level = 1;

        while (level <= depth - 1) {
            boolean isLast = isLastStack.get(level);
            String appendString = isLast ? "    " : "│   ";
            sb.append(appendString);
            level++;
        }
        return sb.toString();
    }

    private String getConnectorString(boolean isLast, int depth) {
        if (depth == 0) { return ""; }
        else {
            return isLast ? "└── " : "├── ";
        }
    }
}
package org.example.filesys;

class DirectoryPrinterVisitor implements FileSystemVisitor {
    private StringBuilder output = new StringBuilder();
    private String indent(int depth) {
        return " ".repeat(depth * 2);
    }
    @Override
    public void visit(FileNode file, int depth) {
        output.append(indent(depth))
                .append(file.getPath().getFileName())
                .append(file.isOpen() ? "*" : "")
                .append("\n");
    }
    @Override
    public void visit(DirectoryNode directory, int depth) {
        output.append(indent(depth))
                .append(directory.getPath().getFileName())
                .append("/\n");
    }
    public String getOutput() {
        return output.toString();
    }
}
package org.example.tools.filesys;

import java.nio.file.Path;

class FileNode extends FileSystemNode {
    private boolean isOpen;

    public FileNode(Path path, boolean isOpen) {
        super(path);
        this.isOpen = isOpen;
    }
    public boolean isOpen() {
        return isOpen;
    }
    @Override
    public void accept(FileSystemVisitor visitor) {
        visitor.visit(this);
    }
}
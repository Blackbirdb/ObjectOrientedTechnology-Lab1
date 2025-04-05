package org.example.tools.filesys;

interface FileSystemVisitor {
    void visit(FileNode file);
    void visit(DirectoryNode directory);
}
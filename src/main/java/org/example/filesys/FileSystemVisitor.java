package org.example.filesys;

interface FileSystemVisitor {
    void visit(FileNode file);
    void visit(DirectoryNode directory);
}
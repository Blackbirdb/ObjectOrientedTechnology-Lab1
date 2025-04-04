package org.example.filesys;

interface FileSystemVisitor {
    void visit(FileNode file, int depth);
    void visit(DirectoryNode directory, int depth);
}
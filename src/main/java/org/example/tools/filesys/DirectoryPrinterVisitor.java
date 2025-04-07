package org.example.tools.filesys;

import org.example.treeprinter.TreePrintVisitor;

class DirectoryPrinterVisitor extends TreePrintVisitor {
    public void visit(DirectoryNode node) {
        node.accept(this);
    }

    public void visit(FileNode node) {
        node.accept(this);
    }
}
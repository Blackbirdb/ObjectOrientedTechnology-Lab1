package org.example.tools.filesys;

import org.example.tools.treeprinter.InnerTreeNode;
import org.example.tools.treeprinter.LeafTreeNode;
import org.example.tools.treeprinter.TreePrintVisitor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class DirectoryPrinterVisitor extends TreePrintVisitor {

    @Override
    public void visit(LeafTreeNode node) {
        Path path = ((FileNode) node).getPath();
        boolean isOpen = ((FileNode) node).isOpen();

        String text = path.getFileName().toString();
        if (isOpen) {
            text += "*";
        }

        visitLeafNode(node, text);
    }

    @Override
    public void visit(InnerTreeNode node) {
        Path path = ((DirectoryNode) node).getPath();
        String text = path.getFileName().toString() + "/";

        visitInnerNode(node, text);
    }
}
package org.example.service;

import org.example.document.HtmlDocument;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TreePrinterTest {

    private String rootFilePath = "src/test/java/org/example/testFiles/";

    private String constructFilePath(String relativePath){
        return rootFilePath + relativePath;
    }

    @Test
    public void testDefaultTemplate() throws IOException {
        String result = testTreePrint("default.html");

        String expected = """
                html#html
                ├── head#head
                │   └── title#title
                └── body#body
                """;

        assertEquals(expected, result);
    }

    @Test
    public void testNestedTemplate() throws IOException {
        String result = testTreePrint("nested.html");

        String expected = """
                html#html
                ├── head#head
                │   └── title#title
                │       └── Test Page
                └── body#body
                    ├── div#header
                    │   ├── Welcome to my page
                    │   ├── h1#main-title
                    │   │   └── Main Title
                    │   └── p#subtitle
                    │       └── This is a subtitle
                    ├── section#content
                    │   ├── p#description
                    │   │   └── This is a paragraph inside the section.
                    │   └── ul#list
                    │       ├── li#item-1
                    │       │   └── Item 1
                    │       ├── li#item-2
                    │       │   └── Item 2
                    │       └── li#item-3
                    │           └── Item 3
                    └── footer#footer
                        ├── Contact us at example@email.com
                        └── p#footer-text
                            └── Footer text here.
                """;

        assertEquals(expected, result);
    }

    @Test
    public void testBasics() throws Exception {
        String result = testTreePrint("basics.html");
        String expected = """
                html
                ├── head
                │   └── title
                └── body
                    ├── h1#main-title
                    │   └── Hello, World!
                    └── p#paragraph
                        └── This is a paragraph.
                """;
    }

    public String testTreePrint(String filePath) throws IOException {
        HtmlFileReader htmlFileReader = new HtmlFileReader();
        HtmlDocument document = htmlFileReader.readHtmlFromFile(constructFilePath(filePath));

        TreePrinter printer = new TreePrinter(document);
        String result = printer.getTreeOutput();

        System.out.println(result);

        return result;
    }

}
package com.example;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {
        File file = new File("src/main/java/com/example/Example.java");
        JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, new FileInputStream(file));
        String sourceCode = new String(Files.readAllBytes(Paths.get(file.toURI())));

        for (MethodSource<?> method : javaClass.getMethods()) {
            int beginLine = sourceCode.substring(0, method.getStartPosition()).split("\n").length;
            int endLine = sourceCode.substring(0, method.getEndPosition()).split("\n").length;
            System.out.println(method.getName() + "|" + method.getInternal() + " starts at line " + beginLine
                    + " and ends at line " + endLine);
        }
    }
}

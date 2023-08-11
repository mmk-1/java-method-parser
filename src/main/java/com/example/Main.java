package com.example;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class Main {

    private static final Map<String, String> javaToASMMap = new HashMap<>();

    static {
        javaToASMMap.put("boolean", "Z");
        javaToASMMap.put("char", "C");
        javaToASMMap.put("byte", "B");
        javaToASMMap.put("short", "S");
        javaToASMMap.put("int", "I");
        javaToASMMap.put("float", "F");
        javaToASMMap.put("long", "J");
        javaToASMMap.put("double", "D");
    }


    public static void main(String[] args) throws Exception {
        File file = new File("/workspaces/rv-project/method-parser/demo/src/main/java/com/example/Example.java");
        JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, Files.newInputStream(file.toPath()));
        String sourceCode = new String(Files.readAllBytes(Paths.get(file.toURI())));
        Map<String, ArrayList<Integer>> result = new HashMap<>();

        for (MethodSource<?> method : javaClass.getMethods()) {
            int beginLine = sourceCode.substring(0, method.getStartPosition()).split("\n").length;
            int endLine = sourceCode.substring(0, method.getEndPosition()).split("\n").length;
            ArrayList<Integer> nums = new ArrayList<>();
            nums.add(beginLine);
            nums.add(endLine);
//            String str = method.toSignature().split(":")[0];
//            String[] strs = str.split(" ");
//            str = "";
//            for (int i = 1; i < strs.length; i++){
//                str += strs[i] + " ";
//            }
//            str = str.trim();
            System.out.println(method.toSignature());
            System.out.println(convertToAsm(method.toSignature()));
        }
    }
public static String convertToAsm(String javaMethodSignature) {
        // Extract the method name and parameter types from the Java method signature
        int methodNameStart = javaMethodSignature.indexOf(" ") + 1;
        int methodNameEnd = javaMethodSignature.indexOf("(");
        String methodName = javaMethodSignature.substring(methodNameStart, methodNameEnd);

        System.out.println(methodName);


        int paramsStart = methodNameEnd + 1;
        int paramsEnd = javaMethodSignature.indexOf(")");
        String params = javaMethodSignature.substring(paramsStart, paramsEnd);

        String[] paramTypes;
        if (params.isEmpty()) {
            paramTypes = new String[0];
        } else {
            paramTypes = params.split(",");
        }
        // Convert the parameter types to the ASM format
        StringBuilder asmParams = new StringBuilder();
        for (String paramType : paramTypes) {
            paramType = paramType.trim();
            asmParams.append(convertToASMType(paramType));
        }
        // Return the ASM method signature
        return methodName + "(" + asmParams.toString() + ")";
    }


    public static String convertToASMType(String javaType) {
        return javaToASMMap.getOrDefault(javaType, "L" + javaType.replace(".", "/") + ";");
    }
}
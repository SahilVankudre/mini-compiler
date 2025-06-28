package com.minicom;

import com.minicom.lexer.Lexer;
import com.minicom.lexer.Token;
import com.minicom.parser.Parser;
import com.minicom.parser.SyntaxException;
import com.minicom.semantic.SemanticAnalyzer;
import com.minicom.semantic.SemanticException;
import com.minicom.codegen.CodeGenerator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Mini Compiler");
        System.out.println("Enter your code below : ");

        StringBuilder codeBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().equalsIgnoreCase("END")) break;
                codeBuilder.append(line).append("\n");
            }
        } catch (Exception e) {
            System.err.println("❌ Error reading input: " + e.getMessage());
            return;
        }

        String code = codeBuilder.toString().trim();
        System.out.println("\n Lexing & Parsing your code...\n");

        Lexer lexer = new Lexer();
        List<Token> tokens = lexer.tokenize(code);
        tokens.forEach(System.out::println);

        try {
            Parser parser = new Parser(tokens);
            parser.parse();
            System.out.println("\n✅ Syntax is valid");

            SemanticAnalyzer analyzer = new SemanticAnalyzer(tokens);
            analyzer.analyze();
            System.out.println("✅ Semantics are valid");

            CodeGenerator generator = new CodeGenerator(tokens);
            generator.generate();
            generator.printIntermediateCode();

        } catch (SyntaxException | SemanticException e) {
            System.err.println("❌ Error: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("❌ Internal Error: " + e.getMessage());
        }
    }
}

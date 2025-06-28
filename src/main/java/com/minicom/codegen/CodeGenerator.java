package com.minicom.codegen;

import com.minicom.lexer.Token;
import com.minicom.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class CodeGenerator {
    private final List<Token> tokens;
    private final List<String> intermediateCode = new ArrayList<>();
    private int position = 0;
    private int tempCount = 1;

    public CodeGenerator(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void generate() {
        while (position < tokens.size()) {
            generateStatement();
        }
    }

    private void generateStatement() {
        consume(TokenType.KEYWORD);
        Token var = consume(TokenType.IDENTIFIER);
        consume(TokenType.OPERATOR); // '='

        String result = generateExpression();

        consume(TokenType.SYMBOL); // ';'

        intermediateCode.add(var.getValue() + " = " + result);
    }

    private String generateExpression() {
        Token left = consume(TokenType.NUMBER, TokenType.IDENTIFIER);
        String leftVal = left.getValue();

        if (match(TokenType.OPERATOR)) {
            Token operator = consume(TokenType.OPERATOR);
            Token right = consume(TokenType.NUMBER, TokenType.IDENTIFIER);
            String rightVal = right.getValue();

            String temp = "t" + tempCount++;
            intermediateCode.add(temp + " = " + leftVal + " " + operator.getValue() + " " + rightVal);
            return temp;
        }

        return leftVal;
    }

    private Token consume(TokenType... expectedTypes) {
        if (position < tokens.size()) {
            Token token = tokens.get(position);
            for (TokenType expected : expectedTypes) {
                if (token.getType() == expected) {
                    position++;
                    return token;
                }
            }
        }
        throw new RuntimeException("Expected valid expression token at: " + currentToken());
    }

    private boolean match(TokenType type) {
        return position < tokens.size() && tokens.get(position).getType() == type;
    }

    private Token currentToken() {
        return tokens.get(position);
    }

    public void printIntermediateCode() {
        System.out.println("\n📜 Intermediate Code:");
        for (String line : intermediateCode) {
            System.out.println(line);
        }
    }
}

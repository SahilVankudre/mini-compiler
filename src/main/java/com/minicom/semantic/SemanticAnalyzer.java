package com.minicom.semantic;

import com.minicom.lexer.Token;
import com.minicom.lexer.TokenType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SemanticAnalyzer {
    private final List<Token> tokens;
    private final Set<String> declaredVariables = new HashSet<>();
    private int position = 0;

    public SemanticAnalyzer(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void analyze() {
        while (position < tokens.size()) {
            analyzeStatement();
        }
    }

    private void analyzeStatement() {
        consume(TokenType.KEYWORD); // int
        Token var = consume(TokenType.IDENTIFIER);
        if (declaredVariables.contains(var.getValue())) {
            throw new SemanticException("Variable '" + var.getValue() + "' already declared.");
        }
        declaredVariables.add(var.getValue());

        consume(TokenType.OPERATOR); // =
        analyzeExpression();
        consume(TokenType.SYMBOL); // ;
    }

    private void analyzeExpression() {
        Token first = consume(TokenType.NUMBER, TokenType.IDENTIFIER);
        if (first.getType() == TokenType.IDENTIFIER && !declaredVariables.contains(first.getValue())) {
            throw new SemanticException("Variable '" + first.getValue() + "' used before declaration.");
        }

        if (match(TokenType.OPERATOR)) {
            consume(TokenType.OPERATOR);
            Token second = consume(TokenType.NUMBER, TokenType.IDENTIFIER);
            if (second.getType() == TokenType.IDENTIFIER && !declaredVariables.contains(second.getValue())) {
                throw new SemanticException("Variable '" + second.getValue() + "' used before declaration.");
            }
        }
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
        throw new SemanticException("Expected token at: " + currentToken());
    }

    private boolean match(TokenType type) {
        return position < tokens.size() && tokens.get(position).getType() == type;
    }

    private Token currentToken() {
        return tokens.get(position);
    }
}

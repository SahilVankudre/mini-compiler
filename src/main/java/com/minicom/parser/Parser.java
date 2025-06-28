package com.minicom.parser;

import com.minicom.lexer.Token;
import com.minicom.lexer.TokenType;

import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int position = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void parse() throws SyntaxException {
        parseStatement();
    }

    private void parseStatement() throws SyntaxException {
        expectTypeAndMessage(TokenType.KEYWORD, "Expected a data type (e.g., int)");
        expectTypeAndMessage(TokenType.IDENTIFIER, "Expected a variable name");
        expectTypeWithExactValue(TokenType.OPERATOR, "=");
        parseExpression();
        expectTypeWithExactValue(TokenType.SYMBOL, ";");
    }

    private void parseExpression() throws SyntaxException {
        expectTypeAndMessage(TokenType.NUMBER, "Expected a number");

        if (match(TokenType.OPERATOR)) {
            advance(); // consume operator
            expectTypeAndMessage(TokenType.NUMBER, "Expected a number after operator");
        }
    }

    private void expectTypeAndMessage(TokenType type, String errorMessage) throws SyntaxException {
        if (!match(type)) {
            throw new SyntaxException(errorMessage + " at token: " + currentToken());
        }
        advance();
    }

    private void expectTypeWithExactValue(TokenType type, String value) throws SyntaxException {
        if (!match(type) || !currentToken().getValue().equals(value)) {
            throw new SyntaxException("Expected '" + value + "' but found: " + currentToken());
        }
        advance();
    }

    private boolean match(TokenType type) {
        return position < tokens.size() && tokens.get(position).getType() == type;
    }

    private Token currentToken() {
        return tokens.get(position);
    }

    private void advance() {
        position++;
    }
}

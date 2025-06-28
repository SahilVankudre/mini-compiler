package com.minicom.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private static final String KEYWORDS = "\\b(int|float|if|else|return)\\b";
    private static final String IDENTIFIER = "\\b[a-zA-Z_][a-zA-Z0-9_]*\\b";
    private static final String NUMBER = "\\b\\d+\\b";
    private static final String OPERATOR = "[+\\-*/=]";
    private static final String SYMBOL = "[;{}()]";

    private static final Pattern TOKEN_PATTERN = Pattern.compile(
            KEYWORDS + "|" + IDENTIFIER + "|" + NUMBER + "|" + OPERATOR + "|" + SYMBOL
    );

    public List<Token> tokenize(String code) {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERN.matcher(code);

        while (matcher.find()) {
            String value = matcher.group();
            TokenType type;

            if (value.matches(KEYWORDS)) type = TokenType.KEYWORD;
            else if (value.matches(IDENTIFIER)) type = TokenType.IDENTIFIER;
            else if (value.matches(NUMBER)) type = TokenType.NUMBER;
            else if (value.matches(OPERATOR)) type = TokenType.OPERATOR;
            else if (value.matches(SYMBOL)) type = TokenType.SYMBOL;
            else type = TokenType.UNKNOWN;

            tokens.add(new Token(type, value));
        }

        return tokens;
    }
}

class Scanner(val source: String) {
    var start: Int = 0
    var current: Int = 0
    var line: Int = 1

    private var tokens = mutableListOf<Token>()

    private val keywords = mapOf<String, TokenType>(
        "and" to TokenType.AND,
        "class" to TokenType.CLASS,
        "else" to TokenType.ELSE,
        "false" to TokenType.FALSE,
        "fun" to TokenType.FUN,
        "for" to TokenType.FOR,
        "if" to TokenType.IF,
        "nil" to TokenType.NIL,
        "or" to TokenType.OR,
        "print" to TokenType.PRINT,
        "return" to TokenType.RETURN,
        "super" to TokenType.SUPER,
        "this" to TokenType.THIS,
        "true" to TokenType.TRUE,
        "var" to TokenType.VAR,
        "while" to TokenType.WHILE
    )

    fun scanTokens(): List<Token> {
        while (!isAtEnd()) {
            start = current
            scanToken()
        }

        tokens.add(Token(TokenType.EOF, "", null, line));
        return tokens
    }

    private fun isAtEnd(): Boolean {
        return current >= source.length
    }

    private fun scanToken() {
        val c = advance()
        when (c) {
            '(' -> addToken(TokenType.LEFT_PAREN)
            ')' -> addToken(TokenType.RIGHT_PAREN)
            '{' -> addToken(TokenType.LEFT_BRACE)
            '}' -> addToken(TokenType.RIGHT_BRACE)
            ',' -> addToken(TokenType.COMMA)
            '.' -> addToken(TokenType.DOT)
            '-' -> addToken(TokenType.MINUS)
            '+' -> addToken(TokenType.PLUS)
            ';' -> addToken(TokenType.SEMICOLON)
            '*' -> addToken(TokenType.STAR)

            '!' -> addToken(if (match('=')) TokenType.BANG_EQUAL else TokenType.BANG)
            '=' -> addToken(if (match('=')) TokenType.EQUAL_EQUAL else TokenType.EQUAL)
            '>' -> addToken(if (match('=')) TokenType.GREATER_EQUAL else TokenType.GREATER)
            '<' -> addToken(if (match('=')) TokenType.LESS_EQUAL else TokenType.LESS)

            '/' -> {
                if (match('/')) {
                    while (peek() != '\n' && !isAtEnd()) advance()
                } else {
                    addToken(TokenType.SLASH)
                }
            }

            // useless characters
            ' ', '\r', '\t' -> {}

            '\n' -> line++

            // string literals
            '"'-> string()

            else -> {
                if (isDigit(c)) {
                    number()
                } else if (isAlpha(c)) {
                    identifier()
                } else {
                    ErrorHandler.error(line,"Unexpected character: $c")
                }
            }

        }
    }

    private fun isAlpha(c: Char): Boolean {
        return ((c in 'a'..'z') || (c in 'A'..'Z') || (c == '_'))
    }
    private fun isDigit(c: Char): Boolean {
        return c in '0'..'9'
    }

    private fun isAlphaNumeric(c: Char): Boolean {
        return isAlpha(c) || isDigit(c)
    }

    private fun string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance()
        }

        if (isAtEnd()) {
            ErrorHandler.error(line, "Unterminated String.")
            return;
        }

        advance()
        val value: String = source.substring(start + 1, current - 1)
        addToken(TokenType.STRING, value)
    }

    private fun number() {
        while (isDigit(peek())) advance()

        if (peek() == '.' && isDigit(peekNext())) {
            advance()
            while (isDigit(peek())) advance()
        }

        addToken(TokenType.NUMBER, source.substring(start, current).toDouble())
    }

    private fun identifier() {
        while (isAlphaNumeric(peek())) advance()

        val text: String = source.substring(start, current)
        var type: TokenType? = keywords[text]
        if (type == null) type = TokenType.IDENTIFIER
        addToken(type)
    }

    // lookahead
    private fun peek(): Char {
        if (isAtEnd()) return 0.toChar()
        return source[current]
    }

    private fun peekNext(): Char {
        if (1 + current >= source.length) return 0.toChar()
        return source[current + 1]
    }

    // lookahead
    private fun match(expected: Char): Boolean {
        if (isAtEnd()) return false;
        if (source[current] != expected) return false;

        current++;
        return true;
    }

    private fun advance(): Char {
        return source[current++]
    }

    private fun addToken(type: TokenType) {
        addToken(type, null)
    }

    private fun addToken(type: TokenType, literal: Any?) {
        var text: String = source.substring(start, current);
        tokens.add(Token(type, text, literal, line));
    }

}

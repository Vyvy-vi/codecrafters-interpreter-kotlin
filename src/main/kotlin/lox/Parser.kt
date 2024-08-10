package lox

class Parser(private val tokens: List<Token>) {

    private class ParseError : RuntimeException()

    private var current: Int = 0

    // expression     → equality ;
    private fun expression() : Expr {
        return equality()
    }

    // equality       → comparison ( ( "!=" | "==" ) comparison )* ;
    private fun equality(): Expr {
        var expr: Expr = comparison()

        while (match(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
            val operator: Token = previous()
            val right: Expr = comparison()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    // comparison     → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
    private fun comparison(): Expr {
        var expr: Expr = term()

        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            val operator: Token = previous()
            val right: Expr = term()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr

    }

    // term           → factor ( ( "-" | "+" ) factor )* ;
    private fun term(): Expr {

        var expr: Expr = factor()

        while (match(TokenType.MINUS, TokenType.PLUS)) {
            val operator: Token = previous()
            val right: Expr = factor()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    // factor         → unary ( ( "/" | "*" ) unary )* ;
    private fun factor(): Expr {
        var expr: Expr = unary()

        while (match(TokenType.SLASH, TokenType.STAR)) {
            val operator: Token = previous()
            val right: Expr = unary()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }


    // unary          → ( "!" | "-" ) unary | primary
    private fun unary(): Expr {

        var operator: Token? = null

        while (match(TokenType.BANG, TokenType.MINUS)) {
            operator = previous()
        }

        val right: Expr = primary()

        if (operator != null) {
            return Expr.Unary(operator, unary())
        } else {
            return right
        }

        return primary()
    }

    // primary        → NUMBER | STRING | "true" | "false" | "nil"
    //               | "(" expression ")" ;
    private fun primary(): Expr {
        if (match(TokenType.FALSE)) return Expr.Literal(false)
        if (match(TokenType.TRUE)) return Expr.Literal(true)
        if (match(TokenType.NIL)) return Expr.Literal(null)

        if (match(TokenType.NUMBER, TokenType.STRING)) {
            val previous = previous().literal
            if (previous == null)
                return Expr.Literal(null)
            else
                return Expr.Literal(previous)
        }

        if (match(TokenType.LEFT_PAREN)) {
            val expr: Expr = expression()
            consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.")
            return Expr.Grouping(expr)
        }

        throw error(peek(), "Expect expression.")
    }

    private fun match(vararg types: TokenType): Boolean {
        for (type in types) {
            if (check(type)) {
                advance()
                return true
            }
        }

        return false
    }

    private fun consume(type: TokenType, message: String): Token {
        if (check(type)) return advance()
        throw error(peek(), message)
    }

    private fun check(type: TokenType): Boolean {
        if (isAtEnd()) return false
        return peek().type == type
    }

    private fun advance() : Token {
        if (!isAtEnd()) current++
        return previous()
    }

    private fun isAtEnd(): Boolean {
        return (peek().type == TokenType.EOF)
    }

    private fun peek(): Token {
        return tokens.get(current)
    }

    private fun previous(): Token {
        return tokens.get(current - 1)
    }

    private fun error(token: Token, message: String): ParseError {
        ErrorHandler.error(token, message)
        return ParseError()
    }

    private fun synchronize() {
        advance()

        while (!isAtEnd()) {
            if (previous().type == TokenType.SEMICOLON) return

            when (peek().type) {
                TokenType.CLASS, TokenType.FUN, TokenType.VAR,
                TokenType.FOR, TokenType.IF, TokenType.WHILE,
                TokenType.PRINT, TokenType.RETURN -> return
                else ->
                    advance()
            }
        }
    }

    fun parse(): Expr? {
        try {
            return expression()
        } catch (error: ParseError) {
            return null
        }
    }



}
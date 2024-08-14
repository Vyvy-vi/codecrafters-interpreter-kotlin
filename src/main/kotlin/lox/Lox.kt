package lox

object Lox {
    var hadError = false
    var hadRuntimeError = false
    val interpreter = Interpreter()

    fun error(line: Int, message: String) {
        report(line, "", message)
    }

    private fun report(line: Int, where: String, message: String) {
        System.err.println("[line $line] Error$where: $message")
        hadError = true;
    }

    fun error(token: Token, message: String) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
        } else {
            report(token.line, " at '" + token.lexeme + "'", message);
        }
    }

    fun runtimeError(error: RuntimeError) {
        System.err.println(error.message + "\n[line${error.token.line}]")
        hadRuntimeError = true
    }
}
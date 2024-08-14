import lox.*
import java.io.File
import kotlin.system.exitProcess


fun main(args: Array<String>) {
    if (args.size < 2) {
        System.err.println("Usage: ./your_program.sh [commmand] <filename>")
        exitProcess(1)
    }

    val command = args[0]
    val filename = args[1]

    when (command) {
        "tokenize" -> {
            val fileContents = File(filename).readText()
            val scanner = Scanner(fileContents)
            val tokens = scanner.scanTokens()

            tokens.forEach { token -> println(token) }
        }

        "generateAst" -> {
            tools.main(arrayOf("src/main/kotlin/lox"))
            val expression: Expr = Expr.Binary(
                Expr.Unary(
                    Token(TokenType.MINUS, "-", null, 1),
                    Expr.Literal(123)
                ),
                Token(TokenType.STAR, "*", null, 1),
                Expr.Grouping(
                    Expr.Literal(45.67)
                )
            )
        }

        "parse" -> {
            val fileContents = File(filename).readText()
            val scanner = Scanner(fileContents)
            val tokens = scanner.scanTokens()

            val parser = Parser(tokens)
            val expression: Expr? = parser.parse()

            if (Lox.hadError || expression == null) exitProcess(65)

            println(AstPrinter().print(expression))
        }

        "evaluate" -> {
            val fileContents = File(filename).readText()
            val scanner = Scanner(fileContents)
            val tokens = scanner.scanTokens()

            val parser = Parser(tokens)
            val expression: Expr? = parser.parse()
            try {
                if (expression != null)
                    Lox.interpreter.interpret(expression)
            } catch (e: Exception) {
                System.err.println(e.message)
                exitProcess(65)
            }
        }
        else -> {
            System.err.println("Unknown command: $command")
            exitProcess(1)
        }
    }
    if (Lox.hadError) exitProcess(65)
    if (Lox.hadRuntimeError) System.exit(70);
}

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

    val fileContents = File(filename).readText()
    val scanner = Scanner(fileContents)
    val tokens = scanner.scanTokens()

    when (command) {
        "tokenize" -> {
            tokens.forEach { token -> println(token) }
        }

        "generateAst" -> {
            tools.main(arrayOf("src/main/kotlin/lox"))
        }

        "parse" -> {
//            val parser = Parser(tokens)
//            val expression: Expr? = parser.parse()
//
//            if (Lox.hadError || expression == null) exitProcess(65)
//
//            println(AstPrinter().print(expression))
        }

        "evaluate" -> {
            val parser = Parser(tokens)
            val statements: List<Stmt> = parser.parse()
            try {
                Lox.interpreter.interpret(statements)
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

import lox.*
import java.io.File
import kotlin.system.exitProcess


fun main(args: Array<String>) {

    if (args.size == 0) {
        print("> ")
        var input = readlnOrNull()
        while (input != null && input != "quit") {
            val scanner = Scanner(input)
            val tokens = scanner.scanTokens()
            val parser = Parser(tokens)
            val statements: List<Stmt> = parser.parse()

            try {
                Lox.interpreter.interpret(statements)
            } catch (e: Exception) {
                System.err.println(e.message)
                exitProcess(65)
            }

            print("> ")
            System.out.flush()
            input = readlnOrNull()
        }
    }

    if (args[0] == "generateAst") {
        tools.main(arrayOf("src/main/kotlin/lox"))
        exitProcess(0)
    }

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

        "parse" -> {
//            val parser = Parser(tokens)
//            val expression: Expr? = parser.parse()
//
//            if (Lox.hadError || expression == null) exitProcess(65)
//
//            println(AstPrinter().print(expression))
        }

        "evaluate" -> {
//            val parser = Parser(tokens)
//            val expression: Expr? = parser.parse()
//            try {
//                if (expression != null)
//                    Lox.interpreter.interpret(expression)
//            } catch (e: Exception) {
//                System.err.println(e.message)
//                exitProcess(65)
//            }
        }

        "run" -> {
            val parser = Parser(tokens)

            try {
                val statements: List<Stmt> = parser.parse()
                Lox.interpreter.interpret(statements)
            } catch (e: Exception) {
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

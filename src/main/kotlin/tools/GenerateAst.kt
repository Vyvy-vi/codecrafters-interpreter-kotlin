package tools

import java.io.IOException
import java.io.PrintWriter
import kotlin.system.exitProcess

fun defineType(writer: PrintWriter, baseName: String, className: String, fields: List<String>) {
    var typedNames = ""

    fields.forEachIndexed { idx, field ->
        val type = field.split(" ")[0]
        val name = field.split(" ")[1]

        if (idx == fields.size - 1) {
            typedNames += "val $name: $type"
        } else {
            typedNames += "val $name: $type, "
        }
    }

    writer.println("\tclass $className($typedNames) : $baseName() {")
    writer.println("\t\toverride fun <R> accept(visitor: Visitor<R>) = visitor.visit$className$baseName(this)")
    writer.println("\t}")
    writer.println()
}

@Throws(IOException::class)
fun defineAst(outputDir: String, baseName: String, types: List<String>) {
    val path = "$outputDir/$baseName.kt"

    val writer = PrintWriter(path, "utf-8")

    writer.println("package lox")
    writer.println()
    writer.println("import lox.Token")
    writer.println()
    writer.println("// Auto-generated file using GenerateAst.kt")
    writer.println("abstract class $baseName {")

    defineVisitor(writer, baseName, types)

    // AST classes
    for (type: String in types) {
        val className: String = type.split(":")[0].trim()
        val fields: String = type.split(":")[1].trim()
        defineType(writer, baseName, className, fields.split(", "))
    }

    // base accept method
    writer.println()
    writer.println("\tabstract fun <R> accept(visitor: Visitor<R>): R")
    writer.println()

    writer.println("}")
    writer.close()
}

fun defineVisitor(writer: PrintWriter, baseName: String, types: List<String>) {
    writer.println()
    writer.println("\tinterface Visitor<R> {")
    for (type: String in types) {
        val typeName = type.split(":")[0].trim()
        writer.println("\t\tfun visit$typeName$baseName(${baseName.lowercase()}: $typeName): R")
    }
    writer.println("\t}")
    writer.println()
}

@Throws(IOException::class)
fun main(args: Array<String>) {
    if (args.size != 1) {
        System.err.println("Usage: generate_ast <output directory>")
        exitProcess(64)
    }

    val outputDir: String = args[0]
    defineAst(
        outputDir, "Expr", listOf(
            "Binary     : Expr left, lox.Token operator, Expr right",
            "Grouping   : Expr expression",
            "Literal    : Any? value",
            "Unary      : lox.Token operator, Expr right"
        )
    )

}
package lox

import java.lang.StringBuilder

class AstPrinter: Expr.Visitor<String> {
    fun print(expr: Expr): String {
        return expr.accept(this)
    }

    override fun visitUnaryExpr(expr: Expr.Unary): String {
        return paranethisize(expr.operator.lexeme, expr.right)
    }

    override fun visitBinaryExpr(expr: Expr.Binary): String {
        return paranethisize(expr.operator.lexeme, expr.left, expr.right)
    }

    override fun visitLiteralExpr(expr: Expr.Literal): String {
        if (expr.value == null)
            return "nil"
        return expr.value.toString()
    }

    override fun visitGroupingExpr(expr: Expr.Grouping): String {
        return paranethisize("group", expr.expression)
    }

    fun paranethisize(name: String, vararg exprs: Expr): String {
        var builder: StringBuilder = StringBuilder()
        builder.append("(").append(name)

        for (expr in exprs) {
            builder.append(" ")
            builder.append(expr.accept(this))
        }

        builder.append(")")

        return builder.toString()
    }
}
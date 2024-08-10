package lox

import lox.Token

// Auto-generated file using GenerateAst.kt
abstract class Expr {

	interface Visitor<R> {
		fun visitBinaryExpr(expr: Binary): R
		fun visitGroupingExpr(expr: Grouping): R
		fun visitLiteralExpr(expr: Literal): R
		fun visitUnaryExpr(expr: Unary): R
	}

	class Binary(val left: Expr, val operator: lox.Token, val right: Expr) : Expr() {
		override fun <R> accept(visitor: Visitor<R>) = visitor.visitBinaryExpr(this)
	}

	class Grouping(val expression: Expr) : Expr() {
		override fun <R> accept(visitor: Visitor<R>) = visitor.visitGroupingExpr(this)
	}

	class Literal(val value: Any?) : Expr() {
		override fun <R> accept(visitor: Visitor<R>) = visitor.visitLiteralExpr(this)
	}

	class Unary(val operator: lox.Token, val right: Expr) : Expr() {
		override fun <R> accept(visitor: Visitor<R>) = visitor.visitUnaryExpr(this)
	}


	abstract fun <R> accept(visitor: Visitor<R>): R

}

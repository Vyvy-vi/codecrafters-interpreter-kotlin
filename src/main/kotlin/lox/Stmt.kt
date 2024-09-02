package lox

import lox.Token

// Auto-generated file using GenerateAst.kt
abstract class Stmt {

	interface Visitor<R> {
		fun visitExpressionStmt(stmt: Expression): R
		fun visitPrintStmt(stmt: Print): R
	}

	class Expression(val expression: Expr) : Stmt() {
		override fun <R> accept(visitor: Visitor<R>) = visitor.visitExpressionStmt(this)
	}

	class Print(val expression: Expr) : Stmt() {
		override fun <R> accept(visitor: Visitor<R>) = visitor.visitPrintStmt(this)
	}


	abstract fun <R> accept(visitor: Visitor<R>): R

}

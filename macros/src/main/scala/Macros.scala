import scala.quoted._

object Macros {

  inline def assert(expr: => Boolean): Unit =
    ${ assertImpl('expr) }

  def assertImpl(expr: Expr[Boolean])(given qctx: QuoteContext) = '{
    if (!$expr)
      throw new AssertionError(s"failed: ${ ${ Expr(expr.toString) } }")
  }

  inline def copy[A, B](expr: Unit, newValue: A) =
    ${ copyImpl('expr, 'newValue) }

  def copyImpl[A, B](expr: Expr[Unit], newValue: Expr[A])(given qctx: QuoteContext): Expr[B] = {
    import qctx.tasty._
    import util._
    val xTree: Term = expr.unseal.underlyingArgument

    def toCopy(statement: Statement): Expr[B] = statement match {
      case Select(ii@Ident(ident), selector) =>
        '{ ${ii.seal}.copy(${selector} = ${newValue}) }
      case _ =>
        qctx.error(s"Don't like this statement: $statement")
        ???
    }

    xTree match {
      case Block(List(stmt), Literal(Constant(()))) => toCopy(stmt)
      case _ =>
        qctx.error(s"Don't like this tree: $xTree")
        ???
    }
  }
}

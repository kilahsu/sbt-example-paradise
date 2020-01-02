import ModelAnnotations.{Happy, Sad}

import scala.reflect.macros.blackbox
import scala.language.experimental.macros
import scala.annotation.StaticAnnotation

object ModelAnnotations {
  case class Happy()   extends StaticAnnotation
  case class Laughing()   extends StaticAnnotation
  case class Sad()     extends StaticAnnotation
}

object helloMacro {
  def impl(c: blackbox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._
    val result = {
      annottees.map(_.tree).toList match {
        case q"object $name extends ..$parents { ..$body }" :: Nil =>
          val classTypeName = TypeName(name.toString())
          val classType = c.typecheck(q"(None.asInstanceOf[$classTypeName])").tpe
          val members = classType.decls.collect {
            case m: MethodSymbol if m.isCaseAccessor => m
          }
          val printStr = members.map { member =>
            println(s"Checking member ${member.name}, return type is ${member.returnType}")
            member.name + ": " +
              member.accessed.annotations.map {
                annotation =>
                  annotation.tree.tpe.typeSymbol.name
              }.mkString(", ")
          }.mkString("\n")

          q"""
            object $name extends ..$parents {
              def hello: ${typeOf[String]} = $printStr
              ..$body
            }
          """
      }
    }
    c.Expr[Any](result)
  }
}

class hello extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro helloMacro.impl
}

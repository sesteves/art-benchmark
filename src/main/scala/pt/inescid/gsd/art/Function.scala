package pt.inescid.gsd.art

trait Function {

}

object FunctionFactory {

  private case class step() extends Function

  private case class sinusoid() extends Function


  def apply(f: String): Function = {
    f match {
      case "step" => step()
      case "sinusoid" => sinusoid()
    }
  }

}
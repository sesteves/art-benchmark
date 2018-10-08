package pt.inescid.gsd.art.benchmark.workloads

abstract class Workload {

}


object WorkloadFactory {
  def apply(app: String): Workload = {
    app match {
      case "filter" => _
      case "reduce" => _
      case "join" => _
      case "window" => _
      case "stddev" => _
    }
  }
}
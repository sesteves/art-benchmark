package pt.inescid.gsd.art.benchmark.workloads

import org.apache.spark.streaming.dstream.DStream

abstract class Workload {
  def apply(stream: DStream[String]): Unit
}

object WorkloadFactory {
  def apply(app: String): Workload = {
    app match {
      case "filter" => new FilterWorkload
      case "reduce" => new ReduceWorkload
      case "join" => new JoinWorkload
      case "window" => new WindowWorkload
      case _ => throw new IllegalArgumentException(s"No app named '$app'")
    }
  }
}
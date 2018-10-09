package pt.inescid.gsd.art.benchmark.workloads
import org.apache.spark.streaming.dstream.DStream

class LinearRoadWorkload extends Workload {
  override def apply(stream: DStream[String]): Unit = ???
}

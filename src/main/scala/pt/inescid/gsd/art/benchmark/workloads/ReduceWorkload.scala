package pt.inescid.gsd.art.benchmark.workloads
import org.apache.spark.streaming.dstream.DStream

class ReduceWorkload extends Workload {
  override def apply(stream: DStream[String]): Unit = {
    stream.map((_,1)).reduceByKey(_ + _).count().map(c => s"Number of records: $c").print
  }
}

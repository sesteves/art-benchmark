package pt.inescid.gsd.art.benchmark.workloads

import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.dstream.DStream

class WindowWorkload extends Workload {
  override def apply(stream: DStream[String]): Unit = {
    stream.map((_, 1)).reduceByKeyAndWindow(_ + _, Seconds(20)).count().map(c => s"Number of records: $c").print
  }
}

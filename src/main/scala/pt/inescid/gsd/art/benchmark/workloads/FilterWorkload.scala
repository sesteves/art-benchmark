package pt.inescid.gsd.art.benchmark.workloads

import org.apache.spark.streaming.dstream.DStream

import scala.util.Random

class FilterWorkload extends Workload {

  override def apply(stream: DStream[String]): Unit = {
    stream.filter((_) => Random.nextInt(2) == 0).count().map(c => s"Number of records: $c").print
  }
}

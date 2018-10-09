package pt.inescid.gsd.art.benchmark.workloads

import org.apache.spark.streaming.dstream.DStream

import scala.util.Random

class JoinWorkload extends Workload {
  override def apply(stream: DStream[String]): Unit = {
    val s1 = stream.filter((_) => Random.nextInt(2) == 0).map((_, 1))
    val s2 = stream.map((_, 1))
    s1.join(s2).count().map(c => s"Number of records: $c").print

  }
}

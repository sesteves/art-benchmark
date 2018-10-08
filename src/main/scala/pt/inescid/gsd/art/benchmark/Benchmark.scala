package pt.inescid.gsd.art.benchmark

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Duration, StreamingContext}

object Benchmark {

  def main(args: Array[String]): Unit = {
    if (args.length != 6) {
      System.err.println("Usage: Benchmark <numStreams> <host> <port> <batchMillis> <app> <duration>")
      System.exit(1)
    }

    val (numStreams, host, port, batchMillis, app, duration) =
      (args(0).toInt, args(1), args(2).toInt, args(3).toInt, args(4), args(5).toLong)
    val sparkConf = new SparkConf()

    sparkConf.set("spark.art.window.duration", (batchMillis).toString)
    sparkConf.set("spark.akka.heartbeat.interval", "100")

    sparkConf.setAppName(s"art-$app")
    sparkConf.setJars(Array("target/scala-2.10/benchmark-app_2.10-0.1-SNAPSHOT.jar"))
    sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    sparkConf.set("spark.executor.extraJavaOptions", " -XX:+UseCompressedOops -XX:+UseConcMarkSweepGC " +
      "-XX:+AggressiveOpts -XX:FreqInlineSize=300 -XX:MaxInlineSize=300 ")
    if (sparkConf.getOption("spark.master") == None) {
      // Master not set, as this was not launched through Spark-submit. Setting master as local."
      sparkConf.setMaster("local[*]")
    }

    // Create the context
    val ssc = new StreamingContext(sparkConf, Duration(batchMillis))

    val rawStreams = (1 to numStreams).map(_ =>
      ssc.rawSocketStream[String](host, port, StorageLevel.MEMORY_ONLY_SER)).toArray
    val stream = ssc.union(rawStreams).flatMap(_.split(' '))


    ssc.start()
    ssc.awaitTermination(duration)
  }

}

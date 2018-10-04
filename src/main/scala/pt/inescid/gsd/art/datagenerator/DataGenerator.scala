package pt.inescid.gsd.art.datagenerator

import java.io.{ByteArrayOutputStream, IOException}
import java.net.ServerSocket
import java.nio.ByteBuffer

import org.apache.spark.SparkConf
import org.apache.spark.serializer.KryoSerializer
import org.glassfish.tyrus.server.Server
import pt.inescid.gsd.art.datagenerator.outputstreams.{ArtOutputStream, ArtOutputStreamFactory}

import scala.util.Random

object DataGenerator {

  val WSServerPort = 3030

  def main(args: Array[String]) {
    if (args.length < 5) {
      System.err.println("Usage: DataGenerator <port> <function> <minBytesPerSec> <maxBytesPerSec> <period>")
      System.exit(1)
    }

    // Parse the arguments using a pattern match
    val (port, function, minBps, maxBps, period) = (args(0).toInt, args(1), args(2).toInt, args(3).toInt, args(4).toInt)

    // generate data to send
    val array = getData(minBps)
    val countBuf = ByteBuffer.wrap(new Array[Byte](4))
    countBuf.putInt(array.length)
    countBuf.flip()

    val serverSocket = new ServerSocket(port)
    println("Listening on port " + port)

    while (true) {
      val socket = serverSocket.accept()
      println("Got a new connection")

      val out = ArtOutputStreamFactory(function, socket.getOutputStream, minBps, maxBps, period)
      val wsServer = startWSServer(out)

      try {
        while (true) {
          out.write(countBuf.array)
          out.write(array)
        }
      } catch {
        case _: IOException =>
          println("Client disconnected")
      } finally {
        wsServer.stop()
        socket.close()
      }
    }
  }

  def getData(minBps: Int): Array[Byte] = {
    val blockSize = minBps / 10
    val bufferStream = new ByteArrayOutputStream(blockSize + 1000)
    val ser = new KryoSerializer(new SparkConf()).newInstance()
    val serStream = ser.serializeStream(bufferStream)

    // generates 105 bytes lines with alphanumeric chars
    def generate = (1 to 15).map(_ => Random.alphanumeric.take(6).mkString("")).mkString(" ")

    while (bufferStream.size() < blockSize) {
      serStream.writeObject(generate)
    }
    bufferStream.toByteArray
  }

  def startWSServer(artOS: ArtOutputStream): Server = {
//    val properties = new java.util.HashMap[String, AnyRef]
//    properties.put(Observer.ObserverKey, artOS)
//
//    val server = new Server("localhost", WSServerPort, "/art", properties, classOf[DataGeneratorEndpoint])
//    server.start()
//    server
    null
  }
}

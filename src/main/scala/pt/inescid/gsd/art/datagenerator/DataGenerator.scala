package pt.inescid.gsd.art.datagenerator

import java.io.{ByteArrayOutputStream, IOException}
import java.net.{InetSocketAddress, ServerSocket}
import java.nio.ByteBuffer

import org.apache.spark.SparkConf
import org.apache.spark.serializer.KryoSerializer
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import pt.inescid.gsd.art.datagenerator.outputstreams.{ArtOutputStream, ArtOutputStreamFactory}

import scala.util.Random

object DataGenerator extends WebSocketServer(new InetSocketAddress(3030)) {

  val CommandSeparator = "::"

  var artOutputStream: ArtOutputStream = null

  def main(args: Array[String]) {
    if (args.length < 5) {
      System.err.println("Usage: DataGenerator <port> <function> <minBytesPerSec> <maxBytesPerSec> <period>")
      System.exit(1)
    }

    // Parse the arguments using a pattern match
    val (port, function, minBps, maxBps, period) = (args(0).toInt, args(1), args(2).toInt, args(3).toInt, args(4).toInt)

    // starting web socket server
    start()

    // generate data to send
    val array = getData(minBps)
    val countBuf = ByteBuffer.wrap(new Array[Byte](4))
    countBuf.putInt(array.length)
    countBuf.flip()

    val serverSocket = new ServerSocket(port)
    println("Listening on port " + port)

    try {
      while (true) {
        val socket = serverSocket.accept()
        println("Got a new connection")

        val out = ArtOutputStreamFactory(function, socket.getOutputStream, minBps, maxBps, period)
        artOutputStream = out

        try {
          while (true) {
            out.write(countBuf.array)
            out.write(array)
          }
        } catch {
          case _: IOException =>
            println("Client disconnected")
        } finally {
          artOutputStream = null
          socket.close()
        }
      }

    } finally {
      stop()
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

  override def onStart(): Unit = {
    println("WS server started.")
  }

  def onOpen(conn: WebSocket, handshake: ClientHandshake): Unit = {
    println("Connection opened. Waiting for commands...")
  }

  def onMessage(conn: WebSocket, command: String): Unit = {
    println(s"Received command '$command'")

    if (artOutputStream != null) {
      val commandParts = command.split(CommandSeparator)
      val op = commandParts.head
      val value = commandParts.last

      op match {
        case "set-min" => artOutputStream.setMinBps(value.toInt)
        case "set-max" => artOutputStream.maxBps = value.toInt
        case "set-period" => artOutputStream.period = value.toInt
        case _ => println(s"Invalid operation: $op")
      }
    } else {
      println("There is no active session to execute command.")
    }
  }

  def onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean): Unit = {
    println("Connection closed.")
  }

  override def onError(conn: WebSocket, ex: Exception): Unit = {
    ex.printStackTrace()
  }
}

package pt.inescid.gsd.art

import java.io.{ByteArrayOutputStream, IOException}
import java.net.ServerSocket

import javax.websocket.DeploymentException
import org.glassfish.tyrus.server.Server
import pt.inescid.gsd.art.outputstreams.ArtOutputStreamFactory

import scala.collection.mutable


object DataGenerator {


  def main(args: Array[String]) {
    if (args.length < 6) {
      System.err.println("Usage: DataGenerator <port> <file> <function> <minBytesPerSec> <maxBytesPerSec> <period>")
      System.exit(1)
    }

    // Parse the arguments using a pattern match
    val (port, file, function, minBytesPerSec, maxBytesPerSec, period) = (args(0).toInt, args(1),
      args(2), args(3).toInt, args(4).toInt, args(5).toInt)


    // data to send
    val bufferStream = new ByteArrayOutputStream()


    val serverSocket = new ServerSocket(port)
    println("Listening on port " + port)

    while (true) {
      val socket = serverSocket.accept()
      println("Got a new connection")

      val out = ArtOutputStreamFactory(function, socket.getOutputStream, minBytesPerSec, maxBytesPerSec, period)

      try {
        while (true) {




        }
      } catch {
        case e: IOException =>
          println("Client disconnected")
          socket.close()
      }

    }


  }

  def startWS = {

    val properties = new java.util.HashMap[String, AnyRef]

    val server =
      new Server("localhost", 8025, "/ws", properties, DataGeneratorEndpoint.getClass)

    try {
      server.start
      this.wait()

    } catch {
      case e: DeploymentException =>
        throw new RuntimeException(e)
    } finally {
      server.stop
    }
  }
}

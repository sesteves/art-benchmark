package pt.inescid.gsd.art

import java.util.Scanner

import javax.websocket.DeploymentException
import org.glassfish.tyrus.server.Server


object DataGenerator {

  def startWS = {
    // server
    val server = new Server("localhost", 8025, "/ws", null,
      DataGeneratorEndpoint.getClass)
    try {
      server.start
      System.out.println("Press any key to stop the server..")
      new Scanner(System.in).nextLine
    } catch {
      case e: DeploymentException =>
        throw new RuntimeException(e)
    } finally {
      server.stop
    }
  }

  def main(args: Array[String]) {
    if (args.length < 6) {
      System.err.println("Usage: DataGenerator <port> <file> <tendency> <minBytesPerSec> <maxBytesPerSec> " +
        "<stepDuration> [<stepBytes>]")
      System.exit(1)
    }

    // Parse the arguments using a pattern match
    val (port, file, functionStr, minBytesPerSec, maxBytesPerSec, stepDuration) = (args(0).toInt, args(1),
      args(2), args(3).toInt, args(4).toInt, args(5).toInt)

    val function = FunctionFactory(functionStr)

  }
}

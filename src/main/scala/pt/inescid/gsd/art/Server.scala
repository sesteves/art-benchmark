package pt.inescid.gsd.art

import java.util.Scanner

import javax.websocket.DeploymentException
import org.glassfish.tyrus.server.Server


object Server {

  def main(args: Array[String]): Unit = {
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
}
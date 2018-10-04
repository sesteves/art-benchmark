package pt.inescid.gsd.art.datagenerator

import java.io._
import java.net.URI

import javax.websocket._
import javax.websocket.server.ServerEndpointConfig
import org.glassfish.tyrus.client.ClientManager
import org.glassfish.tyrus.server.Server
import org.glassfish.tyrus.spi.ServerContainerFactory
import org.scalatest.FlatSpec
import pt.inescid.gsd.art.datagenerator.DataGenerator.WSServerPort
import pt.inescid.gsd.art.datagenerator.outputstreams.{ArtOutputStream, ConstantOutputStream}

class DataGeneratorEndpointTest extends FlatSpec {

  val Server = "ws://localhost:3030/art/datagen"

  "DataGeneratorEndpoint" should "receive messages" in {

    val os = new ByteArrayOutputStream(100)

    val constantOutputStream = new ConstantOutputStream(os, 1, 1, 1)

    class DataGeneratorEndpointConfigurator(artOS: ArtOutputStream) extends ServerEndpointConfig.Configurator {
      override def getEndpointInstance[T](endpointClass: Class[T]): T = {
        new DataGeneratorEndpoint().asInstanceOf[T]
      }
    }

    val serverConfig = ServerEndpointConfig.Builder.create(classOf[DataGeneratorEndpoint], "/datagen")
      .configurator(new DataGeneratorEndpointConfigurator(constantOutputStream)).build()

    val server = ServerContainerFactory.createServerContainer()
    server.addEndpoint(serverConfig)
//    server.addEndpoint(classOf[DataGeneratorEndpoint])

    try {
      server.start("/art", WSServerPort)
      Thread.sleep(1000)

      val client = ClientManager.createClient()
      val session = client.connectToServer(ClientEndpoint, new URI(Server))
//      session.getBasicRemote.sendText("set-min::9", true)
//
//      assert(constantOutputStream.minBps == 9)

    } catch {
      case e: IOException => e.printStackTrace()
    } finally {
      server.stop()
    }
  }

  import javax.websocket.OnOpen

  @javax.websocket.ClientEndpoint()
  object ClientEndpoint {

    @OnOpen
    def onOpen(session: Session) = {
      println(s"Connection established. session id: ${session.getId}")
    }

    @OnMessage
    def onMessage(message: String): Unit = {
      println(s"Received message: $message")
    }

    @OnError
    def onError(e: Throwable): Unit = {
      e.printStackTrace()
    }
  }

}

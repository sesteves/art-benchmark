package pt.inescid.gsd.art.datagenerator

import java.net.URI

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.scalatest.FlatSpec
import pt.inescid.gsd.art.datagenerator.outputstreams.ConstantOutputStream

class DataGeneratorTest extends FlatSpec {

  val Server = "ws://localhost:3030"

  "DataGenerator" should "receive commands and update ingestion rate" in {

    val constantOutputStream = new ConstantOutputStream(null, 1, 1, 1)
    DataGenerator.artOutputStream = constantOutputStream
    DataGenerator.start()

    try {
      val client = new WebSocketClient((new URI(Server))) {
        override def onMessage(message: String): Unit = {
        }
        override def onOpen(handshake: ServerHandshake): Unit = {
        }
        override def onClose(code: Int, reason: String, remote: Boolean): Unit = {
        }
        override def onError(ex: Exception): Unit = {
          ex.printStackTrace()
        }
      }
      client.connect()
      Thread.sleep(100)

      client.send("set-min::7")
      client.send("set-max::8")
      client.send("set-period::9")
      Thread.sleep(100)

      assert(constantOutputStream.minBps == 7)
      assert(constantOutputStream.maxBps == 8)
      assert(constantOutputStream.period == 9)
    } finally {
      DataGenerator.stop()
    }
  }
}

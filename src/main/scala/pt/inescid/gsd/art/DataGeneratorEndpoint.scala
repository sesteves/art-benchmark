package pt.inescid.gsd.art

import java.io.IOException

import javax.websocket._
import javax.websocket.server.ServerEndpoint

@ServerEndpoint("/art-datagen")
object DataGeneratorEndpoint {

  @OnOpen
  def onOpen(session: Session): Unit = {
    println("Session opened, id: %s%n", session.getId)
    try
      session.getBasicRemote.sendText("Hi there, we are successfully connected.")
    catch {
      case ex: IOException =>
        ex.printStackTrace()
    }
  }

  @OnMessage
  def onMessage(message: String, session: Session): Unit = {
    println("Message received. Session id: %s Message: %s%n", session.getId, message)
    try
      session.getBasicRemote.sendText(String.format("We received your message: %s%n", message))
    catch {
      case ex: IOException =>
        ex.printStackTrace()
    }
  }

  @OnError
  def onError(e: Throwable): Unit = {
    e.printStackTrace()
  }

  @OnClose
  def onClose(session: Session): Unit = {
    println("Session closed with id: %s%n", session.getId)
  }
}
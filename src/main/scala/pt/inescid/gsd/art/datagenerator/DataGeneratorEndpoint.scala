package pt.inescid.gsd.art.datagenerator

import javax.websocket._
import javax.websocket.server.ServerEndpoint

@ServerEndpoint("/datagen")
class DataGeneratorEndpoint() { // extends Subject[DataGeneratorEndpoint] {

  val MessageSeparator = "::"

  var minBps: Int = _
  var maxBps: Int = _
  var period: Int = _

  @OnOpen
  def onOpen(session: Session, config: EndpointConfig): Unit = {
    println(s"Session opened, id: ${session.getId}")
  }

  @OnMessage
  def onMessage(message: String, session: Session): Unit = {
    println(s"Message received. Session id: ${session.getId} Message: $message")
    val messageParts = message.split(MessageSeparator)
    val op = messageParts.head
    val value = messageParts.last

    op match {
      case "set-min" => minBps = value.toInt
      case "set-max" => maxBps = value.toInt
      case "set-period" => period = value.toInt
    }

    // notifyObservers
  }

  @OnError
  def onError(e: Throwable): Unit = {
    e.printStackTrace()
  }

  @OnClose
  def onClose(session: Session): Unit = {
    println(s"Session closed with id: ${session.getId}")
  }

}

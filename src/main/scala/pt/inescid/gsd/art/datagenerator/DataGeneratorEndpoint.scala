package pt.inescid.gsd.art.datagenerator

import javax.websocket._
import javax.websocket.server.ServerEndpoint
import pt.inescid.gsd.art.datagenerator.Observer.Subject
import pt.inescid.gsd.art.datagenerator.outputstreams.ArtOutputStream

@ServerEndpoint("/art-datagen")
class DataGeneratorEndpoint extends Subject[DataGeneratorEndpoint] {

  val MessageSeparator = "::"

  var minBps: Int = _
  var maxBps: Int = _
  var period: Int = _

  @OnOpen
  def onOpen(session: Session, endpointConfig: EndpointConfig): Unit = {
    println("Session opened, id: %s%n", session.getId)

    val artOS = endpointConfig.getUserProperties().get(Observer.ObserverKey).asInstanceOf[ArtOutputStream]
    addObserver(artOS)
  }

  @OnMessage
  def onMessage(message: String, session: Session): Unit = {
    println("Message received. Session id: %s Message: %s%n", session.getId, message)
    val messageParts = message.split(MessageSeparator)
    val op = messageParts.head
    val value = messageParts.last

    op match {
      case "set-min" => minBps = value.toInt
      case "set-max" => maxBps = value.toInt
      case "set-period" => period = value.toInt
    }

    notifyObservers
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

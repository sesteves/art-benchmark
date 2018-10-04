package pt.inescid.gsd.art.datagenerator

object Observer {

  val ObserverKey = "observer"

  trait Observer[S] {
    def update(subject: S)
  }

  trait Subject[S] {
    this: S =>
    private var observers: List[Observer[S]] = Nil

    def addObserver(observer: Observer[S]) =  {
      println("adding observer!")
      observers = observer :: observers
      println(s"observers size: ${observers.size}")
    }

    def notifyObservers = {
      println(s"Notify observers size: ${observers.size}")
      observers.foreach(_.update(this))
    }
  }
}
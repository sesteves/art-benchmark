package pt.inescid.gsd.art.datagenerator

object Observer {

  val ObserverKey = "observer"

  trait Observer[S] {
    def update(subject: S)
  }

  trait Subject[S] {
    this: S =>
    private var observers: List[Observer[S]] = Nil

    def addObserver(observer: Observer[S]) = observers = observer :: observers

    def notifyObservers = observers.foreach(_.update(this))
  }
}
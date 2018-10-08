package pt.inescid.gsd.art.datagenerator.outputstreams

import java.io.OutputStream

class SinusoidOutputStream(out: OutputStream, minBpss: Int, maxBpss: Int, periods: Int)
  extends ArtOutputStream(out, minBpss, maxBpss, periods) {

  val stepRatio = 0.05

  var count = 0

  var lastTick = System.currentTimeMillis()

  override def write(bytes: Array[Byte]): Unit = {
    val duration = stepRatio * period

    val tick = System.currentTimeMillis()

    while (tick - lastTick > duration) {
      setCurrentBps

      count += 1
      lastTick = tick
    }

    write(bytes, 0, bytes.length)
  }

  def setCurrentBps: Unit = {
    val n = (1 / stepRatio).toInt
    val step = (stepRatio * 2 * (maxBps - minBps)).toInt
    val c = (count % n) + 1

    // in the 1s half of the period the input rate should be ascending
    if (c <= n / 2) {
      currentBps = minBps + c * step
    } else {
      currentBps = minBps + (n - c) * step
    }
  }

  override def setMinBps(bps: Int): Unit = {
    minBps = bps
    setCurrentBps
  }

  override def setMaxBps(bps: Int): Unit = {
    maxBps = bps
    setCurrentBps
  }

  override def setPeriod(period: Int): Unit = this.period = period
}

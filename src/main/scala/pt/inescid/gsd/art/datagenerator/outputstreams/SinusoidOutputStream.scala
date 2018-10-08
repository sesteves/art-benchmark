package pt.inescid.gsd.art.datagenerator.outputstreams

import java.io.OutputStream

class SinusoidOutputStream(out: OutputStream, minBps: Int, maxBps: Int, period: Int)
  extends ArtOutputStream(out, minBps, maxBps, period) {

  val stepRatio = 0.05

  var count = 0

  var lastTick = System.currentTimeMillis()

  override def write(bytes: Array[Byte]): Unit = {
    val duration = stepRatio * period

    val tick = System.currentTimeMillis()

    while (tick - lastTick > duration) {
      val n = period / duration
      val step = (stepRatio * 2 * (maxBps - minBps)).toInt

      // in the 1s half of the period the input rate should be ascending
      if (count % n < n / 2) {
        currentBps += step
      } else {
        currentBps -= step
      }

      count += 1
      lastTick = tick
    }

    write(bytes, 0, bytes.length)
  }

  override def setMinBps(bps: Int): Unit = ???

  override def setMaxBps(bps: Int): Unit = ???

  override def setPeriod(period: Int): Unit = ???
}

package pt.inescid.gsd.art.datagenerator.outputstreams

import java.io.OutputStream

class StepOutputStream(out: OutputStream, minBpss: Int, maxBpss: Int, periods: Int)
  extends ArtOutputStream(out, minBpss, maxBpss, periods) {

  private var isPhaseZero = true

  private var lastTick = System.currentTimeMillis()

  override def write(bytes: Array[Byte]): Unit = {
    val tick = System.currentTimeMillis()

    if (tick - lastTick > period / 2) {
      currentBps = if (isPhaseZero) maxBps else minBps

      isPhaseZero = !isPhaseZero
      lastTick = tick
    }

    write(bytes, 0, bytes.length)
  }

  override def setMinBps(bps: Int): Unit = {
    minBps = bps
    if(isPhaseZero) {
      currentBps = minBps
    }
  }

  override def setMaxBps(bps: Int): Unit = {
    maxBps = bps
    if(!isPhaseZero) {
      currentBps = maxBps
    }
  }

  override def setPeriod(period: Int) = this.period = period
}

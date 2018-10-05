package pt.inescid.gsd.art.datagenerator.outputstreams

import java.io.OutputStream

class StepOutputStream(out: OutputStream, minBps: Int, maxBps: Int, period: Int)
  extends ArtOutputStream(out, minBps, maxBps, period) {

  private val phaseBps = Array(minBps, maxBps)

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

}

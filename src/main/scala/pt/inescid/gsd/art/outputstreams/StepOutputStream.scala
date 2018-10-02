package pt.inescid.gsd.art.outputstreams

import java.io.OutputStream

class StepOutputStream(out: OutputStream, minBytesPerSec: Int, maxBytesPerSec: Int, period: Int)
  extends ArtOutputStream(out, minBytesPerSec, maxBytesPerSec, period) {

  private val halfPeriod = period / 2

  private var lastTick = System.currentTimeMillis()

  override def write(bytes: Array[Byte]): Unit = {
    val tick = System.currentTimeMillis()


    if (tick - lastTick > halfPeriod) {

      if (currentBytesPerSec == minBytesPerSec) {
        currentBytesPerSec = maxBytesPerSec
      } else {
        currentBytesPerSec = minBytesPerSec
      }

      lastTick = tick
    }

    write(bytes, 0, bytes.length)
  }

}

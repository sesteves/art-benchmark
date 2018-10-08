package pt.inescid.gsd.art.datagenerator.outputstreams

import java.io.OutputStream

class ConstantOutputStream(out: OutputStream, minBpss: Int, maxBpss: Int, periods: Int)
  extends ArtOutputStream(out, minBpss, maxBpss, periods) {

  override def write(bytes: Array[Byte]): Unit = {
    write(bytes, 0, bytes.length)
  }

  override def setMinBps(bps: Int): Unit = currentBps = bps

  override def setMaxBps(bps: Int): Unit = currentBps = bps

  override def setPeriod(period: Int): Unit = this.period = period
}

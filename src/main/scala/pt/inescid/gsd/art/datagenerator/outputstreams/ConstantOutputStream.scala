package pt.inescid.gsd.art.datagenerator.outputstreams

import java.io.OutputStream

class ConstantOutputStream(out: OutputStream, minBps: Int, maxBps: Int, period: Int)
  extends ArtOutputStream(out, minBps, maxBps, period) {

  override def write(bytes: Array[Byte]): Unit = {
    currentBps = minBps
  }
}

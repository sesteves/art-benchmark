package pt.inescid.gsd.art.outputstreams

import java.io.OutputStream

class SinusoidOutputStream(out: OutputStream, minBytesPerSec: Int, maxBytesPerSec: Int, period: Int)
  extends ArtOutputStream(out, minBytesPerSec, maxBytesPerSec, period) {

  override def write(bytes: Array[Byte]) = ???
}

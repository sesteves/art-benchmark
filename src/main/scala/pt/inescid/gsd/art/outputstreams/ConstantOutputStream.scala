package pt.inescid.gsd.art.outputstreams

import java.io.OutputStream

class ConstantOutputStream (out: OutputStream, minBytesPerSec: Int, maxBytesPerSec: Int, period: Int)
  extends ArtOutputStream(out, minBytesPerSec, maxBytesPerSec, period) {


}

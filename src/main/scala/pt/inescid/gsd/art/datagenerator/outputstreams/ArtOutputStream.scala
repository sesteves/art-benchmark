package pt.inescid.gsd.art.datagenerator.outputstreams

import java.io.OutputStream
import java.util.concurrent.TimeUnit.{NANOSECONDS, SECONDS}

import scala.annotation.tailrec

abstract class ArtOutputStream(out: OutputStream, var minBps: Int, var maxBps: Int, var period: Int)
  extends OutputStream {

  require(minBps > 0)
  require(maxBps >= minBps)

  private val SYNC_INTERVAL = NANOSECONDS.convert(10, SECONDS)
  private val CHUNK_SIZE = 8192
  private var lastSyncTime = System.nanoTime
  private var bytesWrittenSinceSync = 0L
  protected var currentBps = minBps

  def write(bytes: Array[Byte]): Unit

  override def write(b: Int) {
    waitToWrite(1)
    out.write(b)
  }

  @tailrec
  override final def write(bytes: Array[Byte], offset: Int, length: Int) {
    val writeSize = math.min(length - offset, CHUNK_SIZE)
    if (writeSize > 0) {
      waitToWrite(writeSize)
      out.write(bytes, offset, writeSize)
      write(bytes, offset + writeSize, length)
    }
  }

  override def flush() {
    out.flush()
  }

  override def close() {
    out.close()
  }

  private def waitToWrite(numBytes: Int) {
    val now = System.nanoTime
    val elapsedNanosecs = math.max(now - lastSyncTime, 1)
    val rate = bytesWrittenSinceSync.toDouble * 1e9 / elapsedNanosecs

    if (rate < currentBps) {
      // It's okay to write; just update some variables and return
      bytesWrittenSinceSync += numBytes
      if (now > lastSyncTime + SYNC_INTERVAL) {
        println(s"Rate = $rate bytes/sec")
        // Sync interval has passed; let's resync
        lastSyncTime = now
        bytesWrittenSinceSync = numBytes
      }
    } else {
      // Calculate how much time we should sleep to bring ourselves to the desired rate.
      val targetTimeInMillis = bytesWrittenSinceSync * 1000 / currentBps
      val elapsedTimeInMillis = elapsedNanosecs / 1000000
      val sleepTimeInMillis = targetTimeInMillis - elapsedTimeInMillis
      if (sleepTimeInMillis > 0) {
        Thread.sleep(sleepTimeInMillis)
      }
      waitToWrite(numBytes)
    }
  }
}

object ArtOutputStreamFactory {

  def apply(function: String, out: OutputStream, minBps: Int, maxBps: Int, period: Int):
  ArtOutputStream = {

    function match {
      case "step" => new StepOutputStream(out,minBps, maxBps, period)
      case "sinusoid" => ???
    }

  }

}
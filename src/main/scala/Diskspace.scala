import sys.process._
import collection.immutable.SortedMap

class StorageUnit(val bytes: Long) extends Ordered[StorageUnit] {
  def inBytes = bytes

  def inKilobytes = bytes / (1024L)

  def inMegabytes = bytes / (1024L * 1024)

  def inGigabytes = bytes / (1024L * 1024 * 1024)

  def inTerabytes = bytes / (1024L * 1024 * 1024 * 1024)

  def inPetabytes = bytes / (1024L * 1024 * 1024 * 1024 * 1024)

  def inExabytes = bytes / (1024L * 1024 * 1024 * 1024 * 1024 * 1024)

  def +(that: StorageUnit): StorageUnit = new StorageUnit(this.bytes + that.bytes)

  def -(that: StorageUnit): StorageUnit = new StorageUnit(this.bytes - that.bytes)

  def *(scalar: Double): StorageUnit = new StorageUnit((this.bytes.toDouble * scalar).toLong)

  override def equals(other: Any) = {
    other match {
      case other: StorageUnit =>
        inBytes == other.inBytes
      case _ =>
        false
    }
  }

  override def compare(other: StorageUnit) =
    if (bytes < other.bytes) -1 else if (bytes > other.bytes) 1 else 0

  override def toString() = inBytes + ".bytes"

  def toHuman(): String = {
    val prefix = "KMGTPE"
    var prefixIndex = -1
    var display = bytes.toDouble.abs
    while (display > 1126.0) {
      prefixIndex += 1
      display /= 1024.0
    }
    if (prefixIndex < 0) {
      "%d B".format(bytes)
    } else {
      "%.1f %cB".format(display * bytes.signum, prefix.charAt(prefixIndex))
    }
  }
}

object Main {

  def main(args: Array[String]) {
    println("To get help use the '-h' switch")
    if (args.isEmpty || args(0) == "-h") {
      println("""

        Usage scala Diskspace <path> [excludes...]

        e.g. Diskspace /home
        e.g. Diskspace / --exclude="proc"

              """
      )
      sys.exit(0)
    }

    val path = if (args.length == 1) args(0) else "/"
    val exclude = if (args.length > 1) args.drop(1).toList.mkString(" ") else "--exclude=/home/.ecryptfs --exclude=/proc"
    val color = false
    //    val path = args(0)
    //    val exclude = args(1)
    //    val color = true

    println(Console.BLINK + s" scanning $path")
    println(Console.BLINK + s" ignoring $exclude")

    val result = s"sudo du $exclude -b -d 1 $path" #| "tr \"\\t\" \",\"\"" !!

    val resultMap = result.split("\n").map(_ split ",").foldLeft(Map[Long, String]())((m, s) => m + (s(0).toLong -> s(1)))

    val resultMapSorted = resultMap.toSeq.sortBy(_._1).reverse

    if (color) {
      for ((k, v) <- resultMapSorted) println(Console.YELLOW + v + Console.WHITE + " -> " + Console.GREEN + new StorageUnit(k).toHuman + Console.RESET)
    } else {
      for ((k, v) <- resultMapSorted) println(s" $v  ->  " + new StorageUnit(k).toHuman)
    }

  }
}

Main.main(args)

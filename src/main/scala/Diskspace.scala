import sys.process._
import collection.immutable.SortedMap

case class StorageUnit(val bytes: Long) extends Ordered[StorageUnit] {
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

        Usage scala Diskspace <host> <path> [excludes...]

        e.g. Diskspace localhost /home/user
        e.g. Diskspace user@server /home/user
        e.g. Diskspace user@server /home/user --exclude="proc"

              """
      )
      sys.exit(0)
    }
    
    val arg0 = args(0)
    val host = if(args(0) != "localhost") s"ssh $arg0 " else "localhost"
    val path = args(1)
    val exclude = if (args.length > 2) args.drop(2).toList.mkString(" ") else ""
    val color = false
    //    val color = true

    println(s" scan on [$host]")
    println(s" folder [$path]")
    println(s" ignoring [$exclude]")

    val command1 = s"du -k -d 1 $path $exclude" 
    val command2 = s"$host du -k -d 1 $path $exclude" 
    val translate =  "tr \"\\t\" \",\"\"" 
    val command = if (host == "localhost") s"$command1" #| s"$translate" else s"$command2" #| s"$translate"

    println(s"Executing: [$command]")
    val result = command!!

    val resultMap = result.split("\n").map(_ split ",").foldLeft(Map[Long, String]())((m, s) => m + (s(0).toLong -> s(1)))

    val resultMapSorted = resultMap.toSeq.sortBy(_._1).reverse

    if (color) {
      for ((k, v) <- resultMapSorted) println(Console.YELLOW + v + Console.WHITE + " -> " + Console.GREEN + new StorageUnit(k).toHuman + Console.RESET)
    } else {
      for ((k, v) <- resultMapSorted) println(s" $v  ->  " +  StorageUnit(k*1024).toHuman)
    }

  }
}

Main.main(args)

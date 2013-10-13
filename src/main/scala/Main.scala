import java.io.File
import java.nio.file.{Files, Path}

object Main {
  def getFolderSize(p: Path): Long = {
    Files.size(p)
  }

  def main(args: Array[String]) {
    println("Hello, World!")
    println("# of arguments: %d" format count(args))
  }

  def count[T](it: Iterable[T]): Int = it.size
}
//import java.io.{FileOutputStream, File}
//import java.nio.file.Path
//import org.scalatest.FunSuite
//
//class MainSuite extends FunSuite {
//  test("counting an empty collection") {
//    assert(Main.count(Map()) == 0)
//    assert(Main.count(Set()) == 0)
//  }
//
//  test("counting a non-empty collection") {
//    assert(Main.count(Array(1)) == 1)
//    assert(Main.count(Map(1 -> 1)) == 1)
//    assert(Main.count(Set(1)) == 1)
//  }
//
//  test("get file size") {
//    val folder = System.getProperty("java.io.tmpdir")
//    val file = new File(folder, "file1.txt")
//    fillFileToSize(file, 5)
//    println(file.getAbsolutePath)
//    assert(Main.getFolderSize(file.toPath) == 5)
//  }
//
//
//  def fillFileToSize(file: File ,size: Int) {
//    val stream = new FileOutputStream(file);
//
//    val buff = new Array[Byte](size)
//    stream.write(buff)
//    stream.flush()
//    stream.close()
//  }
//
//
//}
//
//class ScalaByte{
//  val bytes:Array[Byte] = "this is my test".getBytes()
//}
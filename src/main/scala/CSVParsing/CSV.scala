package CSVParsing

import scala.io.Source

final case class ReadResult[A](lignes: Iterable[A], nbInvalidLine : Int)

object CSV {

  //fonction non recursive
  def read[A](fileName: String, parseLine: Array[String] => Option[A], regex: String = ","): ReadResult[A] = {

    val SourceCsv = Source.fromFile(fileName)
    val linesWithoutHeader : Iterable[String] = SourceCsv.getLines().to(Iterable).drop(1)
    val parsedLine : Iterable[Option[A]] = linesWithoutHeader.map{ x => parseLine(x.split(regex).map(_.trim)) }
    val invalidLine = parsedLine.count{ x => x.isEmpty }


    ReadResult(parsedLine.flatten, invalidLine)
  }
}

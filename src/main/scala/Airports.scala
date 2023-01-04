import scala.util.Try
import scalikejdbc._


final case class Airports(ID : String, Type : String, Name : String, Country : String)

object Airports extends SQLSyntaxSupport[Airports] {

  def fromCsvLine(line : Array[String]): Option[Airports] =
    line.length match {
      case 14 => parseAirports(line)
      case _ => None
    }

  def parseAirports(line : Array[String]): Option[Airports] =
    (Try(line(1).substring(1,line(1).length-1)).toOption,Try(line(2).substring(1,line(2).length-1)).toOption, Try(line(3).substring(1,line(3).length-1)).toOption, Try(line(8).substring(1,line(8).length-1)).toOption) match {
      case (Some(x),Some(y),Some(z),Some(a)) if a.length == 2 => Some(Airports(x,y,z,a))
      case _ => None
    }

  override val tableName = "airport"
  def apply(rs: WrappedResultSet) = new Airports(
    rs.string(1), rs.string(2), rs.string(3), rs.string(4))
}

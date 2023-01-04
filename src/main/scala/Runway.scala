import scala.util.Try
import scalikejdbc._


final case class Runway(ID : String, Surface : String, ID_lat : String)

object Runway extends SQLSyntaxSupport[Runway] {

  def fromCsvLine(line : Array[String]): Option[Runway] =
    line.length match {
      case 20 => parseRunway(line)
      case _ => None
    }

  def parseRunway(line : Array[String]): Option[Runway] =
    (Try(line(2).substring(1,line(2).length-1)).toOption, Try(line(5).substring(1,line(5).length-1)).toOption, Try(line(8).substring(1,line(8).length-1)).toOption) match {
      case (Some(x),Some(y),Some(z)) => Some(Runway(x,y,z))
      case _ => None
    }

  override val tableName = "runway"
  def apply(rs: WrappedResultSet) = new Runway(
    rs.string(1), rs.string(2), rs.string(3))
}

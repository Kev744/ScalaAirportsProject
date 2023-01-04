import scala.util.Try
import scalikejdbc._

final case class Country(Code : String, Name : String, Continent : String)

object Country extends SQLSyntaxSupport[Country] {

  override val tableName = "countries"
  def apply(rs: WrappedResultSet) = new Country(
    rs.string("code"), rs.string("name"), rs.string("continent"))

  def fromCsvLine(line : Array[String]): Option[Country] =
    line.length match {
      case 5 | 6 => parseCountry(line)
      case _ => None
    }

  def parseCountry(line : Array[String]): Option[Country] =
    (Try(line(1).substring(1,line(1).length-1)).toOption, Try(line(2).substring(1,line(2).length-1)).toOption, Try(line(3).substring(1,line(3).length-1)).toOption) match {
      case (Some(x),Some(y),Some(z)) if x.length == 2 => Some(Country(x,y,z))
      case _ => None
    }
}
import scalikejdbc._
import scalikejdbc.config._
import sqls.count

object DataBase {
/*
  sql"""
create table countries (
  code  varchar(2) check length(code) = 2,
  name varchar(64) ,
  continent varchar(2)  check length(continent) = 2
)
""".execute.apply()

  sql"""
create table airport (
  id varchar(64) not null,
  type varchar(64) not null,
  name varchar(100) not null,
  country varchar(2) not null check length(country) = 2
)
""".execute.apply()

  sql"""
create table runway (
  id varchar(64)  not null,
  surface varchar(64) not null,
  id_lat varchar(64) not null
)
""".execute.apply()

  // table creation, you can run DDL by using #execute as same as JDBC

  def insertObjectTable[T](ligne : T): Int = ligne match {
    case Country(code,country,continent) => withSQL { insert.into(Country).values(code, country, continent) }.update.apply()
    case Airports(id, typ, name, country) => withSQL {insert.into(Airports).values(id, typ, name, country) }.update.apply()
    case Runway(id, surface, idlat) =>  withSQL {insert.into(Runway).values(id, surface, idlat) }.update.apply()
  }
  // insert initial data
  def insertIntoDB[A](path : String, parseLine : Array[String] => Option[A]) : Unit = {
    CSVParsing.CSV.read (path, parseLine).lignes foreach {rows =>
      insertObjectTable(rows)}
  }


  insertIntoDB("ressources/countries.csv", Country.parseCountry)

  insertIntoDB("ressources/runways.csv", Runway.parseRunway)

  insertIntoDB("ressources/airports.csv", Airports.parseAirports)

 */



  def findAirportAndRunwayByCountry(code: String)(implicit session : DBSession = AutoSession) : List[(Airports,Runway)] =    {
    val (c, a, r) = (Country.syntax("c"), Airports.syntax("a"), Runway.syntax("r"))
    withSQL { select.from(Airports as a).innerJoin(Runway as r).on(r.ID, a.ID).innerJoin(Country as c).on(c.Code, a.Country).where.eq(c.Code, code)
      .union(select.from(Airports as a).innerJoin(Runway as r).on(r.ID, a.ID).innerJoin(Country as c).on(c.Code, a.Country).where.like(c.Name, code.substring(0,1).toUpperCase+code.substring(1,code.length).toLowerCase + "%"))}.map(rs => (Airports(rs),Runway(rs))).list.apply()
  }
  def top10Country(numberFilter : Int)(implicit s: DBSession = AutoSession) : List[(String,Int)] = {
    val (_, a, _) = (Country.syntax("c"), Airports.syntax("a"), Runway.syntax("r"))

    withSQL { select(a.Country, count).from(Airports as a).groupBy(a.Country).orderBy(count).desc.limit(numberFilter)}.map(rs => (rs.string(1), rs.int(2))).list.apply()
  }
  def surfaceByCountry()(implicit s: DBSession = AutoSession): List[(String, String)] = {
    val (c, a, r) = (Country.syntax("c"), Airports.syntax("a"), Runway.syntax("r"))

    withSQL {select(c.Name, sqls"group_concat(distinct ${r.Surface})").from(Country as c).innerJoin(Airports as a).on(c.Code, a.Country).innerJoin(Runway as r).on(r.ID, a.ID).groupBy(c.Name)}.map(rs => (rs.string(1), rs.string(2))).list.apply()
  }

  def top10commonLatID()(implicit s: DBSession = AutoSession) : List[(String,Int)] = {
    val (_, _, r) = (Country.syntax("c"), Airports.syntax("a"), Runway.syntax("r"))

    withSQL { select( r.column("ID_lat"), count).from(Runway as r).groupBy(r.column("ID_lat")).orderBy(count).desc}.map(rs => (rs.string(1), rs.int(2))).list.apply()
  }

}

import CSVParsing.CSV

import scala.util.Try
object Functions {

  val result: Iterable[Country] = CSV.read("ressources/countries.csv", Country.parseCountry).lignes
  val result2: Iterable[Airports] = CSV.read("ressources/airports.csv", Airports.parseAirports).lignes
  val result3: Iterable[Runway] = CSV.read("ressources/runways.csv", Runway.parseRunway).lignes

  def findAirportAndRunwayByCountry(code : String) : List[(Airports, Option[Runway])]  = {
    if (result.exists(x => x.Code.toLowerCase == code.toLowerCase)) {
        result2.filter(x => x.Country == code).map(y => (y, result3.find(z => z.ID == y.ID))).toList
      }

    else {
      result2.filter(x => x.Country == result.find(x => x.Name.toLowerCase.startsWith(code.toLowerCase)).head.Code).map(y => (y, result3.find(z => z.ID == y.ID))).toList
    }
  }

  def topandlast10Country(numberToFilter : Int) : List[(String, Int)] = {
    val onlyCountry = result2.map(x => (x.Country,1))
    val groupByCountryandSum = onlyCountry.groupBy(x => x._1).view.mapValues(x => x.map(x => x._2).reduce(_+_)).toList
    val allCountries = groupByCountryandSum.sortWith(_._2 > _._2).zipWithIndex.filter(x => x._2 < numberToFilter).flatMap(x => Try {
      (result.find(y => y.Code == x._1._1).head.Name, x._1._2)
    }.toOption)
    allCountries
  }


  def surfaceByCountry(): List[(String, String)] = {
    result2.map(x => (x, result3.find(y => y.ID == x.ID))).groupBy(x => x._1.Country).view.mapValues(x => x.flatMap(x => x._2.map(y => y.Surface).filter(z => z.nonEmpty)).toSet.mkString("Array(",",",")")).toList.flatMap(x => Try {(result.find(y => y.Code == x._1).head.Name, x._2)}.toOption)
  }

  def top10commonIdentLat() : List[(String, Int)]= {
    result3.map(x => (x.ID_lat,1)).groupBy(x => x._1).view.mapValues(x => x.map(x => x._2).reduce(_+_)).toArray.sortWith(_._2 > _._2).zipWithIndex.filter(x => x._2 < 10).map(x => (x._1._1, x._1._2)).toList
  }


}

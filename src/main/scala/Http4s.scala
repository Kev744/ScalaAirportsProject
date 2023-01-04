import cats._
import cats.effect._
import cats.implicits._
import org.http4s.circe._
import org.http4s._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.dsl._
import org.http4s.dsl.impl._
import org.http4s.headers._
import org.http4s.implicits._
import org.http4s.server._
import org.http4s.server.blaze.BlazeServerBuilder
import org.typelevel.ci.CIString
import scalikejdbc.config.DBs



object Http4s extends IOApp {

  object CountryQueryParamMatcher extends QueryParamDecoderMatcher[String]("country")

  object AirportIDQueryParamMatcher extends OptionalQueryParamDecoderMatcher[String]("id_airport")

  //val airport: List[(Airports, Runway)] = List((Airports("5", "petit", "CDG", "FR"), Runway("5", "ASP", "10")), (Airports("6", "grand", "Saint-Exupery", "FR"), Runway("6", "ASP", "15")))

  //println(airport.asJson)

  def movieRoutes[F[_] : Monad]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "movies" :? CountryQueryParamMatcher(country) =>
        Ok(Functions.findAirportAndRunwayByCountry(country).asJson)
        }
  }

  import scala.concurrent.ExecutionContext.global

  override def run(args: List[String]): IO[ExitCode] = {
    DBs.setupAll()
    val apis = Router(
      "/api" -> Http4s.movieRoutes[IO],
    ).orNotFound

    BlazeServerBuilder[IO](global)
      .bindHttp(8081, "localhost")
      .withHttpApp(apis)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)

  }
  DBs.closeAll()
}
import Main.ui
import scalikejdbc.config.DBs

import java.awt.{Color, Toolkit}
import scala.swing._
import scala.util.Try


class UI extends MainFrame {
  DBs.setupAll()
  title = "Features world airports"
  preferredSize = new Dimension(Toolkit.getDefaultToolkit.getScreenSize)
  background = Color.blue
  val report = new ReportUI
  val db = new CheckBox("Use database connection instead")
  contents = new BorderPanel { add(new BoxPanel(Orientation.Horizontal) {
    contents += Button("Query") {
      Try{
      add(new ScrollPane(changeCountry(db)), BorderPanel.Position.Center)
        ui.validate()
      }.getOrElse(Dialog.showMessage(null, "Incorrect input ! This seems that input isn't in that list, write a different code or country"))

    }
    contents += Swing.HStrut(30)
    contents += Button("Reports") {
      report.visible = true
      ui.visible = false
    }
    contents += Swing.HStrut(30)
    contents += db
  }, BorderPanel.Position.North)


  }



  def changeCountry(db_checkbox : CheckBox): Table = {
    val text = Dialog.showInput(parent = contents.head, message = "Write a code of a country or a country", initial = "Enter your code or country...")
    if (db_checkbox.selected) {
      text match {
        case Some(s) => airportRunwayByCountryDB(s)
        case None => new Table()
      }
    } else {
      text match {
        case Some(s) => airportRunwayByCountry(s)
        case None => new Table()
      }
    }
  }
  def airportRunwayByCountry(code : String): Table = {
    val headers = Seq("Airport","Runway linked")
    val rowData = Functions.findAirportAndRunwayByCountry(code).map(x => x.productIterator.toArray).toArray
    val tableAirportRunway = new Table(rowData,headers)
    tableAirportRunway}

  def airportRunwayByCountryDB(code : String): Table = {
    val headers = Seq("Airport" , "Runway linked")
    val rowData = DataBase.findAirportAndRunwayByCountry(code).map(x => x.productIterator.toArray).toArray
    val tableAirportRunway = new Table(rowData,headers)
    tableAirportRunway}



}





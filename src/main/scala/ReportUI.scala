import Main.ui
import scalikejdbc.config.DBs

import java.awt.{Color, Cursor, Toolkit}
import scala.swing._
import scala.swing.event.{MouseClicked, SelectionChanged}

class ReportUI extends MainFrame {
  title = "Reports on world airports features"
  preferredSize = new Dimension(Toolkit.getDefaultToolkit.getScreenSize)
  val db : CheckBox = new CheckBox("Use database connection instead")
  val scrolltableLatitude: ScrollPane = new ScrollPane(reportTop10(db))
  val scrollsurface : ScrollPane = new ScrollPane(surfaceCountry(db))
  val comboFilter : ComboBox[Int] = new ComboBox[Int](Seq(10,20,50,100,200,245))
  var test = new ScrollPane(numberAirportsByCountries(comboFilter.selection.item, db))
  val button1  = new Button("Top 10 most commun runways latitude")
  contents = new BorderPanel {
      add(new BoxPanel(Orientation.Horizontal) { contents += Button("Most commun runways latitude in decreasing order") {
        add(Swing.Glue,BorderPanel.Position.South)
        add(scrolltableLatitude, BorderPanel.Position.Center)
        ui.report.validate()
        ui.report.repaint()}
        contents += Swing.HStrut(30)
        contents += Button("10 highest and lowest number of countries") {
          add(comboFilter, BorderPanel.Position.Center)
          ui.report.validate()
          ui.report.repaint()
          listenTo(comboFilter.selection)
          reactions += { case SelectionChanged(`comboFilter`) =>
            test = new ScrollPane(numberAirportsByCountries(comboFilter.selection.item, db))
            add(test,BorderPanel.Position.South)

            ui.report.validate()
            ui.report.repaint()
          }
        }
        contents += Swing.HStrut(30)
        contents += Button("Type of runways per country") {
        add(Swing.Glue,BorderPanel.Position.South)
        add(scrollsurface, BorderPanel.Position.Center)
        ui.report.validate()
        ui.report.repaint()}
        contents += Swing.HStrut(30)
        contents += db}, BorderPanel.Position.North)
        add(new MainQuery() {
          listenTo(mouse.clicks)
          reactions += {
            case MouseClicked(_, _, _, _, _) => ui.visible = true
              val reportUI = ui
              reportUI.report.visible = false
          }}, BorderPanel.Position.West)



  }


  def numberAirportsByCountries(numberToFilter : Int, db_checkbox : CheckBox): Table = {
    val headers = Seq("Country", "Number of airports")
    if(db_checkbox.selected) {
      val rowData = DataBase.top10Country(numberToFilter).map(x => x.productIterator.toArray).toArray
      val tableNumberAirportsCountries = new Table(rowData, headers)
      tableNumberAirportsCountries
    }
    else {
      val rowData = Functions.topandlast10Country(numberToFilter).map(x => x.productIterator.toArray).toArray
      val tableNumberAirportsCountries = new Table(rowData, headers)
      tableNumberAirportsCountries
    }
  }
  def reportTop10(db_checkbox : CheckBox): Table = {
    val headers = Seq("Latitude ID", "Count")
    if(db_checkbox.selected) {
      val rowData = DataBase.top10commonLatID().map(x => x.productIterator.toArray).toArray
      val tableTop10 = new Table(rowData, headers)
      tableTop10
    }
    else {
      val rowData = Functions.top10commonIdentLat().map(x => x.productIterator.toArray).toArray
      val tableTop10 = new Table(rowData, headers)
      tableTop10
    }
  }

  def surfaceCountry(db_checkbox : CheckBox): Table = {
    val headers = Seq("Country", "Surfaces")
    if (db_checkbox.selected) {
      val rowData = DataBase.surfaceByCountry().map(x => x.productIterator.toArray).toArray
      val tableSurface = new Table(rowData, headers)
      tableSurface }
    else {
      val rowData = Functions.surfaceByCountry().map(x => x.productIterator.toArray).toArray
      val tableSurface = new Table(rowData, headers)
      tableSurface
    }
  }


}

class MainQuery extends Label {
  text = "<html><a href=''> Return rto the Query window </a> </html> "
  foreground = Color.BLUE
  cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
  //opaque = true
}
package module.data.mock

import java.util.UUID

import com.google.inject.{Inject, Singleton}
import model.{Currency, Stock}
import module.data.StockDao
import org.joda.time.DateTime
import utility.DateHelper

import scala.io.BufferedSource

@Singleton
class MockStockProvider @Inject() (stockDao: StockDao) {

  def readStocks(): Unit = {
    val source: BufferedSource = scala.io.Source.fromURL(getClass.getResource("stocks.txt"))
    val lines: String = source.mkString

    val perLine: Array[String] = lines.split("\n")

    var startDate: DateTime = DateHelper.formatter.parseDateTime("01/7/2015")
    val newStocks = perLine.foldLeft(List[Stock]()) {(stocks, l) => {
      val fields = l.split("\\t")
      val ABCprice = fields(0).substring(1)
      val DEFprice = fields(1).substring(1)
      val XYZprice = fields(2).substring(1)
      val GBP = fields(3)
      val EUR = fields(4)

      startDate = startDate plusDays (1)
      val addedStocks = stocks ::: List[Stock](
      Stock(UUID.randomUUID().toString, "ABC", startDate, -1, BigDecimal(ABCprice), Currency("USD", 1.0)),
      Stock(UUID.randomUUID().toString, "DEF", startDate, -1, BigDecimal(DEFprice), Currency("GBP", GBP.toDouble)),
      Stock(UUID.randomUUID().toString, "XYZ", startDate, -1, BigDecimal(XYZprice), Currency("EUR", EUR.toDouble)))

      addedStocks
    }}

    stockDao.replaceStocks(newStocks)
  }


}

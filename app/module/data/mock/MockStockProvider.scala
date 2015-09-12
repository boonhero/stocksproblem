package module.data.mock

import java.util.UUID

import com.google.inject.{Inject, Singleton}
import model.{Currency, Stock}
import module.data.StockDao
import org.joda.time.DateTime
import utility.DateHelper

@Singleton
class MockStockProvider @Inject() (stockDao: StockDao) {

  def readStocks(): Unit = {
    val sb = new StringBuilder("")
    sb.append("10.00\t100.00\t50.00\t1.54\t1.12");sb.append("\n")
    sb.append("10.50\t99.00\t52.00\t1.55\t1.13");sb.append("\n")
    sb.append("10.50\t99.00\t52.00\t1.55\t1.13");sb.append("\n")
    sb.append("11.50\t97.00\t56.00\t1.55\t1.14");sb.append("\n")
    sb.append("12.00\t96.00\t58.00\t1.56\t1.14");sb.append("\n")
    sb.append("12.50\t95.00\t60.00\t1.56\t1.15");sb.append("\n")
    sb.append("13.00\t94.00\t62.00\t1.55\t1.14");sb.append("\n")
    sb.append("13.50\t93.00\t64.00\t1.55\t1.15");sb.append("\n")
    sb.append("14.00\t92.00\t66.00\t1.55\t1.16");sb.append("\n")
    sb.append("14.50\t91.00\t68.00\t1.54\t1.15");sb.append("\n")
    sb.append("15.00\t90.00\t70.00\t1.55\t1.15");sb.append("\n")
    sb.append("15.50\t89.00\t72.00\t1.55\t1.14");sb.append("\n")
    sb.append("16.00\t88.00\t74.00\t1.55\t1.15");sb.append("\n")
    sb.append("16.50\t87.00\t76.00\t1.55\t1.16");sb.append("\n")
    sb.append("17.00\t86.00\t78.00\t1.55\t1.15");sb.append("\n")
    sb.append("17.50\t85.00\t80.00\t1.55\t1.14");sb.append("\n")
    sb.append("18.00\t84.00\t82.00\t1.55\t1.14");sb.append("\n")
    sb.append("18.50\t83.00\t84.00\t1.56\t1.14");sb.append("\n")
    sb.append("19.00\t82.00\t86.00\t1.56\t1.15");sb.append("\n")
    sb.append("19.50\t81.00\t88.00\t1.57\t1.16");sb.append("\n")
    sb.append("20.00\t80.00\t90.00\t1.57\t1.17");sb.append("\n")
    sb.append("20.50\t79.00\t92.00\t1.58\t1.17");sb.append("\n")
    sb.append("21.00\t78.00\t94.00\t1.57\t1.16");sb.append("\n")
    sb.append("21.50\t77.00\t96.00\t1.58\t1.17");sb.append("\n")
    sb.append("22.00\t76.00\t98.00\t1.57\t1.17");sb.append("\n")
    sb.append("22.50\t75.00\t100.00\t1.57\t1.16");sb.append("\n")
    sb.append("23.00\t74.00\t102.00\t1.56\t1.15");sb.append("\n")
    sb.append("23.50\t73.00\t104.00\t1.56\t1.16");sb.append("\n")
    sb.append("24.00\t72.00\t106.00\t1.55\t1.15");sb.append("\n")
    sb.append("24.50\t71.00\t108.00\t1.55\t1.14");sb.append("\n")
    sb.append("25.00\t70.00\t110.00\t1.55\t1.15");sb.append("\n")


    val lines: String = sb.toString()

    val perLine: Array[String] = lines.split("\n")
    var startDate: DateTime = DateHelper.formatter.parseDateTime("30/6/2015")
    val newStocks = perLine.foldLeft(List[Stock]()) {(stocks, l) => {

      val fields = l.split("\\t")
      val ABCprice = fields(0)
      val DEFprice = fields(1)
      val XYZprice = fields(2)
      val GBP = fields(3)
      val EUR = fields(4)

      startDate = startDate plusDays (1)
      val addedStocks = stocks ::: List[Stock](
      Stock("", Some(UUID.randomUUID().toString), "ABC", startDate.toDate, -1, BigDecimal(ABCprice), Currency("USD", 1.0), BigDecimal(0)),
      Stock("", Some(UUID.randomUUID().toString), "DEF", startDate.toDate, -1, BigDecimal(DEFprice), Currency("GBP", GBP.toDouble), BigDecimal(0)),
      Stock("", Some(UUID.randomUUID().toString), "XYZ", startDate.toDate, -1, BigDecimal(XYZprice), Currency("EUR", EUR.toDouble), BigDecimal(0)))

      addedStocks
    }}

    stockDao.replaceStocks(newStocks)
  }


}

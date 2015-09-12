package utility

import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}

/**
 * Created by asales on 4/9/2015.
 */
object CurrencyHelper {
   def base(value: BigDecimal, rate: Double) =  value * (rate)
}

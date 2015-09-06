package utility

import org.joda.time.format.{DateTimeFormatter, DateTimeFormat}

/**
 * Created by asales on 4/9/2015.
 */
object DateHelper {
   val formatter: DateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy");
   val dashFormatter: DateTimeFormatter = DateTimeFormat.forPattern("dd-MM-yyyy")
}

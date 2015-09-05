import com.google.inject.{Inject, AbstractModule, Guice}
import play.api.{Application, GlobalSettings}

/**
 * Created by asales on 5/9/2015.
 */
object Global extends GlobalSettings{

  override def onStart(app: Application): Unit = {
    println("APP STARTING")
  }
}

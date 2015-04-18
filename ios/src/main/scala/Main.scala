package srg.scala.paxbritannica.ios

import org.robovm.apple.foundation.NSAutoreleasePool
import org.robovm.apple.uikit.UIApplication
import com.badlogic.gdx.backends.iosrobovm.{IOSApplication, IOSApplicationConfiguration}
import srg.scala.paxbritannica.PaxBritannica

class Main extends IOSApplication.Delegate {
  override protected def createApplication(): IOSApplication = {
    val config = new IOSApplicationConfiguration
    new IOSApplication(new PaxBritannica, config)
  }
}

object Main {
  def main(args: Array[String]) {
      val pool = new NSAutoreleasePool
      UIApplication.main(args, null, classOf[Main])
      pool.drain()
  }
}

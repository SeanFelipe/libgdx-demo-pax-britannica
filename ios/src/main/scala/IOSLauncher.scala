package srg.scala.paxbritannica

import org.robovm.apple.foundation.NSAutoreleasePool
import org.robovm.apple.uikit.UIApplication

import com.badlogic.gdx.backends.iosrobovm.{ IOSApplication, IOSApplicationConfiguration }
import srg.scala.paxbritannica.PaxBritannica

class IOSLauncher extends IOSApplication.Delegate {
    def createApplication() : IOSApplication = {
        val config = new IOSApplicationConfiguration()
        return new IOSApplication(new PaxBritannica(), config)
    }

    def main(argv: Array[String]) {
        val pool = new NSAutoreleasePool()
        UIApplication.main(argv, null, this.getClass)
        pool.close()
    }
}

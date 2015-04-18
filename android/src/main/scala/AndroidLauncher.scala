package srg.scala.paxbritannica.andr
// https://groups.google.com/forum/#!msg/scala-on-android/9Y_7VJonwjs/ypkbO-LheJgJ
// there seems to be a bug in the android plugin which causes TR.scala to not find android 
// depedencies. Needed to change the package name to make it compile.

import android.os.Bundle
import com.badlogic.gdx.backends.android.{AndroidApplication, AndroidApplicationConfiguration}
import srg.scala.paxbritannica.PaxBritannica

class Main extends AndroidApplication {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    val config = new AndroidApplicationConfiguration
    config.useAccelerometer = false
    config.useCompass = false
    config.useWakelock = true
    config.hideStatusBar = true
    initialize(new PaxBritannica, config)
  }
}

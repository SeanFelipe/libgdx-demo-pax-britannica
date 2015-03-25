package srg.scala.paxbritannica.android

import android.os.Bundle
import com.badlogic.gdx.backends.android.{AndroidApplication, AndroidApplicationConfiguration}_
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

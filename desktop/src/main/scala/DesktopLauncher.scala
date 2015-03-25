package srg.scala.paxbritannica.desktop

import com.badlogic.gdx.backends.lwjgl._
import srg.scala.paxbritannica.PaxBritannica

object Main extends App {
    val config = new LwjglApplicationConfiguration
    config.width = 1024
    config.height = 550
    config.title = "PaxBritannicaScala"
    config.forceExit = false
    new LwjglApplication(new PaxBritannica, config)
}

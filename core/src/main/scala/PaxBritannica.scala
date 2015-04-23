package srg.scala.paxbritannica

import com.badlogic.gdx.Game
import srg.scala.paxbritannica.mainmenu.MainMenu

class PaxBritannica extends Game {
	override def create () {
		setScreen(new MainMenu(this))
	}
}

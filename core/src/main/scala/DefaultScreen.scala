package srg.scala.paxbritannica

import com.badlogic.gdx.Game
import com.badlogic.gdx.Screen

abstract class DefaultScreen (ggame: Game ) extends Screen {
	val game = ggame

	override def resize(width: Int , height: Int ) {
	}

	override def pause() {
	}

	override def resume() { 
	}

	override def dispose() {
	}
	
}

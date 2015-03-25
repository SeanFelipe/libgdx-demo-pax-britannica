package srg.scala.paxbritannica;

import com.badlogic.gdx.Game;

import srg.scala.paxbritannica.mainmenu.MainMenu;

public class PaxBritannica extends Game {
	@Override 
	public void create () {
		setScreen(new MainMenu(this));
	}
}

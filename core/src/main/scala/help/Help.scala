package srg.scala.paxbritannica.help

import com.badlogic.gdx.{ Game, Gdx, Input }
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.{ GL20, OrthographicCamera }
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.{ BitmapFont, Sprite, SpriteBatch }
import com.badlogic.gdx.math.{ Intersector, Vector3 }
import com.badlogic.gdx.math.collision.{ BoundingBox, Ray }

import srg.scala.paxbritannica.{ DefaultScreen, GameInstance, Resources }
import srg.scala.paxbritannica.background.BackgroundFXRenderer
import srg.scala.paxbritannica.mainmenu.MainMenu

class Help (game: Game) extends DefaultScreen (game) with InputProcessor {
	
    Gdx.input.setCatchBackKey(true)
    Gdx.input.setInputProcessor(this)
    var width = 800
	var height = 480
	
	val collisionBack, collisionMusic  = new BoundingBox()

	var back : Sprite = _
	var fighter, bomber, frigate, upgrade : Sprite = _
    var blackFade : Sprite = _
	var cam : OrthographicCamera = _
	
	val backgroundFX = new BackgroundFXRenderer()
	
	var font : BitmapFont = _
    var titleBatch, fadeBatch : SpriteBatch = _
	var collisionRay : Ray = _
	var finished = false
	
	var time = 0f
	var fade = 1.0f

	override def show() {		
        // TODO: can these be moved to the constructor ?
		GameInstance.resetGame()
		blackFade = Resources.blackFade

		back = Resources.back
		back.setPosition(20, 10)
		back.setColor(1,1,1,0.5f)

		collisionBack.set(new Vector3(back.getVertices()(0), back.getVertices()(1), -10),
            new Vector3(back.getVertices()(10), back.getVertices()(11), 10));
		
		fighter = Resources.fighterOutline
		fighter.setRotation(0)
		bomber = Resources.bomberOutline
		bomber.setRotation(0)
		frigate = Resources.frigateOutline
		frigate.setRotation(0)
		upgrade = Resources.upgradeOutline
		upgrade.setRotation(0)
		
		titleBatch = new SpriteBatch()
		titleBatch.getProjectionMatrix().setToOrtho2D(0, 0, 800, 480)
		fadeBatch = new SpriteBatch()
		fadeBatch.getProjectionMatrix().setToOrtho2D(0, 0, 2, 2)
		
		font = new BitmapFont()
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear)
	}

	override def render(delta: Float ) {
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

		time = time + delta

		if (time < 1f)
			return

		backgroundFX.render()

		titleBatch.begin()
		
		back.draw(titleBatch)
		
		font.draw(titleBatch, "Pax Britannica is a one-touch real-time strategy game by No Fun Games.", 90, 460)
		font.draw(titleBatch, "Two players battle it out underwater, struggling to be the last one standing!", 90, 440)
		font.draw(titleBatch, "Select your ship to start a battle against the computer. Select two ships to start a local multiplayer", 90, 400)
		font.draw(titleBatch, "battle. Touching your play area spins the needle on the radial menu in the middle of the player's", 90, 380)
		font.draw(titleBatch, "factory ship.The needle will only travel as far as the player's current resources allow. Resources ", 90, 360)
		font.draw(titleBatch, "(gold? seaweed? who knows!) accumulate over time.", 90, 340)
		font.draw(titleBatch, "Releasing creates a ship that corresponds to the quadrant that the needle is pointing at.", 90, 300)
		font.draw(titleBatch, "Fighter: Small, fast and cheap. Great at chasing down bombers.", 130, 260)
		fighter.setPosition(70, 215)
		fighter.draw(titleBatch)
		font.draw(titleBatch, "Bomber: Shoots slow projectiles that do massive damage to frigates or enemy factory ships!", 130, 220)
		bomber.setPosition(70, 175)
		bomber.draw(titleBatch)
		font.draw(titleBatch, "Frigate: A great hulk of a ship that fires volleys of heat-seeking torpedoes. Effective against fighters.", 130, 180)
		frigate.setPosition(70, 135)
		frigate.draw(titleBatch)
		font.draw(titleBatch, "Upgrade: Improve your factory ship to accumulate resources more quickly.", 130, 140)
		upgrade.setPosition(70, 95)
		upgrade.draw(titleBatch)
		font.draw(titleBatch, "Ships you spawn fight automatically using the latest in artificial aquatelligence technology.", 90, 100)
		font.draw(titleBatch, "The player who keeps their factory ship alive wins!", 90, 80)	 
	
		titleBatch.end()

		if ( ! finished && fade > 0) {
			fade = Math.max(fade - Gdx.graphics.getDeltaTime() / 2f, 0)
			fadeBatch.begin()
			blackFade.setColor(blackFade.getColor().r, blackFade.getColor().g, blackFade.getColor().b, fade)
			blackFade.draw(fadeBatch)
			fadeBatch.end()
		}

		if (finished) {
			fade = Math.min(fade + Gdx.graphics.getDeltaTime() / 2f, 1)
			fadeBatch.begin()
			blackFade.setColor(blackFade.getColor().r, blackFade.getColor().g, blackFade.getColor().b, fade)
			blackFade.draw(fadeBatch)
			fadeBatch.end()
			if (fade >= 1) {
				game.setScreen(new MainMenu(game))
			}
		}

	}
	
	override def resize(width: Int, height: Int) {
		this.width = width
		this.height = height
		if (width == 480 && height == 320) {
			cam = new OrthographicCamera(700, 466)
			this.width = 700
			this.height = 466
		} else if (width == 320 && height == 240) {
			cam = new OrthographicCamera(700, 525)
			this.width = 700
			this.height = 525
		} else if (width == 400 && height == 240) {
			cam = new OrthographicCamera(800, 480)
			this.width = 800
			this.height = 480
		} else if (width == 432 && height == 240) {
			cam = new OrthographicCamera(700, 389)
			this.width = 700
			this.height = 389
		} else if (width == 960 && height == 640) {
			cam = new OrthographicCamera(800, 533)
			this.width = 800
			this.height = 533
		}  else if (width == 1366 && height == 768) {
			cam = new OrthographicCamera(1280, 720)
			this.width = 1280
			this.height = 720
		} else if (width == 1366 && height == 720) {
			cam = new OrthographicCamera(1280, 675)
			this.width = 1280
			this.height = 675
		} else if (width == 1536 && height == 1152) {
			cam = new OrthographicCamera(1366, 1024)
			this.width = 1366
			this.height = 1024
		} else if (width == 1920 && height == 1152) {
			cam = new OrthographicCamera(1366, 854)
			this.width = 1366
			this.height = 854
		} else if (width == 1920 && height == 1200) {
			cam = new OrthographicCamera(1366, 800)
			this.width = 1280
			this.height = 800
		} else if (width > 1280) {
			cam = new OrthographicCamera(1280, 768)
			this.width = 1280
			this.height = 768
		} else if (width < 800) {
			cam = new OrthographicCamera(800, 480)
			this.width = 800
			this.height = 480
		} else {
			cam = new OrthographicCamera(width, height)
		}
		cam.position.x = 400
		cam.position.y = 240
		cam.update()	
		backgroundFX.resize(width, height)
		titleBatch.getProjectionMatrix().set(cam.combined)
		
		back.setPosition(20 - ((this.width - 800) / 2), 10 - ((this.height - 480) / 2))
		collisionBack.set(new Vector3(back.getVertices()(0), back.getVertices()(1), -10),new Vector3(back.getVertices()(10), back.getVertices()(11), 10))
	
	}

	override def hide() {
	}

	override def keyDown(keycode: Int) : Boolean = {
		if(keycode == Input.Keys.BACK) {
			finished = true
		}
		
		if(keycode == Input.Keys.ESCAPE) {
			finished = true
		}
		return false
	}

	override def keyUp(keycode: Int) : Boolean = {
		return false
	}

	override def keyTyped(character: Char) : Boolean = {
		return false
	}

	override def touchDown(x: Int, y: Int, pointer: Int, button: Int) : Boolean = {
		collisionRay = cam.getPickRay(x, y)
		
		if (Intersector.intersectRayBoundsFast(collisionRay, collisionBack)) {
			finished = true
		}
		return false
	}

	override def touchUp(x: Int, y: Int, pointer: Int, button: Int) : Boolean = {
		return false
	}

	override def touchDragged(x: Int, y: Int, pointer: Int) : Boolean = {
		return false
	}

	override def scrolled(amount: Int) : Boolean = {
		return false
	}

	override def mouseMoved(screenX: Int, screenY: Int) : Boolean = {
		return false
	}
}

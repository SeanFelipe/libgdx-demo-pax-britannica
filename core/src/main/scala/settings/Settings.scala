package srg.scala.paxbritannica.settings

import com.badlogic.gdx.{ Game, Gdx, Input, InputProcessor, Preferences }
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.{ BitmapFont, Sprite, SpriteBatch }
import com.badlogic.gdx.math.{ Intersector, Vector3 }
import com.badlogic.gdx.math.collision.{ BoundingBox, Ray }

import srg.scala.paxbritannica.{ DefaultScreen, GameInstance, Resources }
import srg.scala.paxbritannica.background.BackgroundFXRenderer
import srg.scala.paxbritannica.mainmenu.MainMenu

class Settings (game: Game) extends DefaultScreen (game) with InputProcessor {
	
    // TODO: a lot of duplicated code here with Help. Can we DRY it up?

    Gdx.input.setCatchBackKey( true )
    Gdx.input.setInputProcessor(this)
	val backgroundFX = new BackgroundFXRenderer()
	
	val collisionBack, collisionDiffEasy, collisionDiffMedium, collisionDiffHard = new BoundingBox()
	val collisionFacHealthLow, collisionFacHealthMedium, collisionFacHealthHigh = new BoundingBox()
	val collisionAntiAliasOff, collisionAntiAliasOn = new BoundingBox()

	var checkboxOn, checkboxOff : Sprite = _
	var blackFade : Sprite = _
	var back: Sprite = _
	var titleBatch, fadeBatch : SpriteBatch = _

	var cam : OrthographicCamera = _ 
	var font : BitmapFont = _
    var collisionRay : Ray = _	

	var finished = false
	var time = 0f
	var fade = 1.0f
	var width = 800
	var height = 480

	override def show() {		
        // TODO can we do this in the constructor? Or are we waiting for the ecosystem
        // to get us resources?

		GameInstance.resetGame()
		
		blackFade = Resources.blackFade
		
		back = Resources.back
		back.setPosition(20, 10)
		back.setColor(1,1,1,0.5f)
		collisionBack.set(new Vector3(back.getVertices()(0), back.getVertices()(1), -10),new Vector3(back.getVertices()(10), back.getVertices()(11), 10))
		
		collisionDiffEasy.set(new Vector3(90, 330,-10),new Vector3(190, 360, 10))
		collisionDiffMedium.set(new Vector3(240, 330,-10),new Vector3(340, 360, 10))
		collisionDiffHard.set(new Vector3(400, 330,-10),new Vector3(500, 360, 10))
		
		collisionFacHealthLow.set(new Vector3(90, 230,-10),new Vector3(190, 260, 10))
		collisionFacHealthMedium.set(new Vector3(240, 230,-10),new Vector3(340, 260, 10))
		collisionFacHealthHigh.set(new Vector3(400, 230,-10),new Vector3(500, 260, 10))
		
		collisionAntiAliasOff.set(new Vector3(90, 130,-10),new Vector3(190, 160, 10))
		collisionAntiAliasOn.set(new Vector3(240, 130,-10),new Vector3(340, 160, 10))
		
		checkboxOn = Resources.checkboxOn
		checkboxOff = Resources.checkboxOff
		
		titleBatch = new SpriteBatch()
		titleBatch.getProjectionMatrix().setToOrtho2D(0, 0, 800, 480)
		fadeBatch = new SpriteBatch()
		fadeBatch.getProjectionMatrix().setToOrtho2D(0, 0, 2, 2)
		
		font = new BitmapFont()
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear)
	}
 
	override def render(delta: Float ) {
		time = time + delta

		if (time < 1f)
			return

		backgroundFX.render()
		
		titleBatch.begin()
		
		back.draw(titleBatch)
		
		font.draw(titleBatch, "Difficulty", 90, 400)
		font.draw(titleBatch, "Easy", 130, 360)
		if(GameInstance.difficultyConfig == 0) {
			checkboxOn.setPosition(90, 330)
			checkboxOn.draw(titleBatch)
		} else {
			checkboxOff.setPosition(90, 330)
			checkboxOff.draw(titleBatch)
		}
		font.draw(titleBatch, "Medium", 280, 360)
		if(GameInstance.difficultyConfig == 1) {
			checkboxOn.setPosition(240, 330)
			checkboxOn.draw(titleBatch)
		} else {
			checkboxOff.setPosition(240, 330)
			checkboxOff.draw(titleBatch)
		}
		font.draw(titleBatch, "Hard", 440, 360)
		if(GameInstance.difficultyConfig == 2) {
			checkboxOn.setPosition(400, 330)
			checkboxOn.draw(titleBatch)
		} else {
			checkboxOff.setPosition(400, 330)
			checkboxOff.draw(titleBatch)
		}
		
		
		font.draw(titleBatch, "Factory Health", 90, 300)
		font.draw(titleBatch, "Low", 130, 260)
		if(GameInstance.factoryHealthConfig == 0) {
			checkboxOn.setPosition(90, 230)
			checkboxOn.draw(titleBatch)
		} else {
			checkboxOff.setPosition(90, 230)
			checkboxOff.draw(titleBatch)
		}
		font.draw(titleBatch, "Medium", 280, 260)
		if(GameInstance.factoryHealthConfig == 1) {
			checkboxOn.setPosition(240, 230)
			checkboxOn.draw(titleBatch)
		} else {
			checkboxOff.setPosition(240, 230)
			checkboxOff.draw(titleBatch)
		}
		font.draw(titleBatch, "High", 440, 260)
		if(GameInstance.factoryHealthConfig == 2) {
			checkboxOn.setPosition(400, 230)
			checkboxOn.draw(titleBatch)
		} else {
			checkboxOff.setPosition(400, 230)
			checkboxOff.draw(titleBatch)
		}
		
		
		font.draw(titleBatch, "AntiAliasing (only for fast devices)", 90, 200)
		font.draw(titleBatch, "Off", 130, 160)
		if(GameInstance.antiAliasConfig == 0) {
			checkboxOn.setPosition(90, 130)
			checkboxOn.draw(titleBatch)
		} else {
			checkboxOff.setPosition(90, 130)
			checkboxOff.draw(titleBatch)
		}
		font.draw(titleBatch, "On", 280, 160)
		if(GameInstance.antiAliasConfig == 1) {
			checkboxOn.setPosition(240, 130)
			checkboxOn.draw(titleBatch)
		} else {
			checkboxOff.setPosition(240, 130)
			checkboxOff.draw(titleBatch)
		}
		
		titleBatch.end()

		if (!finished && fade > 0) {
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
		
		back.setPosition(20 - ((this.width-800)/2), 10- ((this.height-480)/2))
		collisionBack.set(new Vector3(back.getVertices()(0), back.getVertices()(1), -10),new Vector3(back.getVertices()(10), back.getVertices()(11), 10))	
	}

	override def hide() {
	}

	override def keyDown(keycode: Int) : Boolean = {
		if( keycode == Input.Keys.BACK ) {
			finished = true
		}
		
		if( keycode == Input.Keys.ESCAPE ) {
			finished = true
		}
		return false
	}

	override def keyUp(keycode: Int) : Boolean = {
		return false
	}

	override def keyTyped(character: Char) : Boolean = {
		// TODO Auto-generated method stub
		return false
	}

	override def touchDown(x: Int, y: Int, pointer: Int, button: Int) : Boolean = {
		
		collisionRay = cam.getPickRay(x, y)
        val prefs = Gdx.app.getPreferences("paxbritannica")
		
		if (Intersector.intersectRayBoundsFast(collisionRay, collisionBack)) {
			finished = true
		}
		
		if (Intersector.intersectRayBoundsFast(collisionRay, collisionDiffEasy)) {
			prefs.putInteger("difficulty",0)
			GameInstance.difficultyConfig  = prefs.getInteger("difficulty",0)
		}
		if (Intersector.intersectRayBoundsFast(collisionRay, collisionDiffMedium)) {
			prefs.putInteger("difficulty",1)
			GameInstance.difficultyConfig  = prefs.getInteger("difficulty",0)
		}
		if (Intersector.intersectRayBoundsFast(collisionRay, collisionDiffHard)) {
			prefs.putInteger("difficulty",2)
			GameInstance.difficultyConfig  = prefs.getInteger("difficulty",0)
		}
		
		if (Intersector.intersectRayBoundsFast(collisionRay, collisionFacHealthLow)) {
			prefs.putInteger("factoryHealth",0)
			GameInstance.factoryHealthConfig  = prefs.getInteger("factoryHealth",0)
		}
		if (Intersector.intersectRayBoundsFast(collisionRay, collisionFacHealthMedium)) {
			prefs.putInteger("factoryHealth",1)
			GameInstance.factoryHealthConfig  = prefs.getInteger("factoryHealth",0)
		}
		if (Intersector.intersectRayBoundsFast(collisionRay, collisionFacHealthHigh)) {
			prefs.putInteger("factoryHealth",2)
			GameInstance.factoryHealthConfig  = prefs.getInteger("factoryHealth",0)
		}	
		
		if (Intersector.intersectRayBoundsFast(collisionRay, collisionAntiAliasOff)) {
			prefs.putInteger("antiAliasConfig",0)
			GameInstance.antiAliasConfig  = prefs.getInteger("antiAliasConfig",1)
			Resources.reInit()
			show()
			resize(this.width,this.height)
		}
		if (Intersector.intersectRayBoundsFast(collisionRay, collisionAntiAliasOn)) {
			prefs.putInteger("antiAliasConfig",1)
			GameInstance.antiAliasConfig  = prefs.getInteger("antiAliasConfig",1)
			Resources.reInit()
			show()
			resize(this.width,this.height)
		}	
		return false
	}

	override def touchUp(x: Int, y: Int, pointer: Int, button: Int) : Boolean = {
		return false
	}

	override def touchDragged(x: Int, y: Int, pointer: Int ) : Boolean = {
		return false
	}

	override def scrolled(amount: Int ) : Boolean = {
		return false
	}

	override def mouseMoved(screenX: Int, screenY: Int) : Boolean = {
		return false
	}
}

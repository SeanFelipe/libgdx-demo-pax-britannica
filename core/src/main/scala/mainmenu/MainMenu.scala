package srg.scala.paxbritannica.mainmenu

import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.{ Game, Gdx, Input, InputProcessor, Preferences }
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.{ Sprite, SpriteBatch }
import com.badlogic.gdx.math.{ Intersector, Vector2, Vector3 }
import com.badlogic.gdx.math.collision.{ BoundingBox, Ray }
import com.badlogic.gdx.utils.Array

import srg.scala.paxbritannica.{ DefaultScreen, GameInstance, GameScreen, Resources }
import srg.scala.paxbritannica.background.BackgroundFXRenderer
import srg.scala.paxbritannica.help.Help
import srg.scala.paxbritannica.settings.Settings

class MainMenu (game: Game) extends DefaultScreen(game) with InputProcessor {

    println("MainMenu says hello!")

    Resources.reInit()

    val title = Resources.title
    val credits = Resources.credits
    val settings = Resources.settings
    val blackFade = Resources.blackFade
    val musicOnOff = Resources.musicOnOff
    val help = Resources.help

    var	titleBatch = new SpriteBatch()
	var fadeBatch = new SpriteBatch ()

	val	p1 = new FactorySelector(new Vector2(055f, 150f), 1)
	val	p2 = new FactorySelector(new Vector2(180f, 150f), 2)
	val	p3 = new FactorySelector(new Vector2(305f, 150f), 3)
    val	p4 = new FactorySelector(new Vector2(430f, 150f), 4)

    val countdown = new Countdown(new Vector2(380f, 7f))
	var cam: OrthographicCamera = _
	
	var collisionHelp = new BoundingBox()
	var collisionMusic = new BoundingBox()
	var collisionSettings = new BoundingBox()

	val backgroundFX = new BackgroundFXRenderer()

	var fade = 1.0f
	var time = 0f

	var idP1 = -1
	var idP2 = -1
	var cnt = 0
	var oldCnt = 0
	var changeToScreen = -1
	
	var collisionRay: Ray = _
	
	var width = 800
	var height = 480

    Gdx.input.setInputProcessor(this)
	
	override def show() {	

		GameInstance.resetGame

        // music
        musicOnOff.setPosition(20, 10)
		musicOnOff.setColor(1,1,1,0.5f)

		collisionMusic.set(
            new Vector3(musicOnOff.getVertices()(0), musicOnOff.getVertices()(1), -10),
            new Vector3(musicOnOff.getVertices()(10), musicOnOff.getVertices()(11), 10)
        );

        // help
		help.setPosition(75, 10)
		help.setColor(1,1,1,0.5f)
		collisionHelp.set(
            new Vector3(help.getVertices()(0), help.getVertices()(1), -10),
            new Vector3(help.getVertices()(10), help.getVertices()(11), 10)
        );

        // settings
		settings.setPosition(135, 8)
		settings.setColor(1,1,1,0.5f)
		collisionSettings.set(
            new Vector3(settings.getVertices()(0), settings.getVertices()(1), -10),
            new Vector3(settings.getVertices()(10), settings.getVertices()(11), 10)
        );
		
        // spritebatches
		titleBatch.getProjectionMatrix().setToOrtho2D(0, 0, 800, 480)
		fadeBatch.getProjectionMatrix().setToOrtho2D(0, 0, 2, 2)
		
		val prefs = Gdx.app.getPreferences("paxbritannica")
		if(prefs.getBoolean("music") == true) { 
			if(Resources.music == null) Resources.reInit()
			if(!Resources.music.isPlaying()) { 
				Resources.music.play()
				Resources.music.setLooping(true)
			}
			musicOnOff.setColor(1,1,1,0.5f)
		} else {
			Resources.music.stop()			
			musicOnOff.setColor(1,1,1,0.1f)
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
		
		musicOnOff.setPosition(20 - ((this.width-800)/2), 10- ((this.height-480)/2))
		help.setPosition(75- ((this.width-800)/2), 10- ((this.height-480)/2))
		settings.setPosition(135 - ((this.width-800)/2), 8- ((this.height-480)/2))
		
		collisionMusic.set(
            new Vector3(musicOnOff.getVertices()(0), musicOnOff.getVertices()(1), -10),
            new Vector3(musicOnOff.getVertices()(10), musicOnOff.getVertices()(11), 10)
        );

		collisionHelp.set(
            new Vector3(help.getVertices()(0), help.getVertices()(1), -10),
            new Vector3(help.getVertices()(10), help.getVertices()(11), 10)
        );

		collisionSettings.set(
            new Vector3(settings.getVertices()(0), settings.getVertices()(1), -10),
            new Vector3(settings.getVertices()(10), settings.getVertices()(11), 10)
        );
	
	}

	override def render(delta: Float) {
        // why are we passing delta and then defining it ?
		val delta = Math.min(0.06f, Gdx.graphics.getDeltaTime())
		
		time = time + delta

		if (time < 1f)
			return

		backgroundFX.render()

		titleBatch.begin()
		
		musicOnOff.draw(titleBatch)
		help.draw(titleBatch)
		settings.draw(titleBatch)
		
		titleBatch.draw(title, 85f, 320f, 0, 0, 512, 64f, 1.24f, 1.24f, 0)
		titleBatch.draw(credits, 595f, 50f)
		p1.draw(titleBatch)
		p2.draw(titleBatch)
		p3.draw(titleBatch)
		p4.draw(titleBatch)

		if (p1.playerSelect || p1.cpuSelect)
			cnt += 1
		if (p2.playerSelect || p2.cpuSelect)
			cnt += 1
		if (p3.playerSelect || p3.cpuSelect)
			cnt += 1
		if (p4.playerSelect || p4.cpuSelect)
			cnt += 1
		if (cnt > 1) {
			countdown.draw(titleBatch)
		}
		if( cnt != oldCnt) {
			countdown.reset()
			oldCnt = cnt
		}
		if ((p1.picked && !(p1.playerSelect || p1.cpuSelect)) || (p2.picked && !(p2.playerSelect || p2.cpuSelect)) || (p3.picked
				&& !(p3.playerSelect || p3.cpuSelect)) || (p4.picked && !(p4.playerSelect || p4.cpuSelect))) {
			countdown.reset()
		}

		titleBatch.end()

		if (!countdown.finished && fade > 0) {
			fade = Math.max(fade - delta / 2f, 0)
			fadeBatch.begin()
			blackFade.setColor(blackFade.getColor().r, blackFade.getColor().g, blackFade.getColor().b, fade)
			blackFade.draw(fadeBatch)
			fadeBatch.end()
		}

		if (countdown.finished) {
			fade = Math.min(fade + delta / 2f, 1)
			fadeBatch.begin()
			blackFade.setColor(blackFade.getColor().r, blackFade.getColor().g, blackFade.getColor().b, fade)
			blackFade.draw(fadeBatch)
			fadeBatch.end()
			if (fade >= 1 && cnt>=2) {
				val playerList = new Array[Integer]
				if(p1.playerSelect == true) {
					playerList.add(1)
				}
				if(p2.playerSelect == true) {
					playerList.add(2)
				}
				if(p3.playerSelect == true) {
					playerList.add(3)
				}
				if(p4.playerSelect == true) {
					playerList.add(4)
				}
				val cpuList = new Array[Integer]
				if(p1.cpuSelect == true) {
					cpuList.add(1)
				}
				if(p2.cpuSelect == true) {
					cpuList.add(2)
				}
				if(p3.cpuSelect == true) {
					cpuList.add(3)
				}
				if(p4.cpuSelect == true) {
					cpuList.add(4)
				}
				game.setScreen(new GameScreen(game, playerList, cpuList))
			} else if(fade >= 1 && cnt<1)   {
				if(changeToScreen==1) {
					game.setScreen(new Settings(game))
				} else {
					game.setScreen(new Help(game))
				}
			}
		}

	}

	override def hide() {
	}

	override def keyDown(keycode: Int) : Boolean = {
		if(keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
			var exit = true
			if(p1.picked) {
				p1.reset()
				exit = false
			}
			if(p2.picked) {
				p2.reset()
				exit = false
			}			
			if(p3.picked) {
				p3.reset()
				exit = false
			}			
			if(p4.picked) {
				p4.reset()
				exit = false
			}
			
			if(exit) {
				if(!(Gdx.app.getType() == ApplicationType.Applet)) {
					Gdx.app.exit()
				}
			}
		}
		
		if(keycode == Input.Keys.A) {
			if (!p1.picked) {
				p1.picked = true
			} else {
				p1.playerSelect = true
				p1.cpuSelect = false
			}			
		}		
		if(keycode == Input.Keys.F) {
			if (!p2.picked) {
				p2.picked = true
			}  else {
				p2.playerSelect = true
				p2.cpuSelect = false
			}	
		}		
		if(keycode == Input.Keys.H) {
			if (!p3.picked) {
				p3.picked = true
			}  else {
				p3.playerSelect = true
				p3.cpuSelect = false
			}	
		}
		if(keycode == Input.Keys.L) {
			if (!p4.picked) {
				p4.picked = true
			}  else {
				p4.playerSelect = true
				p4.cpuSelect = false
			}	
		}
		
		if(keycode == Input.Keys.M) {
			if (cnt >= 1)
				return false
			val prefs = Gdx.app.getPreferences("paxbritannica")
			prefs.putBoolean("music", !prefs.getBoolean("music"))
			prefs.flush()
			if(prefs.getBoolean("music")) {
				if(Resources.music == null) Resources.reInit()
				if(!Resources.music.isPlaying()) { 
					Resources.music.play()
					Resources.music.setLooping(true)
				}
				musicOnOff.setColor(1,1,1,0.5f)				
			} else {
				Resources.music.stop()
				musicOnOff.setColor(1,1,1,0.1f)
			}			
		}
		
		if(keycode == Input.Keys.F1) {
			if (cnt >= 1)
				return false
			countdown.finished = true
			changeToScreen = 0
		}
		
		if(keycode == Input.Keys.S) {
			if (cnt >= 1)
				return false
			countdown.finished = true
			changeToScreen = 1
		}
				

		return false
	}

	override def keyUp(keycode: Int) : Boolean = {
		// TODO Auto-generated method stub
		return false
	}

	override def keyTyped(character: Char) : Boolean = {
		// TODO Auto-generated method stub
		return false
	}

	override def touchDown(x: Int, y: Int, pointer: Int, button: Int) : Boolean = {
	
        println(s"touchDown at: $x, $y")

        /*
		collisionRay = cam.getPickRay(x, y)
		
		if (cnt > 4 || countdown.finished)
			return false	
		
		// check if ship is activated
		if (Intersector.intersectRayBoundsFast(collisionRay, p1.collision) && !p1.picked) {
			p1.picked = true
		} else if (Intersector.intersectRayBoundsFast(collisionRay, p2.collision) && !p2.picked) {
			p2.picked = true
		} else if (Intersector.intersectRayBoundsFast(collisionRay, p3.collision) && !p3.picked) {
			p3.picked = true
		} else if (Intersector.intersectRayBoundsFast(collisionRay, p4.collision) && !p4.picked) {
			p4.picked = true
		} else if (
            Intersector.intersectRayBoundsFast(collisionRay, p1.collisionPlayerSelect) && p1.picked && !p1.cpuSelect) {
			p1.playerSelect = true
			p1.cpuSelect = false
		} else if (
            Intersector.intersectRayBoundsFast(collisionRay, p2.collisionPlayerSelect) && p2.picked && !p2.cpuSelect) {
			p2.playerSelect = true
			p2.cpuSelect = false
		} else if (
            Intersector.intersectRayBoundsFast(collisionRay, p3.collisionPlayerSelect) && p3.picked && !p3.cpuSelect) {
			p3.playerSelect = true
			p3.cpuSelect = false
		} else if (
            Intersector.intersectRayBoundsFast(collisionRay, p4.collisionPlayerSelect) && p4.picked && !p4.cpuSelect) {
			p4.playerSelect = true
			p4.cpuSelect = false
		} else if (
            Intersector.intersectRayBoundsFast(collisionRay, p1.collisionCPUSelect) && p1.picked && !p1.playerSelect) {
			p1.cpuSelect = true
			p1.playerSelect = false
		} else if (
            Intersector.intersectRayBoundsFast(collisionRay, p2.collisionCPUSelect) && p2.picked && !p2.playerSelect) {
			p2.cpuSelect = true
			p2.playerSelect = false
		} else if (
            Intersector.intersectRayBoundsFast(collisionRay, p3.collisionCPUSelect) && p3.picked && !p3.playerSelect) {
			p3.cpuSelect = true
			p3.playerSelect = false
		} else if (
            Intersector.intersectRayBoundsFast(collisionRay, p4.collisionCPUSelect) && p4.picked && !p4.playerSelect) {
			p4.cpuSelect = true
			p4.playerSelect = false
		}
		
		if (Intersector.intersectRayBoundsFast(collisionRay, collisionMusic)) {
			if (cnt >= 1)
				return false
			val prefs = Gdx.app.getPreferences("paxbritannica")
			prefs.putBoolean("music", !prefs.getBoolean("music"))
			prefs.flush()
			if(prefs.getBoolean("music")) {
				if( Resources.music == null) Resources.reInit()
				if(! Resources.music.isPlaying()) { 
					Resources.music.play()
					Resources.music.setLooping(true)
				}
				musicOnOff.setColor(1,1,1,0.5f)				
			} else {
				Resources.music.stop()
				musicOnOff.setColor(1,1,1,0.1f)
			}			
		}
		
		if (Intersector.intersectRayBoundsFast(collisionRay, collisionHelp)) {
			if (cnt >= 1)
				return false
			countdown.finished = true
			changeToScreen = 0
		}
		
		if (Intersector.intersectRayBoundsFast(collisionRay, collisionSettings)) {
			if (cnt >= 1)
				return false
			countdown.finished = true
			changeToScreen = 1
		}
		
        */
		return false

	}

	override def touchUp(x: Int, y: Int, pointer: Int, button: Int) : Boolean = {
		return false
	}

	override def touchDragged(x: Int, y: Int, pointer: Int) : Boolean =  {
		return false
	}

	override def scrolled(amount: Int) : Boolean = {
		return false
	}

	override def mouseMoved(screenX: Int, screenY: Int) : Boolean = {
		// TODO Auto-generated method stub
		return false
	}
}

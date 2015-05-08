package srg.scala.paxbritannica

import com.badlogic.gdx.{ Game, Gdx, Input, InputProcessor }
import com.badlogic.gdx.graphics.{ GL20, OrthographicCamera }
import com.badlogic.gdx.graphics.g2d.{ BitmapFont, Sprite, SpriteBatch }
import com.badlogic.gdx.math.{ Intersector, MathUtils, Vector2, Vector3 }
import com.badlogic.gdx.math.collision.{ BoundingBox, Ray }
import com.badlogic.gdx.utils.Array

import srg.scala.paxbritannica.background.BackgroundFXRenderer
import srg.scala.paxbritannica.factory.{
    EasyEnemyProduction, FactoryProduction, HardEnemyProduction,
MediumEnemyProduction, PlayerProduction }
import srg.scala.paxbritannica.mainmenu.MainMenu

class GameScreen ( game: Game, playerList: Array[Integer], cpuList: Array[Integer] ) 
    extends DefaultScreen ( game ) with InputProcessor {


///////////////////
// begin constructor
//
// need to set player pointers. 

    // input
    Gdx.input.setCatchBackKey(true)
    Gdx.input.setInputProcessor(this)

    // startup stuffs
    var startTime : Double = 0
    val numPlayers = playerList.size
    val numCpus = cpuList.size
    var gameOver = false
    var gameOverTimer = 5f
    val CENTER = new Vector2(300, 180)

    // camera
    val width = 800
    val height = 480
    val cam = new OrthographicCamera(width, height);
    cam.position.x = 400
    cam.position.y = 240
    cam.update()

    // game batch
    val gameBatch = new SpriteBatch();
    gameBatch.getProjectionMatrix().set(cam.combined);

    // positions (for what ??)
    val POSITIONS = getPositions // Array[Vector2]

    // fade
    var fade = 1.0f
    var touchFadeP1 = 1.0f
    var touchFadeP2 = 1.0f
    var touchFadeP3 = 1.0f
    var touchFadeP4 = 1.0f
    val blackFade = Resources.getInstance().blackFade
    val fadeBatch = new SpriteBatch()
    fadeBatch.getProjectionMatrix().setToOrtho2D(0, 0, 2, 2)

    // touch areas
    val touchAreaArray = getBoundingBoxes
    val touchAreaP1 = touchAreaArray.get(0)
    val touchAreaP2 = touchAreaArray.get(1)
    val touchAreaP3 = touchAreaArray.get(2)
    val touchAreaP4 = touchAreaArray.get(3)
    val stouchAreaP1 = Resources.getInstance().touchArea1
    val stouchAreaP2 = Resources.getInstance().touchArea2
    val stouchAreaP3 = Resources.getInstance().touchArea3
    val stouchAreaP4 = Resources.getInstance().touchArea4
    stouchAreaP1.setRotation(-90)
    stouchAreaP2.setRotation(90)
    stouchAreaP1.setRotation(-90)
    stouchAreaP2.setRotation(90)
    var touchedP1 = false
    var touchedP2 = false
    var touchedP3 = false
    var touchedP4 = false

    // background
    val backgroundFX = new BackgroundFXRenderer()

    // sprites
    var p1, p2, p3, p4 : Sprite = _
    setPlayerSprites

    var pointerP1, pointerP2, pointerP3, pointerP4 : Int = _
    //setPlayerPointers

    var collisionRay : Ray = _

    var currentPos = 0

    setPlayerProduction
    setEnemyProduction
       
    Gdx.gl.glDisable(GL20.GL_CULL_FACE)
    Gdx.gl.glDisable(GL20.GL_DEPTH_TEST)
           

///////////////////
// end constructor



///////////////////
// custom methods

    def getBoundingBoxes : Array[BoundingBox] = {

        // bounding box dimensions
        val playerBoundingBoxVectors = new Array[Vector3](numPlayers * 2)
        val cw = (this.width - 800) / 2
        val ch = (this.height - 480) / 2
        val w12 = this.width / 2
        val h12 = this.height / 2

        numPlayers match {
            case 1 => 
                playerBoundingBoxVectors.set(0, new Vector3(cw, -ch, 0))
                playerBoundingBoxVectors.set(1, new Vector3(cw + this.width, -ch + this.height, 0));
            case 2 =>
                playerBoundingBoxVectors.set(0, new Vector3(-cw, -ch, 0))
                playerBoundingBoxVectors.set(1, new Vector3(-cw + w12, -ch + this.height, 0))
                playerBoundingBoxVectors.set(2, new Vector3(-cw + w12, -ch, 0))
                playerBoundingBoxVectors.set(3, new Vector3(-cw + this.width, -ch + this.height, 0));
            case 3 =>
                playerBoundingBoxVectors.set(0, new Vector3(-cw, -ch, 0))
                playerBoundingBoxVectors.set(1, new Vector3(-cw + w12, -ch + h12, 0))
                playerBoundingBoxVectors.set(2, new Vector3(-cw, -ch + h12, 0))
                playerBoundingBoxVectors.set(3, new Vector3(-cw + w12 , -ch + this.height, 0))
                playerBoundingBoxVectors.set(4, new Vector3(-cw + w12, -ch, 0))
                playerBoundingBoxVectors.set(5, new Vector3(-cw + this.width, -ch + this.height, 0));
            case 4 =>
                playerBoundingBoxVectors.set(0, new Vector3(-cw, -ch, 0))
                playerBoundingBoxVectors.set(1, new Vector3(-cw + w12, -ch + h12, 0))
                playerBoundingBoxVectors.set(2, new Vector3(-cw, -ch + h12, 0))
                playerBoundingBoxVectors.set(3, new Vector3(-cw + w12, -ch + this.height, 0))
                playerBoundingBoxVectors.set(4, new Vector3(-cw + w12, -ch, 0))
                playerBoundingBoxVectors.set(5, new Vector3(-cw + this.width, -ch + h12, 0))
                playerBoundingBoxVectors.set(6, new Vector3(-cw + w12, -ch + h12, 0))
                playerBoundingBoxVectors.set(7, new Vector3(-cw + this.width, -ch + this.height, 0));
        }

        // instantiate boxes
        var playerBoundingBoxes = new Array[BoundingBox]

        for ( i <- 0 until playerBoundingBoxVectors.size by 2 ) {
            val v1 = playerBoundingBoxVectors.get(i)
            val v2 = playerBoundingBoxVectors.get(i + 1)
            playerBoundingBoxes.add(new BoundingBox(v1, v2))
        }

        return playerBoundingBoxes
    }

    def getPositions : Array[Vector2] = {
        val parr = new Array[Vector2](
            if (numPlayers + numCpus != 3) 4;
            else 3
        );
        if (numPlayers + numCpus != 3) {
        // there's got to be a more elegant way to do this without specifying the condition twice
            parr.set(0, new Vector2(150, 180))
            parr.set(1, new Vector2(450, 180))
            parr.set(2, new Vector2(300, 335))
            parr.set(3, new Vector2(300, 25))
        } else {
            parr.set(0, new Vector2(170, 92))
            parr.set(1, new Vector2(432, 100))
            parr.set(2, new Vector2(300, 335))
        }
        return parr
    }

    def setPlayerSprites {
        val pl0 = playerList.get(0)
        if(numPlayers > 0 && pl0 == 1) {
            p1 = Resources.getInstance().factoryP1Small
        } else if(numPlayers > 0 && pl0 == 2) {
            p1 = Resources.getInstance().factoryP2Small
        } else if(numPlayers > 0 && pl0 == 3) {
            p1 = Resources.getInstance().factoryP3Small
        } else if(numPlayers>0 && pl0 == 4) {
            p1 = Resources.getInstance().factoryP4Small		
        }

        val pl1 = playerList.get(1)
        if(numPlayers > 1 && pl1 == 1) {
            p2 = Resources.getInstance().factoryP1Small
        } else if(numPlayers > 1 && pl1 == 2) {
            p2 = Resources.getInstance().factoryP2Small
        } else if(numPlayers > 1 && pl1 == 3) {
            p2 = Resources.getInstance().factoryP3Small
        } else if(numPlayers > 1 && pl1 == 4) {
            p2 = Resources.getInstance().factoryP4Small		
        }

        val pl2 = playerList.get(2)
        if(numPlayers > 2 && pl2 == 1) {
            p3 = Resources.getInstance().factoryP1Small
        } else if(numPlayers > 2 && pl2 == 2) {
            p3 = Resources.getInstance().factoryP2Small
        } else if(numPlayers > 2 && pl2 == 3) {
            p3 = Resources.getInstance().factoryP3Small
        } else if(numPlayers > 2 && pl2 == 4) {
            p3 = Resources.getInstance().factoryP4Small		
        }

        val pl3 = playerList.get(3)
        if(numPlayers > 3 && pl3 == 1) {
            p4 = Resources.getInstance().factoryP1Small
        } else if(numPlayers > 3 && pl3 == 2) {
            p4 = Resources.getInstance().factoryP2Small
        } else if(numPlayers > 3 && pl3 == 3) {
            p4 = Resources.getInstance().factoryP3Small
        } else if(numPlayers > 3 && pl3 == 4) {
            p4 = Resources.getInstance().factoryP4Small		
        }

        if(numPlayers > 0) p1.setScale(.2f)
        if(numPlayers > 1) p2.setScale(.2f)
        if(numPlayers > 2) p3.setScale(.2f)
        if(numPlayers > 3) p4.setScale(.2f)

        if(numPlayers > 0) p1.rotate(-90)
        if(numPlayers > 1) p2.rotate(90)
        if(numPlayers > 2) p3.rotate(-90)
        if(numPlayers > 3) p4.rotate(90)

    }

    def setPlayerProduction {
        for (i <- 0 until numPlayers)  {
            val v1 = new Vector2(POSITIONS.get(currentPos).x, POSITIONS.get(currentPos).y)
            val v2 = new Vector2(POSITIONS.get(currentPos).x, POSITIONS.get(currentPos).y)
            val facing = new Vector2(-v1.sub(CENTER).y, v2.sub(CENTER).x).nor()
            val playerProduction = new PlayerProduction(playerList.get(i), POSITIONS.get(currentPos), facing)
            GameInstance.getInstance().factorys.add(playerProduction)
            currentPos = currentPos + 1
        }
    }

    def setEnemyProduction {
        val pp = POSITIONS.get(currentPos)
        val px = pp.x
        val py = pp.y
        for(i <- 0 until numCpus ) {
            val v1, v2 = new Vector2(px, py)
            val facing = new Vector2(-v1.sub(CENTER).y, v2.sub(CENTER).x).nor()
            if(GameInstance.getInstance().difficultyConfig == 0) {
                val enemyProduction = new EasyEnemyProduction(cpuList.get(i), pp, facing)
                GameInstance.getInstance().factorys.add(enemyProduction)
            } else if(GameInstance.getInstance().difficultyConfig == 1) {
               val enemyProduction = new MediumEnemyProduction(cpuList.get(i), pp, facing)
                GameInstance.getInstance().factorys.add(enemyProduction)
            } else {
                val enemyProduction = new HardEnemyProduction(cpuList.get(i), pp, facing)
                GameInstance.getInstance().factorys.add(enemyProduction)
            }
            currentPos = currentPos + 1
        }
    }

    def generatePositions (n: Int) : Array[Vector2] = {
        val positions = new Array[Vector2](n)
        for (i <- 0 until n) {
            positions.add(new Vector2(
                MathUtils.cos(i / n), MathUtils.sin(i / n)).scl(200)
            );
        }
        return positions
    }

////////////////////////////
// Android methods

    override def resize(width: Int, height: Int) {
        // implement this later. The bounding box logic is pretty messy
        // the java implementation seems to just repeat a bunch of math.
    }

    override def show() {
    }

    override def render(a_delta: Float) {
        val delta = Math.min(0.06f, a_delta)

        backgroundFX.render()		

        Collision.collisionCheck()

        gameBatch.begin()
        // Bubbles
        GameInstance.getInstance().bubbleParticles.draw(gameBatch)
        GameInstance.getInstance().bigBubbleParticles.draw(gameBatch)

        // Factorys
        val facs = GameInstance.getInstance().factorys
        for (i <- 0 until facs.size) {
            val ship = facs.get(i)
            if (ship.alive) {
                ship.draw(gameBatch)
            } else {
                GameInstance.getInstance().factorys.removeValue(ship, true)
                if(GameInstance.getInstance().factorys.size < 2) gameOver = true
            }
        }

        // Frigate
        val frigs = GameInstance.getInstance().frigates
        for (i <- 0 until frigs.size ) {
            val ship = frigs.get(i)
            if (ship.alive) {
                ship.draw(gameBatch)
            } else {
                GameInstance.getInstance().frigates.removeValue(ship, true)
            }
        }

        // Bomber
        val bombers = GameInstance.getInstance().bombers
        for (i <- 0 until bombers.size ) {
            val ship = bombers.get(i)
            if (ship.alive) {
                ship.draw(gameBatch)
            } else {
                GameInstance.getInstance().bombers.removeValue(ship, true)
            }
        }
        // Fighter
        val fighters = GameInstance.getInstance().fighters  
        for (i <- 0 until fighters.size) {
            val ship = fighters.get(i)
            if (ship.alive) {
                ship.draw(gameBatch)
            } else {
                GameInstance.getInstance().fighters.removeValue(ship, true)
            }
        }

        // Laser
        val bullets = GameInstance.getInstance().bullets
        for (i <- 0 until bullets.size) {
            val ship = bullets.get(i)
            if (ship.alive) {
                ship.draw(gameBatch)
            } else {
                GameInstance.getInstance().bullets.removeValue(ship, true)
            }
        }

        // Explosions
        GameInstance.getInstance().sparkParticles.draw(gameBatch)
        GameInstance.getInstance().explosionParticles.draw(gameBatch)

        //		font.draw(gameBatch, "fps: " + Gdx.graphics.getFramesPerSecond(), 10, 30)
        gameBatch.end()

        //show touch area notification
        if(numPlayers > 0 && touchedP1) {
            touchFadeP1 = Math.max(touchFadeP1 - delta / 2f, 0)
        }

        if(numPlayers > 0 && (!touchedP1 || touchFadeP1 > 0)) {
            gameBatch.begin()
            stouchAreaP1.setColor(stouchAreaP1.getColor().r, stouchAreaP1.getColor().g, stouchAreaP1.getColor().b, touchFadeP1)
            stouchAreaP1.draw(gameBatch)
            p1.setColor(p1.getColor().r, p1.getColor().g, p1.getColor().b, touchFadeP1)
            p1.draw(gameBatch)
            gameBatch.end()
        }

        if(numPlayers > 1 && touchedP2) {
            touchFadeP2 = Math.max(touchFadeP2 - delta / 2f, 0)
        }
        if(numPlayers > 1 && (!touchedP2 || touchFadeP2 > 0)) {
            gameBatch.begin()
            stouchAreaP2.setColor(stouchAreaP2.getColor().r, stouchAreaP2.getColor().g, stouchAreaP2.getColor().b, touchFadeP2)
            stouchAreaP2.draw(gameBatch)
            p2.setColor(p2.getColor().r, p2.getColor().g, p2.getColor().b, touchFadeP2)
            p2.draw(gameBatch)
            gameBatch.end()
            }
        if(numPlayers > 2 && touchedP3) {
                touchFadeP3 = Math.max(touchFadeP3 - delta / 2f, 0)
        }
        if(numPlayers > 2 && (!touchedP3 || touchFadeP3 > 0)) {
            gameBatch.begin()
            stouchAreaP3.setColor(stouchAreaP3.getColor().r, stouchAreaP3.getColor().g, stouchAreaP3.getColor().b, touchFadeP3)
            stouchAreaP3.draw(gameBatch)
            p3.setColor(p3.getColor().r, p3.getColor().g, p3.getColor().b, touchFadeP3)
            p3.draw(gameBatch)
            gameBatch.end()
        }
        if(numPlayers > 3 && touchedP4) {
            touchFadeP4 = Math.max(touchFadeP4 - delta / 2f, 0)
        }
        if(numPlayers > 3 && (!touchedP4 || touchFadeP4 > 0)) {
            gameBatch.begin()
            stouchAreaP4.setColor(stouchAreaP4.getColor().r, stouchAreaP4.getColor().g, stouchAreaP4.getColor().b, touchFadeP4)
            stouchAreaP4.draw(gameBatch)
            p4.setColor(p4.getColor().r, p4.getColor().g, p4.getColor().b, touchFadeP4)
            p4.draw(gameBatch)
            gameBatch.end()
        }

        if (!gameOver && fade > 0 && fade < 100) {
            fade = Math.max(fade - delta / 2f, 0)
            fadeBatch.begin()
            blackFade.setColor(blackFade.getColor().r, blackFade.getColor().g, blackFade.getColor().b, fade)
            blackFade.draw(fadeBatch)
            fadeBatch.end()
        }

        if(gameOver) {
            gameOverTimer = gameOverTimer - delta
        }

        if (gameOver && gameOverTimer <= 0) {
            fade = Math.min(fade + delta / 2f, 1)
            fadeBatch.begin()
            blackFade.setColor(blackFade.getColor().r, blackFade.getColor().g, blackFade.getColor().b, fade)
            blackFade.draw(fadeBatch)
            fadeBatch.end()
            if(fade >= 1) game.setScreen(new MainMenu(game))
        }
    }

    override def hide() {
    }

    override def keyDown(keycode: Int) : Boolean = {
        val fsize = GameInstance.getInstance().factorys.size

        keycode match {
            case Input.Keys.BACK =>
                gameOver = true
                gameOverTimer = 0;

            case Input.Keys.ESCAPE =>
                gameOver = true
                gameOverTimer = 0;

        // I took out the casting commands here. Might need to find a Scala casting impl
        // https://github.com/libgdx/libgdx-demo-pax-britannica/blob/master/core/src/de/swagner/paxbritannica/GameScreen.java#L658
            case Input.Keys.A =>
                if(numPlayers > 0 && fsize > 0) {
                    (GameInstance.getInstance().factorys.get(0)).asInstanceOf[FactoryProduction].button_held = true
                    touchedP1 = true
                };
            case Input.Keys.F =>
                if(numPlayers > 1 && fsize > 1) {
                    (GameInstance.getInstance().factorys.get(1)).asInstanceOf[FactoryProduction].button_held = true
                    touchedP2 = true
                }; 
            case Input.Keys.H =>
                if(numPlayers > 2 && fsize > 2) {
                    (GameInstance.getInstance().factorys.get(2)).asInstanceOf[FactoryProduction].button_held = true
                    touchedP3 = true
                }; 
            case Input.Keys.L =>
                if(numPlayers > 3 && fsize > 3) {
                    (GameInstance.getInstance().factorys.get(3)).asInstanceOf[FactoryProduction].button_held = true
                    touchedP4 = true
                }
        }
        return false
    }

    // see above comment re: class casting for factorys
    def keyUp(keycode: Int) : Boolean = {
        val fsize = GameInstance.getInstance().factorys.size

        keycode match {
            case Input.Keys.A =>
                if(numPlayers > 0 && fsize > 0) {
                    (GameInstance.getInstance().factorys.get(0)).asInstanceOf[FactoryProduction].button_held = false
                };
            case Input.Keys.F =>
                if(numPlayers > 1 && fsize > 1) {
                    (GameInstance.getInstance().factorys.get(1)).asInstanceOf[FactoryProduction].button_held = false
                }; 
            case Input.Keys.H =>
                if(numPlayers > 2 && fsize > 1) {
                    (GameInstance.getInstance().factorys.get(2)).asInstanceOf[FactoryProduction].button_held = false
                };
            case Input.Keys.L =>
                if(numPlayers >3 && fsize > 1) {
                    (GameInstance.getInstance().factorys.get(3)).asInstanceOf[FactoryProduction].button_held = false
                } 
        }
        return false
    }

    def keyTyped(character: Char) : Boolean = {
        return false
    }

    // more casting stuffs were removed. see comment above.
    def touchDown(x: Int, y: Int, pointer: Int, button: Int) : Boolean = {
        // can we DRY this up a bit?
        collisionRay = cam.getPickRay(x, y)

        val fsize = GameInstance.getInstance().factorys.size

        if(numPlayers > 0 && Intersector.intersectRayBoundsFast(collisionRay, touchAreaP1) && fsize > 0) {
            (GameInstance.getInstance().factorys.get(0)).asInstanceOf[FactoryProduction].button_held = true
            pointerP1 = pointer
            touchedP1 = true
        } 
        if(numPlayers >1 && Intersector.intersectRayBoundsFast(collisionRay, touchAreaP2) && fsize > 1) {
            (GameInstance.getInstance().factorys.get(1)).asInstanceOf[FactoryProduction].button_held = true
            pointerP2 = pointer
            touchedP2 = true
        } 
        if(numPlayers >2 && Intersector.intersectRayBoundsFast(collisionRay, touchAreaP3) && fsize > 2) {
            (GameInstance.getInstance().factorys.get(2)).asInstanceOf[FactoryProduction].button_held = true
            pointerP3 = pointer
            touchedP3 = true
        } 
        if(numPlayers >3 && Intersector.intersectRayBoundsFast(collisionRay, touchAreaP4) && fsize > 3) {
            (GameInstance.getInstance().factorys.get(3)).asInstanceOf[FactoryProduction].button_held = true
            pointerP4 = pointer
            touchedP4 = true
        } 
        return false
    }

    override def touchUp(x: Int, y: Int, pointer: Int, button: Int) : Boolean = {
        collisionRay = cam.getPickRay(x, y)

        val fsize = GameInstance.getInstance().factorys.size

        if(numPlayers > 0 && pointer == pointerP1 && fsize > 0) {
            (GameInstance.getInstance().factorys.get(0)).asInstanceOf[FactoryProduction].button_held = false
            pointerP1 = -1
        } 
        if(numPlayers >1 && pointer == pointerP2 &&  fsize > 1) {
            (GameInstance.getInstance().factorys.get(1)).asInstanceOf[FactoryProduction].button_held = false
            pointerP2 = -1
        } 
        if(numPlayers >2 && pointer == pointerP3 &&  fsize > 1) {
            (GameInstance.getInstance().factorys.get(2)).asInstanceOf[FactoryProduction].button_held = false
            pointerP3 = -1
        } 
        if(numPlayers >3 && pointer == pointerP4 &&  fsize > 1) {
            (GameInstance.getInstance().factorys.get(3)).asInstanceOf[FactoryProduction].button_held = false
            pointerP4 = -1
        } 
        return false
    }

    override def touchDragged(x: Int, y: Int, pointer: Int) : Boolean = {
        return false
    }

    override def scrolled(amount: Int) : Boolean =  {
        return false
    }

    override def mouseMoved(screenX: Int, screenY: Int) : Boolean = {
            return false
    }

}

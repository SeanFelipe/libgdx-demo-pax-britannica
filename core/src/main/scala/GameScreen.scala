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

    // input
    Gdx.input.setCatchBackKey(true)
    Gdx.input.setInputProcessor(this)

    // startup stuffs
    var startTime : Double = 0
    val numPlayers = playerList.size
    var gameOver = false
    var gameOverTimer = 5
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
    val POSITIONS = getPositions(numPlayers)

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
    val touchAreaArray = playerBoundingBoxes(numPlayers)
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
    setPlayerSprites()

    var pointerP1, pointerP2, pointerP3, pointerP4 : Int = _
    setPlayerPointers()

    val collisionRay : Ray = _

    var currentPos = 0

    setPlayerProduction()
    setEnemyProduction()
       
    Gdx.gl.glDisable(GL20.GL_CULL_FACE)
    Gdx.gl.glDisable(GL20.GL_DEPTH_TEST)
           

////////////////////////////////////////////////////////////////////////////////
// end constructor

    def getBoundingBoxes (numPlayers: Int) : Array[BoundingBox] = {

        // bounding box dimensions
        val playerBoundingBoxVectors = new Array[Vector3](numPlayers * 2)
        val cw = (this.width - 800) / 2
        val ch = (this.height - 480) / 2
        val w12 = this.width / 2
        val h12 = this.height / 2

        match (numPlayers) {
            case 1 => 
                playerBoundingBoxVectors(0).set( cw, -ch, 0)
                playerBoundingBoxVectors(1).set( cw + this.width, -ch + this.height, 0));
            case 2 =>
                playerBoundingBoxVectors(0).set(-cw, -ch, 0)
                playerBoundingBoxVectors(1).set(-cw + w12, -ch + this.height, 0)
                playerBoundingBoxVectors(2).set(-cw + w12, -ch, 0)
                playerBoundingBoxVectors(3).set(-cw + this.width, -ch + this.height, 0)
            case 3 =>
                playerBoundingBoxVectors(0).set(-cw, -ch, 0)
                playerBoundingBoxVectors(1).set(-cw + w12, -ch + h12, 0));
                playerBoundingBoxVectors(2).set(-cw, -ch + h12, 0),
                playerBoundingBoxVectors(3).set(-cw + w12 , -ch + this.height, 0))
                playerBoundingBoxVectors(4).set(-cw + w12, -ch, 0),
                playerBoundingBoxVectors(5).set(-cw + this.width, -ch + this.height, 0))
            case 4 =>
                playerBoundingBoxVectors(0).set(-cw, -ch, 0),
                playerBoundingBoxVectors(1).set(-cw + w12, -ch + h12, 0))
                playerBoundingBoxVectors(2).set(-cw, -ch + h12), 0),
                playerBoundingBoxVectors(3).set(-cw + w12, -ch + this.height, 0))
                playerBoundingBoxVectors(4).set(-cw + w12, -ch, 0),
                playerBoundingBoxVectors(5).set(-cw + this.width, -ch + h12, 0))
                playerBoundingBoxVectors(6).set(-cw + w12, -ch + h12, 0),
                playerBoundingBoxVectors(7).set(-cw + this.width, -ch + this.height, 0))
        }

        // instantiate boxes
        var playerBoundingBoxes = new Array[BoundingBox]

        for ( i <- 0 until playerBoundingBoxVectors by 2 ) {
            val v1 = playerBoundingBoxVectors(i)
            val v2 = playerBoundingBoxVectors(i + 1)
            playerBoundingBoxes.add(new BoundingBox(v1, v2))
        }

        return playerBoundingBoxes
    }

    def getPositions(numPlayers, numCpuPlayers) : Array[Vector2] = {
        val parr = new Array[Vector2](
            if (playerList.size + cpuList.size != 3) 4;
            else 3
        );
        if (playerList.size + cpuList.size != 3) {
        // there's got to be a more elegant way to do this without specifying the condition twice
            parr(0) = (new Vector2(150, 180))
            parr(1) = (new Vector2(450, 180))
            parr(2) = (new Vector2(300, 335))
            parr(3) = (new Vector2(300, 25))
        } else {
            parr(0)(new Vector2(170, 92))
            parr(1)(new Vector2(432, 100))
            parr(2)(new Vector2(300, 335))
        }
        return parr
    }

    def setPlayerSprites {
        if(playerList.size > 0 && playerList(0) == 1) {
            p1 = Resources.getInstance().factoryP1Small
        } else if(playerList.size > 0 && playerList(0) == 2) {
            p1 = Resources.getInstance().factoryP2Small
        } else if(playerList.size > 0 && playerList(0)==3) {
            p1 = Resources.getInstance().factoryP3Small
        } else if(playerList.size>0 && playerList(0)==4) {
            p1 = Resources.getInstance().factoryP4Small		
        }

        if(playerList.size > 1 && playerList.get(1)==1) {
            p2 = Resources.getInstance().factoryP1Small
        } else if(playerList.size > 1 && playerList(1)==2) {
            p2 = Resources.getInstance().factoryP2Small
        } else if(playerList.size > 1 && playerList(1)==3) {
            p2 = Resources.getInstance().factoryP3Small
        } else if(playerList.size > 1 && playerList(1)==4) {
            p2 = Resources.getInstance().factoryP4Small		
        }

        if(playerList.size > 2 && playerList.get(2)==1) {
            p3 = Resources.getInstance().factoryP1Small
        } else if(playerList.size > 2 && playerList(2)==2) {
            p3 = Resources.getInstance().factoryP2Small
        } else if(playerList.size > 2 && playerList(2)==3) {
            p3 = Resources.getInstance().factoryP3Small
        } else if(playerList.size > 2 && playerList(2)==4) {
            p3 = Resources.getInstance().factoryP4Small		
        }

        if(playerList.size > 3 && playerList(3) == 1) {
            p4 = Resources.getInstance().factoryP1Small
        } else if(playerList.size > 3 && playerList(3) == 2) {
            p4 = Resources.getInstance().factoryP2Small
        } else if(playerList.size > 3 && playerList(3) == 3) {
            p4 = Resources.getInstance().factoryP3Small
        } else if(playerList.size > 3 && playerList(3) == 4) {
            p4 = Resources.getInstance().factoryP4Small		
        }

        if(playerList.size > 0) p1.setScale(.2f)
        if(playerList.size > 1) p2.setScale(.2f)
        if(playerList.size > 2) p3.setScale(.2f)
        if(playerList.size > 3) p4.setScale(.2f)

        if(playerList.size > 0) p1.rotate(-90)
        if(playerList.size > 1) p2.rotate(90)
        if(playerList.size > 2) p3.rotate(-90)
        if(playerList.size > 3) p4.rotate(90)

    }

    def setPlayerProduction {
        for (i <- 0 until numPlayers)  {
            val v1 = new Vector2(POSITIONS(currentPos).x, POSITIONS(currentPos).y)
            val v2 = new Vector2(POSITIONS(currentPos).x, POSITIONS(currentPos).y)
            val facing = new Vector2(-v1.sub(CENTER).y, v2.sub(CENTER).x).nor()
            val playerProduction = new PlayerProduction(playerList.get(i), POSITIONS.get(currentPos), facing)
            GameInstance.getInstance().factorys.add(playerProduction)
            ++currentPos
        }
    }

    def setEnemyProduction {
        for(int i=0i<cpuList.size;++i) {
            Vector2 temp1 = new Vector2(POSITIONS.get(currentPos).x, POSITIONS.get(currentPos).y)
            Vector2 temp2 = new Vector2(POSITIONS.get(currentPos).x, POSITIONS.get(currentPos).y)
            Vector2 facing = new Vector2(-temp1.sub(CENTER).y, temp2.sub(CENTER).x).nor()
            if(GameInstance.getInstance().difficultyConfig == 0) {
                enemyProduction = new EasyEnemyProduction(cpuList.get(i), POSITIONS.get(currentPos), facing)
            } else if(GameInstance.getInstance().difficultyConfig == 1) {
                enemyProduction = new MediumEnemyProduction(cpuList.get(i), POSITIONS.get(currentPos), facing)
            } else {
                enemyProduction = new HardEnemyProduction(cpuList.get(i), POSITIONS.get(currentPos), facing)
            }
            GameInstance.getInstance().factorys.add(enemyProduction)
            ++currentPos
        }
    }

    override def resize(width: Int, height: Int) {
        // implement this later. The bounding box logic is pretty messy.
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

    override def show() {
    }

    override def render(delta: Float) {
        delta = Math.min(0.06f, delta)

        backgroundFX.render()		

        Collision.collisionCheck()

        gameBatch.begin()
        // Bubbles
        GameInstance.getInstance().bubbleParticles.draw(gameBatch)
        GameInstance.getInstance().bigBubbleParticles.draw(gameBatch)

        // Factorys
        for (ship <- GameInstance.getInstance().factorys) {
            if (ship.alive) {
                ship.draw(gameBatch)
            } else {
                GameInstance.getInstance().factorys.removeValue(ship, true)
                if(GameInstance.getInstance().factorys.size < 2) gameOver = true
            }
        }

        // Frigate
        for (Ship ship : GameInstance.getInstance().frigates) {
            if (ship.alive) {
                ship.draw(gameBatch)
            } else {
                GameInstance.getInstance().frigates.removeValue(ship, true)
            }
        }

        // Bomber
        for (Ship ship : GameInstance.getInstance().bombers) {
            if (ship.alive) {
                ship.draw(gameBatch)
            } else {
                GameInstance.getInstance().bombers.removeValue(ship, true)
            }
        }
        // Fighter
        for (Ship ship : GameInstance.getInstance().fighters) {
            if (ship.alive) {
                ship.draw(gameBatch)
            } else {
                GameInstance.getInstance().fighters.removeValue(ship, true)
            }
        }

        // Laser
        for (Ship ship : GameInstance.getInstance().bullets) {
            if (ship.alive) {
                ship.draw(gameBatch)
            } else {
                GameInstance.getInstance().bullets.removeValue((Bullet) ship, true)
            }
        }

        // Explosions
        GameInstance.getInstance().sparkParticles.draw(gameBatch)
        GameInstance.getInstance().explosionParticles.draw(gameBatch)

        //		font.draw(gameBatch, "fps: " + Gdx.graphics.getFramesPerSecond(), 10, 30)
        gameBatch.end()

        //show touch area notification
        if(numPlayers > 0 && touchedP1) {
            touchFadeP1 = Math.max(touchFadeP1 - delta / 2.f, 0)
        }

        if(numPlayers > 0 && (!touchedP1 || touchFadeP1>0)) {
            gameBatch.begin()
            stouchAreaP1.setColor(stouchAreaP1.getColor().r, stouchAreaP1.getColor().g, stouchAreaP1.getColor().b, touchFadeP1)
            stouchAreaP1.draw(gameBatch)
            p1.setColor(p1.getColor().r, p1.getColor().g, p1.getColor().b, touchFadeP1)
            p1.draw(gameBatch)
            gameBatch.end()
        }

        if(numPlayers > 1 && touchedP2) {
            touchFadeP2 = Math.max(touchFadeP2 - delta / 2.f, 0)
        }
        if(numPlayers > 1 && (!touchedP2 || touchFadeP2>0)) {
            gameBatch.begin()
            stouchAreaP2.setColor(stouchAreaP2.getColor().r, stouchAreaP2.getColor().g, stouchAreaP2.getColor().b, touchFadeP2)
            stouchAreaP2.draw(gameBatch)
            p2.setColor(p2.getColor().r, p2.getColor().g, p2.getColor().b, touchFadeP2)
            p2.draw(gameBatch)
            gameBatch.end()
            }
        if(numPlayers > 2 && touchedP3) {
                touchFadeP3 = Math.max(touchFadeP3 - delta / 2.f, 0)
        }
        if(numPlayers > 2 && (!touchedP3 || touchFadeP3>0)) {
            gameBatch.begin()
            stouchAreaP3.setColor(stouchAreaP3.getColor().r, stouchAreaP3.getColor().g, stouchAreaP3.getColor().b, touchFadeP3)
            stouchAreaP3.draw(gameBatch)
            p3.setColor(p3.getColor().r, p3.getColor().g, p3.getColor().b, touchFadeP3)
            p3.draw(gameBatch)
            gameBatch.end()
        }
        if(numPlayers > 3 && touchedP4) {
            touchFadeP4 = Math.max(touchFadeP4 - delta / 2.f, 0)
        }
        if(numPlayers > 3 && (!touchedP4 || touchFadeP4>0)) {
            gameBatch.begin()
            stouchAreaP4.setColor(stouchAreaP4.getColor().r, stouchAreaP4.getColor().g, stouchAreaP4.getColor().b, touchFadeP4)
            stouchAreaP4.draw(gameBatch)
            p4.setColor(p4.getColor().r, p4.getColor().g, p4.getColor().b, touchFadeP4)
            p4.draw(gameBatch)
            gameBatch.end()
        }

        if (!gameOver && fade > 0 && fade < 100) {
            fade = Math.max(fade - delta / 2.f, 0)
            fadeBatch.begin()
            blackFade.setColor(blackFade.getColor().r, blackFade.getColor().g, blackFade.getColor().b, fade)
            blackFade.draw(fadeBatch)
            fadeBatch.end()
        }

        if(gameOver) {
            gameOverTimer -= delta
        }

        if (gameOver && gameOverTimer <= 0) {
            fade = Math.min(fade + delta / 2.f, 1)
            fadeBatch.begin()
            blackFade.setColor(blackFade.getColor().r, blackFade.getColor().g, blackFade.getColor().b, fade)
            blackFade.draw(fadeBatch)
            fadeBatch.end()
            if(fade>=1) game.setScreen(new MainMenu(game))
        }
    }

    @Override
    public void hide() {

    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK) {
            gameOver = true
            gameOverTimer=0
    }

    if(keycode == Input.Keys.ESCAPE) {
        gameOver = true
        gameOverTimer=0
    }		

    if(numPlayers >0 && keycode == Input.Keys.A && GameInstance.getInstance().factorys.size>0) {
        ((FactoryProduction) GameInstance.getInstance().factorys.get(0)).button_held = true
        touchedP1 = true
    } 
    if(numPlayers >1 && keycode == Input.Keys.F && GameInstance.getInstance().factorys.size>1) {
        ((FactoryProduction) GameInstance.getInstance().factorys.get(1)).button_held = true
        touchedP2 = true
    } 
    if(numPlayers >2 && keycode == Input.Keys.H && GameInstance.getInstance().factorys.size>2) {
        ((FactoryProduction) GameInstance.getInstance().factorys.get(2)).button_held = true
        touchedP3 = true
    } 
    if(numPlayers >3 && keycode == Input.Keys.L && GameInstance.getInstance().factorys.size>3) {
        ((FactoryProduction) GameInstance.getInstance().factorys.get(3)).button_held = true
        touchedP4 = true
    }
    return false
    }

    @Override
    public boolean keyUp(int keycode) {
        if(numPlayers >0 && keycode == Input.Keys.A && GameInstance.getInstance().factorys.size>0) {
            ((FactoryProduction) GameInstance.getInstance().factorys.get(0)).button_held = false
        } 
        if(numPlayers >1 && keycode == Input.Keys.F &&  GameInstance.getInstance().factorys.size>1) {
            ((FactoryProduction) GameInstance.getInstance().factorys.get(1)).button_held = false
        } 
        if(numPlayers >2 && keycode == Input.Keys.H &&  GameInstance.getInstance().factorys.size>1) {
            ((FactoryProduction) GameInstance.getInstance().factorys.get(2)).button_held = false
        } 
        if(numPlayers >3 && keycode == Input.Keys.L &&  GameInstance.getInstance().factorys.size>1) {
            ((FactoryProduction) GameInstance.getInstance().factorys.get(3)).button_held = false
        } 
        return false
        }

        @Override
        public boolean keyTyped(char character) {
            // TODO Auto-generated method stub
            return false
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        collisionRay = cam.getPickRay(x, y)

        if(numPlayers >0 && Intersector.intersectRayBoundsFast(collisionRay, touchAreaP1) && GameInstance.getInstance().factorys.size>0) {
            ((FactoryProduction) GameInstance.getInstance().factorys.get(0)).button_held = true
            pointerP1 = pointer
            touchedP1 = true
        } 
        if(numPlayers >1 && Intersector.intersectRayBoundsFast(collisionRay, touchAreaP2) && GameInstance.getInstance().factorys.size>1) {
            ((FactoryProduction) GameInstance.getInstance().factorys.get(1)).button_held = true
            pointerP2 = pointer
            touchedP2 = true
        } 
        if(numPlayers >2 && Intersector.intersectRayBoundsFast(collisionRay, touchAreaP3) && GameInstance.getInstance().factorys.size>2) {
            ((FactoryProduction) GameInstance.getInstance().factorys.get(2)).button_held = true
            pointerP3 = pointer
            touchedP3 = true
        } 
        if(numPlayers >3 && Intersector.intersectRayBoundsFast(collisionRay, touchAreaP4) && GameInstance.getInstance().factorys.size>3) {
            ((FactoryProduction) GameInstance.getInstance().factorys.get(3)).button_held = true
            pointerP4 = pointer
            touchedP4 = true
        } 
        return false
        }

        @Override
        public boolean touchUp(int x, int y, int pointer, int button) {
            collisionRay = cam.getPickRay(x, y)

            if(numPlayers >0 && pointer == pointerP1 && GameInstance.getInstance().factorys.size>0) {
                ((FactoryProduction) GameInstance.getInstance().factorys.get(0)).button_held = false
                pointerP1 = -1
            } 
            if(numPlayers >1 && pointer == pointerP2 &&  GameInstance.getInstance().factorys.size>1) {
                ((FactoryProduction) GameInstance.getInstance().factorys.get(1)).button_held = false
                pointerP2 = -1
            } 
            if(numPlayers >2 && pointer == pointerP3 &&  GameInstance.getInstance().factorys.size>1) {
                ((FactoryProduction) GameInstance.getInstance().factorys.get(2)).button_held = false
                pointerP3 = -1
            } 
            if(numPlayers >3 && pointer == pointerP4 &&  GameInstance.getInstance().factorys.size>1) {
                ((FactoryProduction) GameInstance.getInstance().factorys.get(3)).button_held = false
                pointerP4 = -1
            } 
            return false
            }

            @Override
            public boolean touchDragged(int x, int y, int pointer) {
                return false
        }

        @Override
        public boolean scrolled(int amount) {
            return false
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false
        }

            }

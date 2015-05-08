package srg.scala.paxbritannica.background

import com.badlogic.gdx.graphics.g2d.{Sprite, SpriteBatch}
import com.badlogic.gdx.math.{MathUtils, Vector2, Vector3}
import com.badlogic.gdx.utils.Array
import srg.scala.paxbritannica.Resources

class BackgroundFXRenderer {
    
    val NUM_FISHES = 15
    val NUM_DEBRISES = 30

	val debrises = new Array[Debris](NUM_DEBRISES)
    for(i <- 1 to NUM_DEBRISES) {
        debrises.add(new Debris(new Vector2(MathUtils.random(-100, 800),MathUtils.random(-200, 400))))
    }

    val fishes = new Array[Fish](NUM_FISHES)
    for(i <- 1 to NUM_FISHES) {
        fishes.add(new Fish(new Vector2(MathUtils.random(-100, 800),MathUtils.random(-200, 400))))
    } 

    val backgroundFXBatch = new SpriteBatch()
    backgroundFXBatch.getProjectionMatrix().setToOrtho2D(0, 0, 800, 480)

    val background = Resources.background

    val backgroundBatch = new SpriteBatch()
    backgroundBatch.getProjectionMatrix().setToOrtho2D(0, 0, 128, 128)

    var stateTime = 0f
    var lerpTarget = new Vector3()

    def render() {		
        backgroundBatch.begin()
        background.draw(backgroundBatch)
        backgroundBatch.end()

        backgroundFXBatch.begin()
        for (i <- 0 until debrises.size) {
            val debris = debrises.get(i)
            if (debris.alive) {
                debris.draw(backgroundFXBatch)
            } else {
                debris.reset()
            }
        }

        for (i <- 0 until fishes.size) {
            val fish = fishes.get(i)
            if (fish.alive) {
                fish.draw(backgroundFXBatch)
            } else {
                fish.reset()
            }
        }

        backgroundFXBatch.end()		
    }

    def resize(width: Int, height: Int) {
        backgroundFXBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height)
    }

    def dispose() {
        fishes.clear()
        debrises.clear()
        backgroundFXBatch.dispose()
        backgroundBatch.dispose()
    }

}

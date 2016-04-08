package srg.scala.paxbritannica.background

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.math.{MathUtils, Vector2}
import srg.scala.paxbritannica.Resources

class Debris (starting_position: Vector2) extends Sprite () {

    var position = starting_position
    this.setPosition(starting_position.x, starting_position.y)
    var facing = new Vector2(1, 0)

    this.facing.rotate(random_direction)
    this.setScale(random_scale, random_scale)

    val rn = (MathUtils.random(0, 2)) 
    rn match { 
        case 0 => this.set(Resources.debrisSmall)
        case 1 => this.set(Resources.debrisMed)
        case _ => this.set(Resources.debrisLarge)
    }

    var SPEED = 5.0f
    var LIFETIME = MathUtils.random(8, 12)
    var FADE_TIME = 2

    val random_direction = MathUtils.random(-360, 360)
    val random_scale = MathUtils.random() * 0.75f + 0.5f
    val random_speed = (MathUtils.random() * 2f) - 1f
    val random_opacity = MathUtils.random() * 0.35f + 0.6f

    var alive = true
    var since_alive = 0f
    var delta: Float = _

    override def draw(batch: Batch) {
        super.draw(batch)

        val delta = Math.min(0.06f, Gdx.graphics.getDeltaTime())
        since_alive = since_alive + delta

        facing.rotate((SPEED + random_speed) * delta).nor()
        position.add(facing.scl((SPEED + random_speed) * delta))
        this.setPosition(position.x, position.y)

        if (since_alive < FADE_TIME) {
            super.setColor(1, 1, 1, Math.min((since_alive / FADE_TIME) * random_opacity, random_opacity))
        } else {
            this.setColor(1, 1, 1, Math.min(1 - (since_alive - LIFETIME + FADE_TIME) / FADE_TIME, 1) * random_opacity)
        }
        if (since_alive > LIFETIME) {
            alive = false
            this.setColor(1, 1, 1, 0)
        }
    }

    def reset() {
        SPEED = 5.0f
        LIFETIME = MathUtils.random(8, 12)
        FADE_TIME = 2

        val random_direction = MathUtils.random(-360, 360)
        val random_scale = MathUtils.random() * 0.75f + 0.5f
        val random_speed = (MathUtils.random() * 2f) - 1f
        val random_opacity = MathUtils.random() * 0.35f + 0.6f

        alive = true
        since_alive = 0

        this.position = new Vector2(MathUtils.random(-100, 800), MathUtils.random(-100, 400))
        this.setPosition(position.x, position.y)
        this.facing.rotate(random_direction)
        this.setScale(random_scale, random_scale)
    }
}

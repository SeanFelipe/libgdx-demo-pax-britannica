package srg.scala.paxbritannica.background

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.math.{MathUtils, Vector2}
import srg.scala.paxbritannica.Resources

class Fish (starting_position: Vector2) extends Sprite () {

    var SPEED = 0.2f
	var LIFETIME = MathUtils.random(8, 12)
	var FADE_TIME = 2

	var random_direction : Float = _ 
    val rnd = MathUtils.random(-360, 360)
    if ( rnd < 0.5f ) {
        random_direction = 1
    } else {
        random_direction = -1
    }

	var random_scale = MathUtils.random() * 0.75f + 0.2f
	var random_speed = MathUtils.random() + 0.5f
	var random_opacity = MathUtils.random() * 0.1f + 0.1f
	var alive = true
	var since_alive = 0f
	var delta: Float = _

    var position = starting_position
    this.setPosition(starting_position.x, starting_position.y)
    this.setRotation(random_direction)
	this.setScale(random_scale, random_scale)

    val rnd2 = (MathUtils.random(0, 7))  
	rnd2 match { 
        case 0 => this.set(Resources.getInstance().fish1)
        case 1 => this.set(Resources.getInstance().fish2)
        case 2 => this.set(Resources.getInstance().fish3)
        case 3 => this.set(Resources.getInstance().fish4)
        case 4 => this.set(Resources.getInstance().fish5)
        case 5 => this.set(Resources.getInstance().fish6)
        case 6 => this.set(Resources.getInstance().fish7)
        case _ => this.set(Resources.getInstance().fish8)
    }
    if ( random_direction == -1 ) {
			flip(true, false)
		}
			
	override def draw(batch: Batch) {
		super.draw(batch)
		delta = Math.min(0.06f, Gdx.graphics.getDeltaTime())
		since_alive = since_alive + delta / 2f
		position.add((SPEED + random_speed) * delta * 5f * random_direction, 0)
		this.setPosition(position.x, position.y)
		
		if (since_alive < FADE_TIME) {
			super.setColor(1, 1, 1, Math.min((since_alive / FADE_TIME)*random_opacity,random_opacity))
		} else {
			this.setColor(1, 1, 1, Math.min(1 - (since_alive - LIFETIME + FADE_TIME) / FADE_TIME, 1) * random_opacity)
		}
		if (since_alive > LIFETIME) {
			alive = false
		}
	}
	
	def reset() {
		SPEED = 0.2f
		LIFETIME = MathUtils.random(8, 12)
		FADE_TIME = 2

        val rnd = MathUtils.random(-360, 360)
        if ( rnd < 0.5f ) {
            random_direction = 1
        } else {
            random_direction = -1
        }
		random_scale = MathUtils.random() * 0.75f + 0.2f
		random_speed = MathUtils.random() + 0.5f
		random_opacity = MathUtils.random() * 0.1f + 0.1f
		alive = true
		since_alive = 0
		
		this.position = new Vector2(MathUtils.random(-100, 800),MathUtils.random(-100, 400))

		if( random_direction == -1 ) {
			flip(true, false)
		}
		this.setPosition(position.x, position.y)
		this.setRotation(random_direction)
		this.setScale(random_scale, random_scale)
	}
}

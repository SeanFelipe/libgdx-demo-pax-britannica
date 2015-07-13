package srg.scala.paxbritannica.mainmenu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.math.{MathUtils, Vector2, Vector3}
import com.badlogic.gdx.math.collision.BoundingBox
import srg.scala.paxbritannica.Resources

class FactorySelector (starting_position: Vector2, id: Int) extends Sprite() {

    var position = starting_position
    setPosition(position.x, position.y)
    println("----------------------------------------")
    println(s"id: $id position: $position")


	var collision = new BoundingBox()
	var collisionMinVector = new Vector3()
	var collisionMaxVector = new Vector3()

    id match {
        case 1 => 
            this.set(Resources.factoryP1)
            collisionMinVector.set(205f, 120f, -10f)
            collisionMaxVector.set(85f, 300f, 10f)
            collision.set(collisionMinVector, collisionMaxVector);
        case 2 => 
            this.set(Resources.factoryP2)
            collisionMinVector.set(330f, 120f, -10f)
            collisionMaxVector.set(210f, 300f, 10f)
            collision.set(collisionMinVector, collisionMaxVector);
        case 3 => 
            this.set(Resources.factoryP3)
            collisionMinVector.set(455f, 120f, -10f)
            collisionMaxVector.set(335f, 300f, 10f)
            collision.set(collisionMinVector, collisionMaxVector);
        case _ => 
            this.set(Resources.factoryP4)
            collisionMinVector.set(580f, 120f, -10f)
            collisionMaxVector.set(460f, 300f, 10f)
            collision.set(collisionMinVector, collisionMaxVector);
    }

    setRotation(90)
    this.setColor(0, 0, 0, 1)

    var delta: Float = _
	var picked = false
	var playerSelect = false
	var cpuSelect = false

	var collisionPlayerSelect = new BoundingBox()
	var collisionCPUSelect = new BoundingBox()
	
	val button = new Sprite(Resources.aButton)
    button.setPosition(position.x + 70f, position.y + 35f)
		
    val aCpuButton = new Sprite(Resources.aCpuButton)
    aCpuButton.setPosition(position.x + 70f, position.y + 35f)
		
    val aPlayerButton = new Sprite(Resources.aPlayerButton)
    aPlayerButton.setPosition(position.x + 70f, position.y + 35f)
		
	val cpuButton = new Sprite(Resources.cpuButton)
    cpuButton.setPosition(position.x + 30f, position.y - 0f )
		
	val playerButton = new Sprite(Resources.playerButton)
	playerButton.setPosition(position.x + 30f, position.y + 70f )
		
	val pulse = (1 + MathUtils.cos(( pulse_time / 180f ) * 2f * MathUtils.PI )) / 2f
	val color = fade * pulse + 1 * (1 - pulse)

    this.setColor(color, color, color, 1)
    button.setColor(color, color, color, 1)
    cpuButton.setColor(color, color, color, 1)	

	var fade = 0.2f
	var fadeButton = 0.0f
	var pulse_time = 0f
	
	def reset() {
		picked = false
		cpuSelect = false
		playerSelect = false
		
		fade = 0.2f
		fadeButton = 0.0f

		pulse_time = 0		
		val pulse = (1 + MathUtils.cos(( pulse_time / 180f ) * 2f * MathUtils.PI )) / 2f
		val color = fade * pulse + 1 * (1 - pulse)
		this.setColor(color, color, color, 1)
		button.setColor(color, color, color, 1)
		cpuButton.setColor(color, color, color, 1)	
	}

	override def draw(batch: Batch) {
		delta = Math.min(0.06f, Gdx.graphics.getDeltaTime())
		
		super.draw(batch)
		
		pulse_time = pulse_time + Gdx.graphics.getDeltaTime()

		val pulse = (1 + MathUtils.cos(( pulse_time / 5f ) * 2f * MathUtils.PI )) / 2f
		val color = fade * pulse + 1 * ( 1 - pulse )
				
		if(picked && !(playerSelect || cpuSelect)) {
			button.draw(batch)
			button.setColor(0.2f, 0.2f, 0.2f, 1)
		} else {
			if(playerSelect) {
				aPlayerButton.draw(batch)
				aPlayerButton.setColor(color, color, color, 1)
			} else if(cpuSelect) {
				aCpuButton.draw(batch)
				aCpuButton.setColor(color, color, color, 1)
			} else {
				button.draw(batch)
				button.setColor(color, color, color, 1)
			}
		}
		
		if(picked && !(playerSelect || cpuSelect)) {
			fade = 0.2f
		    this.setColor(fade, fade, fade, 1)
			
		    fadeButton = Math.min(fadeButton + delta, 1)
			cpuButton.setColor(fadeButton, fadeButton, fadeButton, 1)
		    cpuButton.draw(batch)
		    
			playerButton.setColor(fadeButton, fadeButton, fadeButton, 1)
		    playerButton.draw(batch)

		} else if(playerSelect || cpuSelect) {
		    fade = Math.min(fade + delta, 1)
		    this.setColor(fade, fade, fade, 1)
		    
			fadeButton = Math.max(fadeButton - delta, 0)
			if(cpuSelect) {
				cpuButton.setColor(0, 0, 0, fadeButton)
			    cpuButton.draw(batch)
			} else {
				cpuButton.setColor(fadeButton, fadeButton, fadeButton, fadeButton)
			    cpuButton.draw(batch)
			}
		    
			if(playerSelect) {
				playerButton.setColor(0, 0, 0, fadeButton)
			    playerButton.draw(batch)
			} else {
				playerButton.setColor(fadeButton, fadeButton, fadeButton, fadeButton)
			    playerButton.draw(batch)
			}
		}

		
	}
}

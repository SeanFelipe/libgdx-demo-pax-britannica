package srg.scala.paxbritannica

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2

class Bullet (id: Int, starting_position: Vector2, facing: Vector2) extends Ship (id, starting_position, facing) {

	val buffer = 500f
	val damage = 0f
	val bulletSpeed = 0f

	override def draw(batch: Batch) {
		if( alive == false ) return
		if( ! Targeting.onScreen(collisionCenter,buffer)) {
			alive = false
		} else if( velocity.len() <= 5 ) {
			alive = false
			GameInstance.getInstance().explosionParticles.addTinyExplosion(collisionCenter)
		} else {		
			super.draw(batch)
		}
		
	}
}

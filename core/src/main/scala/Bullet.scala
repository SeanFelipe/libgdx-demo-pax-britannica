package srg.scala.paxbritannica

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2

class Bullet (id: Int, position: Vector2, facing: Vector2) 
  extends Ship (id, position, facing) {

	val buffer = 500
	var damage = 0
	var bulletSpeed = 0f

	override def draw(batch: Batch) {
		if(alive == false) return
		if( ! Targeting.onScreen(collisionCenter,buffer)) {
			alive = false
		} else if(velocity.len() <= 5) {
			alive = false
			GameInstance.getInstance().explosionParticles.addTinyExplosion(collisionCenter)
		} else {		
			super.draw(batch)
		}
		
	}
}

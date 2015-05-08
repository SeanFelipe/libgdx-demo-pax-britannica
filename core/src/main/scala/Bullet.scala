package srg.scala.paxbritannica

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import srg.scala.paxbritannica.GameInstance

class Bullet (id: Int, position: Vector2, facing: Vector2, bspeed: Float = 0, bdamage: Int = 0) 
  extends Ship (id, position, facing) {

	val buffer = 500
	var damage = bdamage
	var bulletSpeed = bspeed

	override def draw(batch: Batch) {
		if(alive == false) return
		if( ! Targeting.onScreen(collisionCenter,buffer)) {
			alive = false
		} else if(velocity.len() <= 5) {
			alive = false
			GameInstance.explosionParticles.addTinyExplosion(collisionCenter)
		} else {		
			super.draw(batch)
		}
		
	}
}

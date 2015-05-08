package srg.scala.paxbritannica.fighter

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2

import srg.scala.paxbritannica.Bullet
import srg.scala.paxbritannica.Resources

class Laser (id: Int , position: Vector2 , facing: Vector2 ) 
    extends Bullet (id, position, facing) {

    override val bulletSpeed = 1000f
    override val damage = 10
		
    this.velocity = new Vector2().set(facing).scl(bulletSpeed)
    
    this.set(Resources.laser)
    this.setOrigin(0,0)
	
	override def draw(batch: Batch ) {
		delta = Math.min(0.06f, Gdx.graphics.getDeltaTime())
		velocity.scl((Math.pow(1.03f, delta * 30f).toFloat))
		super.draw(batch)
	}
}

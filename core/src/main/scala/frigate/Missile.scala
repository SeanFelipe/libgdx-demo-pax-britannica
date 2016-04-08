package srg.scala.paxbritannica.frigate

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2

import srg.scala.paxbritannica.{ Bullet, Resources }

class Missile (id: Int , position: Vector2 , facing: Vector2 )
    extends Bullet (id, position, facing) {

    val ai = new MissileAI(this)
    override val turnSpeed = 300f
    override val accel = 300f
    override val bulletSpeed = 50f
    override val damage = 50
    this.velocity = new Vector2().set(facing).scl(bulletSpeed)
    this.set(Resources.missile)
    this.setOrigin(this.getWidth() / 2, this.getHeight() / 2)
    
    override def draw(batch: Batch ) {
        ai.update()
        super.draw(batch)
    }
}

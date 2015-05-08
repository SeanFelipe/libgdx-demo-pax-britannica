package srg.scala.paxbritannica.bomber

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2

import srg.scala.paxbritannica.{ GameInstance, Resources, Ship }

class Bomber (id: Int, position: Vector2, facing: Vector2)
    extends Ship (id, position, facing, 440) {

	val ai = new BomberAI(this)

    override val turnSpeed = 45f
    override val accel = 45.0f
    
    id match {
        case 1 => this.set(Resources.bomberP1);
        case 2 => this.set(Resources.bomberP2);
        case 3 => this.set(Resources.bomberP3);
        case _ => this.set(Resources.bomberP4);
    }

    this.setOrigin(this.getWidth() / 2, this.getHeight() / 2)

	override def draw(batch: Batch) {
		ai.update()
		super.draw(batch)
	}

	def shoot(approach: Int) {
		 val bombFacing = new Vector2().set(facing).rotate(90 * approach)
		 GameInstance.bullets.add(new Bomb(id, collisionCenter, bombFacing))
	}
}

package srg.scala.paxbritannica.bomber

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2

import srg.scala.paxbritannica.{ GameInstance, Resources, Ship }

class Bomber (int id, Vector2 position, Vector2 facing)
    extends Ship (id, position, facing) {

	val ai = new BomberAI(this)

    val turnSpeed = 45f
    val accel = 45.0f
    var hitPoints = 440
    
    id match {
        case 1 => this.set(Resources.getInstance().bomberP1);
        case 2 => this.set(Resources.getInstance().bomberP2);
        case 3 => this.set(Resources.getInstance().bomberP3);
        case _ => this.set(Resources.getInstance().bomberP4);
    }

    this.setOrigin(this.getWidth()/2, this.getHeight()/2)

	override def draw(batch: Batch) {
		ai.update()
		super.draw(batch)
	}

	def shoot(approach: Int) {
		 val bombFacing = new Vector2().set(facing).rotate(90 * approach)
		 GameInstance.getInstance().bullets.add(new Bomb(id, collisionCenter, bombFacing))
	}
}

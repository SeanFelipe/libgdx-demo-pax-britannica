package srg.scala.paxbritannica.bomber

import com.badlogic.gdx.math.Vector2

import srg.scala.paxbritannica.Bullet
import srg.scala.paxbritannica.Resources

class Bomb (id: Int , position: Vector2 , facing: Vector2 ) 
    extends Bullet (id, position, facing, 150, 300) {
		this.velocity = new Vector2().set(facing).scl(bulletSpeed)
		this.set(Resources.getInstance().bomb)
		this.setOrigin(this.getWidth() / 2, this.getHeight() / 2)
}

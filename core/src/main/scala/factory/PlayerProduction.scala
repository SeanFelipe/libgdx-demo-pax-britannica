package srg.scala.paxbritannica.factory

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2

class PlayerProduction (id: Int , position: Vector2 , facing: Vector2 ) 
    extends FactoryProduction (id, position, facing) {
		
	override def draw(batch: Batch) {
		thrust()
		turn(1)
		super.draw(batch)
	}
}

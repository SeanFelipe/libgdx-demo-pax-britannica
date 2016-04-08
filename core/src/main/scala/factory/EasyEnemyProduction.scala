package srg.scala.paxbritannica.factory

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.MathUtils

class EasyEnemyProduction (id: Int , position: Vector2 , facing: Vector2 ) 
    extends BaseEnemyProduction (id, position, facing) {

        override def doProduction {
        if(ownFighters > 4 && ownBombers > 3 && ownFrigates > 2) action = 0
        else action = MathUtils.random(-1, 2)
    }
}

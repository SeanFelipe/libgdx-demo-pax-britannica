package srg.scala.paxbritannica.factory

import com.badlogic.gdx.math.Vector2

class MediumEnemyProduction (id: Int , position: Vector2 , facing: Vector2 ) 
    extends BaseEnemyProduction (id, position, facing) {

        override def doProduction {
            if(resourceAmount < 80) action = -1
            else if(ownFighters < 5) action = 0
            else if(ownBombers < 4) action = 1
            else if(ownFrigates < 3) action = 2
            else action = 3
    }
}

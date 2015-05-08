package srg.scala.paxbritannica.factory

import com.badlogic.gdx.math.Vector2

class HardEnemyProduction (id: Int , position: Vector2 , facing: Vector2 ) 
    extends BaseEnemyProduction (id, position, facing) {

        override def doProduction() {
            if(resourceAmount < 80) action = -1
            else if((ownFighters < 4 || enemyBombers > 2) && enemyFighters < 11 && enemyFrigates < 4 && ownFighters < 20) action = 0
            else if((enemyFrigates > 1 && ownBombers < 1) && enemyFighters < 11) action = 1
            else if(enemyFighters < 5 && ownBombers < 5) action = 1
            else if(enemyFighters >= 5 && ownFrigates < 3) action = 2
            else if(ownFrigates < 1) action = 2
            else if(ownFighters < 10) action = 0
            else if(ownBombers > 4 && ownFrigates >= 1 && ownFighters >= 10) action = 3
	}
}

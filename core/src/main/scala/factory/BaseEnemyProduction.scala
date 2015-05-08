package srg.scala.paxbritannica.factory

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2

import srg.scala.paxbritannica.GameInstance
import srg.scala.paxbritannica.Ship
import srg.scala.paxbritannica.bomber.Bomber
import srg.scala.paxbritannica.fighter.Fighter
import srg.scala.paxbritannica.frigate.Frigate

abstract class BaseEnemyProduction (id: Int , position: Vector2 , facing: Vector2 ) 
    extends FactoryProduction (id, position, facing) {

	var script_index = 0
    var action_index = 0
	var timeToHold = 0f
	var accumulated_frames = 0f
	var frames_to_wait = 0f

	var action = -1
	var enemyFighters, enemyBombers, enemyFrigates = 0
    var ownFighters, ownBombers, ownFrigates = 0
	
	override def draw(batch: Batch) {
		super.draw(batch)

		delta = Math.min(0.06f, Gdx.graphics.getDeltaTime())
		accumulated_frames += 30 * delta

		if (production.currentBuildingUnit != action && action > -1) {
			button_held = true
		} else {
			button_held = false
			next_action()
		}

		thrust()
		turn(1)
	}

	def next_action() {
		action = -1		
		enemyFighters = 0
		enemyBombers = 0
		enemyFrigates = 0
		ownFighters = 0
		ownBombers = 0
		ownFrigates = 0				
		accumulated_frames = 0
		timeToHold = 0

        val fa = GameInstance.fighters
		for (i <- 0 until fa.size) {
            val fighter = fa.get(i)
			if(fighter.id != this.id) {
				if(fighter.asInstanceOf[Fighter].ai.target != null &&
                    fighter.asInstanceOf[Fighter].ai.target.id == this.id) {
					enemyFighters = enemyFighters + 1
				}
			}
			else ownFighters = ownFighters + 1
		}
		
        val bo = GameInstance.bombers
		for (i <- 0 until bo.size) {
            val bomber = bo.get(i)
			if(bomber.id != this.id) {
				if(bomber.asInstanceOf[Bomber].ai.target != null && bomber.asInstanceOf[Bomber].ai.target.id == this.id) {
					enemyBombers = enemyBombers + 1
				}
			}
			else ownBombers = ownBombers + 1
		}
		
        val fr = GameInstance.frigates
		for (i <- 0 until fr.size) {
            val frigate = fr.get(i)
			if(frigate.id != this.id) {
				if(frigate.asInstanceOf[Frigate].ai.target != null &&
                    frigate.asInstanceOf[Frigate].ai.target.id == this.id) {
					enemyFrigates = enemyFrigates + 1
				}
			}
			else ownFrigates = ownFrigates + 1
		}
		
	}

    def doProduction {
    }


}

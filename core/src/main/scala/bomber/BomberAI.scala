package srg.scala.paxbritannica.bomber

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.{ MathUtils, Vector2 }

import srg.scala.paxbritannica.{ Ship, Targeting }
import srg.scala.paxbritannica.frigate.Frigate

class BomberAI (bbomber: Bomber) {
	val APPROACH_DISTANCE = 210f
	val COOLDOWN_DURATION = 0.6f
	val MAX_SHOTS = 4f

	var cooldown_timer = 0f
	var shots_counter = MAX_SHOTS
	var approach_sign = 1
	var delta: Float = _

	// 0 = approach
	// 1 = turn
	// 2 = shoot
	// 3 = move_away
	var state = 0
	
	//recycle
	var target_direction = new Vector2()
	var target : Ship = _
	val bomber = bbomber

	def retarget {
        val t = Targeting.getNearestOfType(bomber, 2) 
        if ( t != null ) target = t.getOrElse(null)
        else target = Targeting.getNearestOfType(bomber, 3).getOrElse(null)
	}

	def reviseApproach() {
		if (MathUtils.random() < 0.5) {
			approach_sign = 1
		} else {
			approach_sign = -1
		}
	}

	def update() {
		delta = Math.min(0.06f, Gdx.graphics.getDeltaTime())
		
		if (target == null || !target.alive || MathUtils.random() < 0.005f) {
			val old_target = target
			retarget
			if (old_target != null && target != null && old_target.id != target.id) {
				reviseApproach()
			}
		}

		if (target != null) {
			val target_distance = target.collisionCenter.dst(bomber.collisionCenter)
			target_direction.set(target.collisionCenter).sub(bomber.collisionCenter).nor()

			var unit_factor = 0f
			if (target.isInstanceOf[Frigate]) {
				unit_factor = 0.6f
			} else {
				unit_factor = 1
			}

			if (target_distance > (APPROACH_DISTANCE + 50) * unit_factor) {
				state = 0
			}

			// approach
			if (state == 0) {
				bomber.goTowards(target.collisionCenter, true)
				if (target_distance < APPROACH_DISTANCE * unit_factor) {
					reviseApproach()
					state = 1
				}
			}
			// turn
			else if (state == 1) {
				bomber.turn(-approach_sign)
				bomber.thrust(unit_factor * 0.75f)
				if (target_direction.dot(bomber.facing) < 0.5f) {
					state = 2
				}
			}
			// shoot
			else if (state == 2) {
				bomber.turn(approach_sign * 0.05f)
				bomber.thrust(unit_factor * 0.75f)

				cooldown_timer -= delta
				if (cooldown_timer <= 0) {
					bomber.shoot(approach_sign)
					cooldown_timer = COOLDOWN_DURATION
					shots_counter -= 1

					if (shots_counter == 0) {
						shots_counter = MAX_SHOTS
						state = 3
					}
				}
			}
			// move_away
			else if (state == 3) {
				bomber.goAway(target.collisionCenter, true)
			}
		}
	}
}

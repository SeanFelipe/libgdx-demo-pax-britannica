package srg.scala.paxbritannica.fighter

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import srg.scala.paxbritannica.Ship
import srg.scala.paxbritannica.Targeting

class FighterAI (ffighter: Fighter ) {

    val fighter = ffighter
	// shot range
	var shot_range = 200
	// try to stay this far away when you're out of ammo
	var run_distance = 200
	// true when we've shot everything and want to make a distance, false means
	// we're approaching to attack
	var running = false
	var target : Ship = _
	var on_screen = true
	//recycle vars
	var to_target = new Vector2()

    retarget()

	def retarget() {
		val t = Targeting.getNearestOfType(fighter, 1)
        target = t.getOrElse(null)
		if (target == null) {
			val t = Targeting.getNearestOfType(fighter, 0)
            target = t.getOrElse(null)
		}
		if (target == null) {
			val t = Targeting.getNearestOfType(fighter, 2)
            target = t.getOrElse(null)
		}
		if (target == null) {
			val t = Targeting.getNearestOfType(fighter, 3)
            target = t.getOrElse(null)
		}
	}

	def update() {
		// if we go from on to off screen, retarget
		val new_on_screen = Targeting.onScreen(fighter.collisionCenter)
		if (on_screen && !new_on_screen || target == null || !target.alive || MathUtils.random() < 0.005f) {
			retarget()
		}
		on_screen = new_on_screen

		if (target != null) {
			to_target.set(target.collisionCenter.x - fighter.collisionCenter.x, target.collisionCenter.y - fighter.collisionCenter.y)
			val dist_squared = to_target.dot(to_target)

			if (running) {
				// run away until you have full ammo and are far enough away
				val too_close = dist_squared < Math.pow(run_distance, 2)
				// if you're too close to the target then turn away
				if (too_close) {
					fighter.goAway(target.collisionCenter, true)
				} else {
					fighter.thrust()
				}

				if (! fighter.isEmpty && ! too_close) {
					running = false
				}
			} else {
				// go towards the target and attack!
				fighter.goTowards(target.collisionCenter, true)

				// maybe shoot
				if (fighter.isReadyToShoot()) {
					if (dist_squared <= shot_range * shot_range && to_target.dot(fighter.facing) > 0 && Math.pow(to_target.dot(fighter.facing), 2) > 0.97 * dist_squared) {
						fighter.shoot()
					}
				}

				// if out of shots then run away
				if (fighter.isEmpty) {
					running = true
				}
			}
		}
	}
}

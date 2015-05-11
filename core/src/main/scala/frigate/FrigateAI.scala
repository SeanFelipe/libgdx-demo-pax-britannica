package srg.scala.paxbritannica.frigate

import com.badlogic.gdx.math.{ MathUtils, Vector2 }
import srg.scala.paxbritannica.{ Ship, Targeting }

class FrigateAI (ffrigate: Frigate ) {
	val target_fuzzy_pos = new Vector2()
	var stopping = false
	var target : Ship = _
	val frigate = ffrigate


    def retarget() {
        val t = Targeting.getNearestOfType(frigate, 0) 	
		target = t.getOrElse(null)
		if (target == null) { 
            val t = Targeting.getNearestOfType(frigate, 1) 	
            target = t.getOrElse(null)
        }
		if (target == null) { 
            val t = Targeting.getNearestOfType(frigate, 2) 	
            target = t.getOrElse(null)
        }
		if (target == null) { 
            val t = Targeting.getNearestOfType(frigate, 3) 	
            target = t.getOrElse(null)
        }
		
		if (target != null) {
			val random = new Vector2(
                MathUtils.cos(((MathUtils.random() * MathUtils.PI * 2).toFloat * Math.sqrt(MathUtils.random()).toFloat)),
                MathUtils.sin(((MathUtils.random() * MathUtils.PI * 2).toFloat * Math.sqrt(MathUtils.random()).toFloat))
            );
			target_fuzzy_pos.set(target.collisionCenter).add(random.scl(250))
		}
	}

	def update() {
		if (target == null || !target.alive || MathUtils.random() < 0.001f) {
			retarget()
		}

		if (target != null) {
			val target_distance = target.collisionCenter.dst(frigate.collisionCenter)
			val speed_square = frigate.velocity.dot(frigate.velocity)

			if (frigate.isReadyToShoot() && speed_square > 0) {
		      stopping = true
			} else if(frigate.isEmpty()) {
		      stopping = false
			}

		    if(!stopping) {
		      if(target_distance < 150) {
		        //not too close!
		        frigate.goAway(target_fuzzy_pos, true)
		      } else {
		        frigate.goTowards(target_fuzzy_pos, true)
		      }
		    }
		    
		    // Shoot when not moving and able to fire
		    if(!frigate.isEmpty() && speed_square < 0.1) {
		        frigate.shoot()
		    }
			
		}
	}
}

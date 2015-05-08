package srg.scala.paxbritannica

import com.badlogic.gdx.math.{ MathUtils, Vector2 }
import com.badlogic.gdx.utils.Array

import srg.scala.paxbritannica.bomber.Bomber
import srg.scala.paxbritannica.factory.FactoryProduction
import srg.scala.paxbritannica.fighter.Fighter
import srg.scala.paxbritannica.frigate.Frigate

object Targeting {

    def onScreen( position: Vector2 ) : Boolean = {
        return onScreen(position, 0);
    }

	def onScreen( position: Vector2, buffer: Float ) : Boolean = {
        val left_ok = position.x >= Constants.screenLeft - buffer
        val right_ok = position.x <= Constants.screenRight + buffer
        val bottom_ok = position.y >= Constants.screenBottom + buffer
        val top_ok = position.y <= Constants.screenTop + buffer
        return left_ok && right_ok && bottom_ok && top_ok
	}

	/*
	 * returns the closest target of the given type 0 = Fighter 1 = Bomber 2 =
	 * Frigate 3 = Factory
	 */
	def getNearestOfType(source: Ship , shipType: Int) : Option[Ship] = {
		if (shipType == 0)
			return getNearestOfType(source, GameInstance.fighters)
		else if (shipType == 3)
			return getFactoryWithHighestHealth(source, GameInstance.factorys)
		else if (shipType == 1)
			return getNearestOfType(source, GameInstance.bombers)
		else if (shipType == 2)
			return getNearestOfType(source, GameInstance.frigates)
		else
			return null
	}

	def getFactoryWithHighestHealth(source: Ship,  ships: Array[Ship]) : Option[Ship] = {
		// find the closest one!
		var highestHealth = Float.MinValue
        var closestShip = None:Option[Ship]

        // libgdx Arrays don't support foreach
		for ( i <- 0 until ships.size ) {
			val ship = ships.get(i)
            // don't know how to cast this way yet in Scala. Punt for now.
			// val currentHealth = ship.hitPoints + (((FactoryProduction)ship).harvestRate * 500)
			val currentHealth = ship.hitPoints 

			if (ship.alive && source.id != ship.id && onScreen(ship.collisionCenter) && (currentHealth > highestHealth)) {
				closestShip = Some(ship)
				highestHealth = currentHealth
			}
		}

		return closestShip
	}
	
	def getNearestOfType( source: Ship, ships: Array[Ship] ) : Option[Ship] = {
		// find the closest one!
		var closestShip = None:Option[Ship]
		var closestDistanze = Float.MaxValue


		for (i <- 0 until ships.size) {
            var continue = false
			val ship = ships.get(i)
			val currentDistance = source.collisionCenter.dst(ship.collisionCenter)

			if (ship.alive && source.id != ship.id && onScreen(ship.collisionCenter) && (currentDistance < closestDistanze)) {
				//skip if ship is not targeting source ship
				if (ship.isInstanceOf[Fighter]) {
					if (( ship.asInstanceOf[Fighter] ).ai.target != null && ( ship.asInstanceOf[Fighter]).ai.target.id != source.id) {
						continue = true
					}
				}
				if (ship.isInstanceOf[Bomber]) {
					if (( ship.asInstanceOf[Bomber] ).ai.target != null && ( ship.asInstanceOf[Bomber] ).ai.target.id != source.id) {
						continue = true
					}
				}
				if (ship.isInstanceOf[Frigate]) {
					if (( ship.asInstanceOf[Frigate] ).ai.target != null && (ship.asInstanceOf[Frigate]).ai.target.id != source.id) {
						continue = true
					}
				}
                if ( ! continue ) {
                    closestShip = Some(ship)
                    closestDistanze = currentDistance
                }
			}
		}

		return closestShip
	}

	/*
	 * return a random ship of the desired type that's in range
	 * 0 = Fighter 1 = Bomber 2 = Frigate 3 = Factory
	 */
	def getTypeInRange(source: Ship, shipType: Int, range: Float) : Option[Ship] = {
		if (shipType == 0)
			return getTypeInRange(source, GameInstance.fighters, range)
		else if (shipType == 3)
			return getTypeInRange(source, GameInstance.factorys, range)
		else if (shipType == 1)
			return getTypeInRange(source, GameInstance.bombers, range)
		else if (shipType == 2)
			return getTypeInRange(source, GameInstance.frigates, range)
		else
			return null
	}

	/**
	 * return a random ship of the desired type that's in range
	 */
	def getTypeInRange( source: Ship, ships: Array[Ship], range: Float) : Option[Ship] = {
		var shipsInRange = new Array[Ship]()
		val range_squared = range * range

		for ( i <- 0 until ships.size ) {
			val ship = ships.get(i)
			val currentDistance = source.collisionCenter.dst(ship.collisionCenter)

			if (ship.alive && source.id != ship.id && onScreen(ship.collisionCenter) && (currentDistance < range_squared)) {
				shipsInRange.add(ship)
			}
		}

		if (shipsInRange.size > 0) {
			return Some(shipsInRange.get(MathUtils.random(0, shipsInRange.size - 1)))
		} else {
			return None 
		}
	}
}

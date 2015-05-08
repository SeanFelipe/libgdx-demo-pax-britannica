package srg.scala.paxbritannica

import com.badlogic.gdx.math.Intersector

object Collision {

	def collisionCheck {
        val bullets = GameInstance.bullets

		for (i <- 0 until bullets.size) {
			val bullet = bullets.get(i)
			if (bullet.alive) {
				for (n <- 0 until GameInstance.fighters.size) {
					val ship = GameInstance.fighters.get(n)
					collisionCheck(bullet, ship)
				}
				for (n <- 0 until GameInstance.bombers.size) {
					val ship = GameInstance.bombers.get(n)
					collisionCheck(bullet, ship)
				}
				for (n <- 0 until GameInstance.frigates.size) {
					val ship = GameInstance.frigates.get(n)
					collisionCheck(bullet, ship)
				}
				for (n <- 0 until GameInstance.factorys.size) {
					val ship = GameInstance.factorys.get(n)
					collisionCheck(bullet, ship)
				}
			}
		}
	}

	def collisionCheck(bullet: Bullet, ship: Ship) {
		if ( bullet.id != ship.id && ship.alive) {
			
			for(i <- 0 until ship.collisionPoints.size) {
				if(Intersector.isPointInPolygon(bullet.collisionPoints, ship.collisionPoints.get(i))) {
					ship.damage(bullet.damage)
					GameInstance.bulletHit(ship, bullet)
					bullet.alive = false
					return
				}
			}
			
			for(i <- 0 until bullet.collisionPoints.size) {
				if(Intersector.isPointInPolygon(ship.collisionPoints, bullet.collisionPoints.get(i))) {
					ship.damage(bullet.damage)
					GameInstance.bulletHit(ship, bullet)
					bullet.alive = false
					return
				}
			}
		}
	}

}

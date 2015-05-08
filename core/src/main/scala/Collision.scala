package srg.scala.paxbritannica

import com.badlogic.gdx.math.Intersector

object Collision {

	def collisionCheck {
        val bullets = GameInstance.getInstance().bullets

		for (i <- 0 until bullets.size) {
			val bullet = bullets.get(i)
			if (bullet.alive) {
				for (n <- 0 until GameInstance.getInstance().fighters.size) {
					val ship = GameInstance.getInstance().fighters.get(n)
					collisionCheck(bullet, ship)
				}
				for (n <- 0 until GameInstance.getInstance().bombers.size) {
					val ship = GameInstance.getInstance().bombers.get(n)
					collisionCheck(bullet, ship)
				}
				for (int n=0 n< GameInstance.getInstance().frigates.size;n++) {
					val ship = GameInstance.getInstance().frigates.get(n)
					collisionCheck(bullet, ship)
				}
				for (int n=0 n< GameInstance.getInstance().factorys.size;n++) {
					val ship = GameInstance.getInstance().factorys.get(n)
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
					GameInstance.getInstance().bulletHit(ship, bullet)
					bullet.alive = false
					return
				}
			}
			
			for(i <- 0 until bullet.collisionPoints.size) {
				if(Intersector.isPointInPolygon(ship.collisionPoints, bullet.collisionPoints.get(i))) {
					ship.damage(bullet.damage)
					GameInstance.getInstance().bulletHit(ship, bullet)
					bullet.alive = false
					return
				}
			}
		}
	}

}

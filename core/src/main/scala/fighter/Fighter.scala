package srg.scala.paxbritannica.fighter

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2

import srg.scala.paxbritannica.GameInstance
import srg.scala.paxbritannica.Resources
import srg.scala.paxbritannica.Ship

class Fighter (id: Integer , position: Vector2 , facing: Vector2 ) 
    extends Ship (id, position, facing, 40) {

	val shotCooldownTime = 6f
	val shotCapacity = 5f
	val shotReloadRate = 1f

	var shots = shotCapacity
	var cooldown = 0f

	val ai = new FighterAI(this)

    override val turnSpeed = 120f
    override val accel = 120.0f
		
	id match {
        case 1 => this.set(Resources.getInstance().fighterP1)
        case 2 => this.set(Resources.getInstance().fighterP2)
        case 3 => this.set(Resources.getInstance().fighterP3)
        case _ => this.set(Resources.getInstance().fighterP4)
    }
    this.setOrigin(this.getWidth()/2, this.getHeight()/2)

	override def draw(batch: Batch ) {
		delta = Math.min(0.06f, Gdx.graphics.getDeltaTime())
		ai.update()
		cooldown = Math.max(0, cooldown - delta*50f)
		shots = Math.min(shots + (shotReloadRate * Gdx.graphics.getDeltaTime()), shotCapacity)
		super.draw(batch)
	}

	def isEmpty : Boolean = {
		return shots < 1
	}

	def isReloaded : Boolean = {
		return shots == shotCapacity
	}

	def isCooledDown : Boolean = {
		return cooldown == 0
	}

	def isReadyToShoot() : Boolean = {
		return isCooledDown && ! isEmpty
	}

	def shoot() {
		if (cooldown == 0 && shots >= 1) {
			shots -= 1
			cooldown = shotCooldownTime
			GameInstance.bullets.add(new Laser(id,collisionCenter, facing))
		}
	}
}

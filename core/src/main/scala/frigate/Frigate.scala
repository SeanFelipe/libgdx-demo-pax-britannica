package srg.scala.paxbritannica.frigate

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2

import srg.scala.paxbritannica.{ GameInstance, Resources, Ship }

class Frigate (id: Int, position: Vector2 , facing: Vector2 ) 
    extends Ship (id, position, facing, 2000) {

	val shotCooldownTime = 5
	val shotCapacity = 8
	val shotReloadRate = 1f

	var shots = 0
	var cooldown = 0
	
	val ai = new FrigateAI(this)

    override val turnSpeed = 20f
    override val accel = 14.0f

    id match {
        case 1 => this.set(Resources.frigateP1)
        case 2 => this.set(Resources.frigateP2)
        case 3 => this.set(Resources.frigateP3)
        case _ => this.set(Resources.frigateP4)
    }
    this.setOrigin(this.getWidth() / 2, this.getHeight() / 2)

    override def draw(batch: Batch ) {
        delta = Math.min(0.06f, Gdx.graphics.getDeltaTime())

        ai.update()

        cooldown = Math.max(0, (cooldown - delta * 50f).toInt)
        shots = Math.min((shots + (shotReloadRate * delta)).toInt, shotCapacity)

        super.draw(batch)
    }

    def isEmpty() : Boolean = {
        return shots < 1
    }

    def isReloaded() : Boolean = {
        return shots == shotCapacity
    }

    def isCooledDown() : Boolean = {
        return cooldown == 0
    }

    def isReadyToShoot() : Boolean = {
        return isCooledDown() && isReloaded()
    }

    def shoot() {
        if (cooldown == 0 && shots >= 1) {
            shots -= 1
            cooldown = shotCooldownTime
            GameInstance.bullets.add(new Missile(id, collisionCenter, facing))
    }
        }

    }

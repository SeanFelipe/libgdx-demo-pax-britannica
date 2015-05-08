package srg.scala.paxbritannica

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{ Batch, Sprite, SpriteBatch }
import com.badlogic.gdx.math.{ MathUtils, Vector2 }
import com.badlogic.gdx.utils.Array

import srg.scala.paxbritannica.factory.FactoryProduction

class Ship (iid: Int, pposition: Vector2, ffacing: Vector2, hp: Integer = 0) extends Sprite() {

	val id = iid
	val amount = 1.0f
	val turnSpeed = 1.0f
	val accel = 0.0f
	var hitPoints = hp
	var maxHitPoints = 0
	var delta = 0.0f
	var aliveTime = 0.0f
	var velocity, collisionCenter = new Vector2()

	var position = pposition
    var facing = ffacing
    var target_direction : Vector2 = _

	var collisionPoints = new Array[Vector2]()
    collisionPoints.clear()
    collisionPoints.add(new Vector2())
    collisionPoints.add(new Vector2())
    collisionPoints.add(new Vector2())
    collisionPoints.add(new Vector2())
    
	var alive = true
	var deathCounter = 50f
	var nextExplosion = 10f
	var opacity = 5.0f

    this.setOrigin(this.getWidth() / 2f, this.getHeight() / 2f)

	override def draw(batch: Batch) {
		delta = Math.min(0.06f, Gdx.graphics.getDeltaTime())
		
		aliveTime = aliveTime + delta
		collisionPoints.get(0).set( this.getVertices()(0), this.getVertices()(1))
		collisionPoints.get(1).set( this.getVertices()(5), this.getVertices()(6))
		collisionPoints.get(2).set( this.getVertices()(10), this.getVertices()(11))
		collisionPoints.get(3).set( this.getVertices()(15), this.getVertices()(16))
		
		collisionCenter.set(collisionPoints.get(0)).add(collisionPoints.get(2)).scl(0.5f)

		velocity.scl(Math.pow(0.97f, delta * 30f).toFloat)
		position.add(velocity.x * delta, velocity.y * delta)
		
		this.setRotation(facing.angle())
		this.setPosition(position.x, position.y)

		if ( ! (this.isInstanceOf[Bullet]) && hitPoints <= 0)
			destruct
		if (MathUtils.random() < velocity.len() / 900f) {
			GameInstance.bubbleParticles.addParticle(randomPointOnShip)
		}
		super.draw(batch)
	}

	def turn(direction: Float) {
		delta = Math.min(0.06f, Gdx.graphics.getDeltaTime())
		facing.rotate(direction * turnSpeed * delta).nor()
	}

	def thrust = {
		delta = Math.min(0.06f, Gdx.graphics.getDeltaTime())
		velocity.add(facing.x * accel * delta, facing.y * accel * delta)
	}
	
	def thrust(amount: Float) = {
		delta = Math.min(0.06f, Gdx.graphics.getDeltaTime())
		velocity.add(facing.x * accel * delta, facing.y * accel * amount * delta)
	}

	def randomPointOnShip : Vector2 = {
		return new Vector2(collisionCenter.x + MathUtils.random(-this.getWidth() / 2, this.getWidth() / 2), collisionCenter.y + MathUtils.random(-this.getHeight() / 2, this.getHeight() / 2))
	}

    def goTowardsOrAway(targetPos: Vector2, forceThrust: Boolean, isAway: Boolean) {
		target_direction.set(targetPos).sub(collisionCenter)
		if (isAway) {
			target_direction.scl(-1)
		}

		if (facing.crs(target_direction) > 0) {
			turn(1)
		} else {
			turn(-1)
		}

		if (forceThrust || facing.dot(target_direction) > 0) {
			thrust
		}
	}

	def healthPercentage : Float = {
		return Math.max(hitPoints / maxHitPoints, 0)
	}

	def damage(amount: Float) = {
		hitPoints = Math.max(hitPoints - amount, 0).toInt
	}

	def destruct {
		if (this.isInstanceOf[FactoryProduction]) {
			factoryDestruct
		} else {
			GameInstance.explode(this)
			alive = false
            // TODO: make this more elegant
            val fs = GameInstance.factorys
			for (i <- 0 to fs.size) {
                val factory = fs.get(i)
                // TODO: this -- is going to cause problems ...
				if(factory.isInstanceOf[FactoryProduction] && factory.id == this.id) {
                    val n = factory.asInstanceOf[FactoryProduction].ownShips
                    factory.asInstanceOf[FactoryProduction].ownShips = n - 1
                }
			}
		}
	}

	def factoryDestruct {
		delta = Math.min(0.06f, Gdx.graphics.getDeltaTime())

		if (deathCounter > 0) {
			(this.asInstanceOf[FactoryProduction]).production.halt_production = true
			this.setColor(1, 1, 1, Math.min(1, opacity))
			opacity = opacity - 1 * delta
			if (Math.floor(deathCounter) % nextExplosion == 0) {
				GameInstance.explode(this, randomPointOnShip)
				nextExplosion = MathUtils.random(2, 6)
			}
			deathCounter = deathCounter - 10 * delta
		} else {
			for (i <- 1 to 10)  {
				GameInstance.explode(this, randomPointOnShip)
			}
			alive = false
		}
	}

	// automatically thrusts and turns according to the target
	def goTowards(targetPos: Vector2 , forceThrust: Boolean ) {
		goTowardsOrAway(targetPos, forceThrust, false)
	}

	def goAway(targetPos: Vector2, forceThrust: Boolean ) {
		goTowardsOrAway(targetPos, forceThrust, true)
	}

}

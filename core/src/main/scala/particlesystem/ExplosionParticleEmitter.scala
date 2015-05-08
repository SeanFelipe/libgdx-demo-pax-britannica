package srg.scala.paxbritannica.particlesystem

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2

import srg.scala.paxbritannica.GameInstance
import srg.scala.paxbritannica.Resources

class ExplosionParticleEmitter extends ParticleEmitter(0.5f, 1f) {

		delta_scale = 1.0f
		set(Resources.getInstance().explosion)

	def addParticle( position: Vector2 , scale: Float ) {
		super.addParticle(position, new Vector2(0,0), life, scale)
	}

	def addBigExplosion( position: Vector2 ) {
		delta_scale = 5
		addParticle(position, 0.5f)
		val random = new Vector2(MathUtils.cos((MathUtils.random() * MathUtils.PI * 2f).toFloat * Math.sqrt(MathUtils.random()).toFloat), (MathUtils.sin(MathUtils.random() * MathUtils.PI * 2f) * Math.sqrt(MathUtils.random()).toFloat))
		for (i <- 1 to 20) {
			val vel = new Vector2().set(random).add(random)
			val velp = new Vector2().set(vel).scl(i / 20F * 2F)
			val offset = new Vector2().set(random).scl(10)
			GameInstance.sparkParticles.addLaserExplosion(new Vector2(position.x + offset.x, position.y + offset.y), velp)
		}
		for (i <- 1 to 5) {
			val vel = new Vector2(MathUtils.random() * 2 - 1, MathUtils.random() * 2 - 1).scl(4)
			val offset = new Vector2().set(random).scl(3)
			GameInstance.sparkParticles.addLaserExplosion(new Vector2(position.x + offset.x, position.y + offset.y), vel)
		}
		for (i <- 1 to 50) {
			val offset = new Vector2().set(random).scl(17)
			GameInstance.bigBubbleParticles.addParticle(new Vector2(position.x + offset.x, position.y + offset.y))
		}
	}

	def addMediumExplosion(position: Vector2) {
		delta_scale = 3
		addParticle(position, 0.4f)
		val random = new Vector2(MathUtils.cos((MathUtils.random() * MathUtils.PI * 2f).toFloat * Math.sqrt(MathUtils.random()).toFloat), MathUtils.sin(MathUtils.random() * MathUtils.PI * 2f).toFloat * Math.sqrt(MathUtils.random()).toFloat);
		for (i <- 1 to 10) {
			val vel = new Vector2().set(random).add(random)
			val velp = new Vector2().set(vel).scl(i / 20F * 2F)
			val offset = new Vector2().set(random).scl(10)
			GameInstance.sparkParticles.addLaserExplosion(new Vector2(position.x + offset.x, position.y + offset.y), velp)
		}
		for (i <- 1 to 3) {
			val vel = new Vector2(MathUtils.random() * 2 - 1, MathUtils.random() * 2 - 1).scl(3)
			val offset = new Vector2().set(random).scl(3)
			GameInstance.sparkParticles.addLaserExplosion(new Vector2(position.x + offset.x, position.y + offset.y), vel)
		}
		for (i <- 1 to 20) {
			val offset = new Vector2().set(random).scl(2)
			GameInstance.bigBubbleParticles.addParticle(new Vector2(position.x + offset.x, position.y + offset.y))
		}
	}

	def addSmallExplosion(position: Vector2) {
		delta_scale = 2
		addParticle(position, 0.3f)
        val vx = MathUtils.cos((MathUtils.random() * MathUtils.PI * 2f) * Math.sqrt(MathUtils.random()).toFloat)
        val vy = MathUtils.sin((MathUtils.random() * MathUtils.PI * 2f) * Math.sqrt(MathUtils.random()).toFloat)
		val random = new Vector2(vx, vy) 
		for (i <- 1 to 2) {
			val vel = new Vector2().set(random).add(random)
			val velp = new Vector2().set(vel).scl(i / 20F * 2F)
			val offset = new Vector2().set(random).scl(10)
			GameInstance.sparkParticles.addLaserExplosion(new Vector2(position.x + offset.x, position.y + offset.y), velp)
		}
		for (i <- 1 to 2) {
			val vel = new Vector2(MathUtils.random() * 2 - 1, MathUtils.random() * 2 - 1).scl(2)
			val offset = new Vector2().set(random).scl(3)
			GameInstance.sparkParticles.addLaserExplosion(new Vector2(position.x + offset.x, position.y + offset.y), vel)
		}
		for (i <- 1 to 10) {
			val offset = new Vector2().set(random).scl(2)
			GameInstance.bigBubbleParticles.addParticle(new Vector2(position.x + offset.x, position.y + offset.y))
		}
	}

	def addTinyExplosion(position: Vector2) {
		delta_scale = 1
		addParticle(position, 0.1f)
		val random = new Vector2((MathUtils.cos(MathUtils.random() * MathUtils.PI * 2f) * Math.sqrt(MathUtils.random())).toFloat, (MathUtils.sin(MathUtils.random() * MathUtils.PI * 2f).toFloat * Math.sqrt(MathUtils.random()).toFloat))
		for (i <- 1 to 1) {
			val vel = new Vector2().set(random).add(random)
			val velp = new Vector2().set(vel).scl(i / 20F * 2F)
			val offset = new Vector2().set(random).scl(10)
			GameInstance.sparkParticles.addLaserExplosion(new Vector2(position.x + offset.x, position.y + offset.y), velp)
		}
		for (i <- 1 to 1) {
			val vel = new Vector2(MathUtils.random() * 2 - 1, MathUtils.random() * 2 - 1)
			val offset = new Vector2().set(random).scl(3)
			GameInstance.sparkParticles.addLaserExplosion(new Vector2(position.x + offset.x, position.y + offset.y), vel)
		}
		for (i <- 1 to 5) {
			val offset = new Vector2().set(random).scl(17)
			GameInstance.bigBubbleParticles.addParticle(new Vector2(position.x + offset.x, position.y + offset.y))
		}
	}
}

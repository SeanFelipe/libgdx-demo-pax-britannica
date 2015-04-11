package srg.scala.paxbritannica.particlesystem

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2

import srg.scala.paxbritannica.Resources

class SparkParticleEmitter extends ParticleEmitter (1f, 5.95f) {

	var particleVelocity = new Vector2()
    set(Resources.getInstance().spark)

	def addLaserExplosion( position: Vector2, velocity: Vector2 ) {
		for ( i <- 1 to 10 ) {
            val x = MathUtils.cos((MathUtils.random() * MathUtils.PI * 2f).toFloat * Math.sqrt(MathUtils.random()).toFloat)
            val y = MathUtils.sin((MathUtils.random() * MathUtils.PI * 2f).toFloat * Math.sqrt(MathUtils.random()).toFloat)
            random.set(x, y)
			particleVelocity.set(-velocity.x + random.x, -velocity.y + random.y)
			addParticle(position, particleVelocity, 1f, 1)
		}
	}

}

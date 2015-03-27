package srg.scala.paxbritannica.particlesystem

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{ Batch, Sprite }
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.{ Array, Pool }

class ParticleEmitter extends Sprite {
	
	val maxParticle = 500
	var random : Vector2 = _
	
	val life : Float = 1
	val damping : Float = 1
	var delta_scale : Float = _
	var delta : Float = _

	var particles = new Array[Particle](false,maxParticle)
  
	var freeParticles = new Pool[Particle](maxParticle,maxParticle) {
		def newObject() : Particle = {
			return new Particle()
		}
  }

	override def draw( batch : Batch ) {
		delta = Math.min(0.06f, Gdx.graphics.getDeltaTime())
		
		this.setOrigin(0,0)
		for ( i <- particles.size to 0 by -1 ) {
			var particle = particles.get(i)
			if (particle.life > 0) {
				updateParticle(particle)
				val dx = this.getWidth() / 2 * particle.scale //Float
				val dy = this.getHeight() / 2 * particle.scale //Float
				this.setColor(1, 1, 1, Math.max(particle.life / this.life,0))	//r-g-b-a
				this.setScale(particle.scale)
				this.setPosition(particle.position.x -dx, particle.position.y -dy)
				if(!(particle.position.y -dy>=-10 && particle.position.y -dy<=10) && !(particle.position.x -dx>=-10 && particle.position.x -dx<=10)) {
					super.draw(batch)
				} else {
					particle.life = 0
				}
			} else {
				particles.removeIndex(i)
				freeParticles.free(particle)
			}
		}
		
	}

	def updateParticle( particle : Particle ) {
		delta = Math.min(0.06f, Gdx.graphics.getDeltaTime())
		
		if ( particle.life > 0 ) {
			particle.life -= delta
			particle.position.add(particle.velocity.x * delta*10,particle.velocity.y * delta*10)
			particle.velocity.scl(Math.pow(damping, delta).toFloat)
			particle.scale += this.delta_scale * delta/5f
		}
	}
	
	def addParticle(position : Vector2 , velocity : Vector2 , life : Float, scale : Float) {
	     if(particles.size>maxParticle) return
	     if(Gdx.graphics.getFramesPerSecond()<25 && !(this instanceof ExplosionParticleEmitter)) return
		 Particle particle = freeParticles.obtain()
	     particle.setup(position,velocity,life,scale)
	     particles.add(particle)
	}
	
	public void dispose() {
		particles.clear()
		freeParticles.clear()
	}

}

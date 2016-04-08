package srg.scala.paxbritannica.particlesystem

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2

import srg.scala.paxbritannica.Resources

class BigBubbleParticleEmitter extends ParticleEmitter (2.5f, 1f) {

    set(Resources.bigbubble)

    def addParticle( position: Vector2 ) {
        val velocity = new Vector2(MathUtils.random() * 0.1f - 0.05f, 0.01f + MathUtils.random() * 0.05f)
        addParticle( position, velocity, life, 1 )
    }   

}

package srg.scala.paxbritannica.particlesystem

import com.badlogic.gdx.math.Vector2

class Particle {
    var life : Float = _
    var position : Vector2 = _
    var velocity : Vector2 = _
    var scale : Float = _
    
    def setup( position : Vector2, velocity : Vector2 , life : Float , scale : Float ) {
        this.position.set(position)
        this.velocity.set(velocity)
        this.life = life
        this.scale = scale
    }

}

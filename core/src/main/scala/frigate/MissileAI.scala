package srg.scala.paxbritannica.frigate

import com.badlogic.gdx.math.Vector2

import srg.scala.paxbritannica.{ GameInstance, Ship, Targeting }

class MissileAI (mmissile: Missile ) {
    val MAX_LIFETIME = 5 // 5 seconds to auto-destruct
    var target: Ship = _
    val missile = mmissile
    
    val relativeVel = new Vector2()
    val toTarget = new Vector2()

    retarget()

    def retarget() {
        val t = Targeting.getTypeInRange(missile, 0, 500)
        target = t.getOrElse(null) 
        if (target == null) {
            val t = Targeting.getTypeInRange(missile, 1, 500)
            target = t.getOrElse(null) 
        } else
            return
        if (target == null) {
            val t = Targeting.getTypeInRange(missile, 2, 500)
            target = t.getOrElse(null) 
        } else
            return
        if (target == null) {
            val t = Targeting.getTypeInRange(missile, 1, 500)
            target = t.getOrElse(null) 
        } else
            return
        if (target == null) {
            val t = Targeting.getTypeInRange(missile, 3, 500)
            target = t.getOrElse(null) 
        } else
            target = null
    }

    def selfDestruct() {
        // EXPLODE!
        missile.alive = false
        GameInstance.explosionParticles.addTinyExplosion(missile.collisionCenter)
    }

    def predict() : Vector2 = {
        relativeVel.set(missile.velocity).sub(target.velocity)
        toTarget.set(target.collisionCenter).sub(missile.collisionCenter)
        if (missile.velocity.dot(toTarget) != 0) {
            val time_to_target = toTarget.dot(toTarget) / relativeVel.dot(toTarget)
            return new Vector2(target.collisionCenter).sub(relativeVel.scl(Math.max(0, time_to_target)))
        } else {
            return target.collisionCenter
        }
    }

    def update() {
        if (target == null || missile.aliveTime > MAX_LIFETIME) {
            selfDestruct()
        } else if (!target.alive) {
            retarget()
        } else {
            missile.goTowards(predict(), true)
        }
    }
}

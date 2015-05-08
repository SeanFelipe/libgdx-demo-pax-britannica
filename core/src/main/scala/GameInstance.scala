package srg.scala.paxbritannica

import com.badlogic.gdx.{ Gdx, Preferences }
import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array

import srg.scala.paxbritannica.bomber.{ Bomb, Bomber }
import srg.scala.paxbritannica.factory.FactoryProduction
import srg.scala.paxbritannica.fighter.{ Fighter, Laser }
import srg.scala.paxbritannica.frigate.{ Frigate, Missile }
import srg.scala.paxbritannica.particlesystem.{ BigBubbleParticleEmitter,
  ExplosionParticleEmitter, BubbleParticleEmitter, SparkParticleEmitter }

object GameInstance {
	
	var debugMode = false

	var fighters, factorys, bombers, frigates = new Array[Ship]
	var bullets = new Array[Bullet]

	var bubbleParticles = new BubbleParticleEmitter()
	var bigBubbleParticles = new BigBubbleParticleEmitter()
	var sparkParticles = new SparkParticleEmitter()
	var explosionParticles = new ExplosionParticleEmitter()

	var difficultyConfig = 0
	var factoryHealthConfig = 0
	var antiAliasConfig = 0	

	def resetGame {
		fighters.clear
		factorys.clear
		bombers.clear
		frigates.clear
		bullets.clear
		
		bubbleParticles.dispose
		bigBubbleParticles.dispose
		sparkParticles.dispose
		explosionParticles.dispose

		bubbleParticles = new BubbleParticleEmitter()
		bigBubbleParticles = new BigBubbleParticleEmitter()
		sparkParticles = new SparkParticleEmitter()
		explosionParticles = new ExplosionParticleEmitter()
		
		val prefs = Gdx.app.getPreferences("paxbritannica")
		difficultyConfig = prefs.getInteger("difficulty",0)
		factoryHealthConfig = prefs.getInteger("factoryHealth",0)
		antiAliasConfig = prefs.getInteger("antiAliasConfig",1)
	}

	def bulletHit( ship: Ship, bullet: Bullet ) {
		bullet.facing.nor()
		var offset = 0f
		if(ship.isInstanceOf[FactoryProduction]) offset = 50f
		if(ship.isInstanceOf[Frigate]) offset = 20f
		if(ship.isInstanceOf[Bomber]) offset = 10f
		if(ship.isInstanceOf[Fighter]) offset = 10f
		val pos = new Vector2().set(
            bullet.collisionCenter.x + (offset * bullet.facing.x), 
            bullet.collisionCenter.y + (offset * bullet.facing.y)
        );

		// ugh. . .
		val bullet_vel = new Vector2().set(bullet.velocity)
		var bullet_dir: Vector2 = new Vector2()
		if (bullet_vel.dot(bullet_vel) != 0) {
			bullet_dir = bullet_vel.nor()
		}

		val vel = new Vector2(bullet_dir.x * 1.5f, bullet_dir.y * 1.5f)
		if (bullet.isInstanceOf[Laser]) {
			laser_hit(pos, vel)
		} else if (bullet.isInstanceOf[Bomb]) {
			explosionParticles.addMediumExplosion(bullet.position)
		} else if (bullet.isInstanceOf[Missile]) {
			explosionParticles.addTinyExplosion(bullet.position)
		}
	}

	def laser_hit( pos: Vector2 , vel: Vector2 ) {
		sparkParticles.addLaserExplosion(pos, vel)
	}

	def explode( ship: Ship ) {
		explode(ship, ship.collisionCenter)
	}

	def explode(ship: Ship , pos: Vector2) {
		if (ship.isInstanceOf[FactoryProduction]) {
			explosionParticles.addBigExplosion(pos)
		} else if (ship.isInstanceOf[Bomber]) {
			explosionParticles.addMediumExplosion(pos)
		} else if (ship.isInstanceOf[Frigate]) {
			explosionParticles.addMediumExplosion(pos)
		}else {
			explosionParticles.addSmallExplosion(pos)
		}
	}
}

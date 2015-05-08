package srg.scala.paxbritannica.factory

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2

import srg.scala.paxbritannica.Constants
import srg.scala.paxbritannica.GameInstance
import srg.scala.paxbritannica.Resources
import srg.scala.paxbritannica.Ship

class FactoryProduction (iid: Int , pposition: Vector2 , ffacing: Vector2 ) 
  extends Ship(iid, pposition, ffacing) {

    velocity.set(facing.x, facing.y)
    override val turnSpeed = 2.0f
    override val accel = 5.0f

	var harvestRate = 40f
	var harvestRateUpgrade = 15f
	var upgradesUsed = 0f
	var resourceAmount = 20f
	
	var ownShips = 0
	var button_held = false
	var drawDamage = false

    if(GameInstance.factoryHealthConfig == 0) {
        hitPoints = 25000
		} else if(GameInstance.factoryHealthConfig == 1) {
		    hitPoints = 45000
		} else {
		    hitPoints = 65000
		}
    maxHitPoints = hitPoints

	val production = new Production(this)
    val	facing90 = new Vector2()

    id match  {
        case 1 => this.set(Resources.getInstance().factoryP1)
        case 2 => this.set(Resources.getInstance().factoryP2)
        case 3 => this.set(Resources.getInstance().factoryP3)
        case _ => this.set(Resources.getInstance().factoryP4)
    }

	val light_damage1 = new Sprite()
    val light_damage2 = new Sprite()
    val light_damage3 = new Sprite()
    val heavy_damage1 = new Sprite()
    val heavy_damage2 = new Sprite()
    val heavy_damage3 = new Sprite()
    light_damage1.set(Resources.getInstance().factoryLightDamage1)
    light_damage2.set(Resources.getInstance().factoryLightDamage2)
    light_damage3.set(Resources.getInstance().factoryLightDamage3)
    heavy_damage1.set(Resources.getInstance().factoryHeavyDamage1)
    heavy_damage2.set(Resources.getInstance().factoryHeavyDamage2)
    heavy_damage3.set(Resources.getInstance().factoryHeavyDamage3)
    var current_damage = light_damage1

    this.setOrigin(this.getWidth() / 2, this.getHeight() / 2)
		
	override def draw(batch: Batch ) {
		delta = Math.min(0.06f, Gdx.graphics.getDeltaTime())
		
		resourceAmount = Math.min(2000, resourceAmount + (harvestRate * delta))
		super.draw(batch)
		production.draw(batch)

		// Damage
		// ugh. . . sprite needs to be more flexible
		drawDamage = false
		val health = healthPercentage
		val animation = (Math.floor(aliveTime * 20) % 3 + 1).toInt

		if (health < Constants.lowHealthThreshold) {
			animation match {
                case 1 => current_damage = heavy_damage1
                case 2 => current_damage = heavy_damage2
                case _ => current_damage = heavy_damage3
			}
			drawDamage = true
		} else if (health < Constants.highHealthThreshold) {
			animation match {
                case 1 => current_damage = light_damage1
                case 2 => current_damage = light_damage2
                case _ => current_damage = light_damage3
			}
			drawDamage = true
		}
		
		if(drawDamage) {
			facing90.set(facing)
			facing90.rotate(90).nor()

			current_damage.setOrigin(0, 0)
			current_damage.setPosition(collisionCenter.x - (90 * facing.x) - (60 * facing90.x), collisionCenter.y - (90 * facing.y)
					- (60 * facing90.y))
			current_damage.setRotation(facing.angle())
			current_damage.setColor(1, 1, 1, MathUtils.random())
			current_damage.draw(batch)
		}
	}

}

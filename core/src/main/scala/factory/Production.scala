package srg.scala.paxbritannica.factory

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2

import srg.scala.paxbritannica.Constants
import srg.scala.paxbritannica.GameInstance
import srg.scala.paxbritannica.Resources
import srg.scala.paxbritannica.bomber.Bomber
import srg.scala.paxbritannica.fighter.Fighter
import srg.scala.paxbritannica.frigate.Frigate

class Production (ffactory: FactoryProduction ) {

    // TODO: probably messed up an else clause in scale_angle or one of those spots

	val BUILDING_SPEED = 100
	val MAXSHIPS = 100
	val SEGMENTS = 32f
	val RADIUS = 32f
	val DRAW_OFFSET = -4f
	val SPAWN_OFFSET = 47f

	var fighterCost = 50f
	var bomberCost = 170f
	var frigateCost = 360f
	var upgradeCost = 1080f
	var potential_cost = 0f
	var needle_angle = 0f
	var needle_velocity = 0f

	val facing90 = new Vector2()
	var halt_production = false
	var currentBuildingUnit = -1
	var fade = 0f

	val factory = ffactory

    val needle = Resources.needle
    needle.setOrigin(needle.getHeight() / 2, needle.getWidth() / 2)

    val production1 = Resources.production1
    production1.setOrigin(production1.getHeight() / 2, production1.getWidth() / 2)
    val production2 = Resources.production2
    production2.setOrigin(production2.getHeight() / 2, production2.getWidth() / 2)
    val production3 = Resources.production3
    production3.setOrigin(production3.getHeight() / 2, production3.getWidth() / 2)

    val production_tile1 = Resources.production_tile1
    production_tile1.setOrigin(production_tile1.getHeight() / 2, production_tile1.getWidth() / 2)
    val production_tile2 = Resources.production_tile2
    production_tile2.setOrigin(production_tile2.getHeight() / 2, production_tile2.getWidth() / 2)
    val production_tile3 = Resources.production_tile3
    production_tile3.setOrigin(production_tile3.getHeight() / 2, production_tile3.getWidth() / 2)
    val production_tile4 = Resources.production_tile4
    production_tile4.setOrigin(production_tile4.getHeight() / 2, production_tile4.getWidth() / 2)

    val upgrade_outline = Resources.upgradeOutline
    upgrade_outline.setOrigin(upgrade_outline.getHeight() / 2, upgrade_outline.getWidth() / 2)
    val frigate_outline = Resources.frigateOutline
    frigate_outline.setOrigin(frigate_outline.getHeight() / 2, frigate_outline.getWidth() / 2)
    val bomber_outline = Resources.bomberOutline
    bomber_outline.setOrigin(bomber_outline.getHeight() / 2, bomber_outline.getWidth() / 2)
    val fighter_outline = Resources.fighterOutline
    fighter_outline.setOrigin(fighter_outline.getHeight() / 2, fighter_outline.getWidth() / 2)

    val health_none = Resources.healthNone
    health_none.setOrigin(health_none.getHeight() / 2, health_none.getWidth() / 2)
    val health_some = Resources.healthSome
    health_some.setOrigin(health_some.getHeight() / 2, health_some.getWidth() / 2)
    val health_full = Resources.healthFull
    health_full.setOrigin(health_full.getHeight() / 2, health_full.getWidth() / 2)

    /**
     * Span new Ship
     * 
     * 1 = Fighter
     * 2 = Bomber
     * 3 = Frigate
     * 4 = Upgrade
     * @param unitType
     */
    def spawn(unitType: Int ) {

        val spawn_pos = new Vector2(factory.collisionCenter.x + (SPAWN_OFFSET * factory.facing.x),
            factory.collisionCenter.y + (SPAWN_OFFSET * factory.facing.y));

        if (unitType == 1) {
            factory.resourceAmount -= fighterCost
            GameInstance.fighters.add(
                new Fighter(factory.id, spawn_pos, new Vector2(factory.facing.x, factory.facing.y))
            );
        } else if (unitType == 2) {
            spawn_pos.sub(10, 10)
            factory.resourceAmount -= bomberCost
            GameInstance.bombers.add(
                new Bomber(factory.id, spawn_pos, new Vector2(factory.facing.x, factory.facing.y))
            );
        } else if (unitType == 3) {
            spawn_pos.sub(25, 25)
            factory.resourceAmount -= frigateCost
            GameInstance.frigates.add(
                new Frigate(factory.id, spawn_pos, new Vector2(factory.facing.x, factory.facing.y))
            );
        } else if (unitType == 4) {
            factory.resourceAmount -= upgradeCost
            factory.upgradesUsed += 1
            factory.harvestRate += (factory.harvestRateUpgrade/factory.upgradesUsed)
        }
    }

    def update() {
        if (factory.button_held) {
            if (potential_cost == 0 && factory.resourceAmount >= potential_cost) {
                potential_cost = fighterCost
        }

        if (factory.resourceAmount > potential_cost + (BUILDING_SPEED * Gdx.graphics.getDeltaTime()) - 1) {
            potential_cost += BUILDING_SPEED * Gdx.graphics.getDeltaTime()
        } else {
            if (potential_cost > upgradeCost) {
                spawn(4)
            } else if (potential_cost > frigateCost) {
                spawn(3)
            } else if (potential_cost > bomberCost) {
                spawn(2)
            } else if (potential_cost > fighterCost) {
                spawn(1)
            }
            potential_cost = 0
            }
        }
    }

    def scale_angle(frames: Float) : Float = {
        var angle = 0f
        if (frames < fighterCost) {
            angle = 0.25f
        } else if (frames < bomberCost) {
            angle = (frames - fighterCost) / (bomberCost - fighterCost) * 0.25f + 0.25f
        } else if (frames < frigateCost) {
            angle = (frames - bomberCost) / (frigateCost - bomberCost) * 0.25f + 0.5f
        } else {
            angle = (frames - frigateCost) / (upgradeCost - frigateCost) * 0.25f + 0.75f
        }
        return Math.min(angle - 0.25f, 1)
        }

    def get_resources_spent(frames: Float) : Float = {
        var spent = 0f
        if (frames < fighterCost) {
            spent = 0
        } else if (frames < bomberCost) {
            spent = fighterCost
        } else if (frames < frigateCost) {
            spent = bomberCost
        } else if (frames < upgradeCost) {
            spent = frigateCost
        } else {
            spent = upgradeCost
        }
        return spent
        }

    def draw(batch: Batch) {
        update()

        facing90.set(factory.facing)
        facing90.rotate(90).nor()

        var angle = Math.min(1, Math.max(0, norm(scale_angle(factory.resourceAmount),0,0.25f)))
        fade = Math.min(MathUtils.PI, fade+(Gdx.graphics.getDeltaTime()))
        if(fade == MathUtils.PI) fade =0


        production_tile1.setOrigin(0, 0)
        production_tile1.setColor(angle, angle,angle, angle)

        production_tile1.setRotation(factory.facing.angle())
        production_tile1.setPosition(factory.collisionCenter.x - (35 * factory.facing.x) - (32* facing90.x), 
            factory.collisionCenter.y	- (35 * factory.facing.y) - (32 * facing90.y));
        production_tile1.draw(batch)

        angle = Math.min(1, Math.max(0, norm(scale_angle(factory.resourceAmount),0.25f,0.5f)))
        production_tile2.setOrigin(0, 0)

        production_tile2.setColor(angle, angle,angle, angle)

        production_tile2.setRotation(factory.facing.angle())
        production_tile2.setPosition(factory.collisionCenter.x - (35 * factory.facing.x) - (32 * facing90.x), 
            factory.collisionCenter.y	- (35 * factory.facing.y) - (32 * facing90.y));
        production_tile2.draw(batch)

        angle = Math.min(1, Math.max(0, norm(scale_angle(factory.resourceAmount),0.5f,0.75f)))
        production_tile3.setOrigin(0, 0)

        production_tile3.setColor(angle, angle,angle, angle)

        production_tile3.setRotation(factory.facing.angle())
        production_tile3.setPosition(factory.collisionCenter.x - (35* factory.facing.x)- (32 * facing90.x), 
            factory.collisionCenter.y	- (35 * factory.facing.y) - (32 * facing90.y));
        production_tile3.draw(batch)

        angle = Math.min(1, Math.max(0, norm(scale_angle(factory.resourceAmount),0.75f,1f)))
        production_tile4.setOrigin(0, 0)

        production_tile4.setColor(angle, angle,angle, angle)

        production_tile4.setRotation(factory.facing.angle())
        production_tile4.setPosition(factory.collisionCenter.x - (35 * factory.facing.x)- (32 * facing90.x), 
            factory.collisionCenter.y	- (35* factory.facing.y) - (32 * facing90.y));
        production_tile4.draw(batch)

        // Draw the needle
        angle = scale_angle(potential_cost)
        if (angle == 0) {
            if (needle_angle > 0 || needle_velocity < 0) {
                needle_velocity = Math.min(needle_velocity + 0.2f * Gdx.graphics.getDeltaTime(), 0.025f)
                needle_angle = Math.max(needle_angle - needle_velocity, 0)
                if (needle_angle == 0) {
                    needle_velocity = needle_velocity * Math.pow(-0.475f, Gdx.graphics.getDeltaTime()).toFloat
                }
            }
        } else {
            needle_velocity = 0
            needle_angle = angle
        }

        needle.setOrigin(0, 0)
        needle.setPosition(factory.collisionCenter.x - (2 * factory.facing.x) - (-2 * facing90.x), 
            factory.collisionCenter.y	- (2 * factory.facing.y) - (-2 * facing90.y));
        needle.setRotation(factory.facing.angle() + ((-needle_angle) * 360) - 90)
        needle.draw(batch)

        production2.setOrigin(0, 0)
        production2.setRotation(factory.facing.angle())
        production2.setPosition(factory.collisionCenter.x - (35 * factory.facing.x) - (32 * facing90.x), 
            factory.collisionCenter.y	- (35 * factory.facing.y) - (32 * facing90.y));
        production2.draw(batch)

        production3.setOrigin(0, 0)
        production3.setRotation(factory.facing.angle())
        production3.setPosition(factory.collisionCenter.x - (35 * factory.facing.x) - (32 * facing90.x), 
            factory.collisionCenter.y	- (35 * factory.facing.y) - (32 * facing90.y));
        production3.draw(batch)


        // Draw the preview outline
        if (factory.button_held) {
            if (potential_cost > upgradeCost) {
                upgrade_outline.setOrigin(0, 0)
                upgrade_outline.setRotation(factory.facing.angle())
                upgrade_outline.setPosition(
                    factory.collisionCenter.x - (35 * factory.facing.x) - (32 * facing90.x), 
                    factory.collisionCenter.y - (35 * factory.facing.y) - (32 * facing90.y));
                upgrade_outline.draw(batch)
                currentBuildingUnit = 3
            } else if (potential_cost > frigateCost) {
                frigate_outline.setOrigin(0, 0)
                frigate_outline.setRotation(factory.facing.angle())
                frigate_outline.setPosition(
                    factory.collisionCenter.x - (35 * factory.facing.x) - (32* facing90.x), 
                    factory.collisionCenter.y - (35 * factory.facing.y) - (32 * facing90.y));
                frigate_outline.draw(batch)
                currentBuildingUnit = 2
            } else if (potential_cost > bomberCost) {
                bomber_outline.setOrigin(0, 0)
                bomber_outline.setRotation(factory.facing.angle())
                bomber_outline.setPosition(
                    factory.collisionCenter.x - (35 * factory.facing.x) - (32 * facing90.x), 
                    factory.collisionCenter.y - (35 * factory.facing.y) - (32 * facing90.y));
                bomber_outline.draw(batch)
                currentBuildingUnit = 1
            } else if (potential_cost > fighterCost) {
                fighter_outline.setOrigin(0, 0)
                fighter_outline.setRotation(factory.facing.angle())
                fighter_outline.setPosition(
                    factory.collisionCenter.x - (35 * factory.facing.x) - (32 * facing90.x), 
                    factory.collisionCenter.y - (35 * factory.facing.y) - (32 * facing90.y));
                fighter_outline.draw(batch)
                currentBuildingUnit = 0
            } else {
                currentBuildingUnit = -1
            }
            } else {
                currentBuildingUnit = -1

                val health = factory.healthPercentage()
                if (health < Constants.lowHealthThreshold) {
                    val factor = health / Constants.lowHealthThreshold
                    health_none.setOrigin(0, 0)
                    health_none.setRotation(factory.facing.angle())
                    health_none.setColor(1, factor * 0.3f, factor * 0.3f, 1)
                    health_none.setPosition(
                        factory.collisionCenter.x - (35 * factory.facing.x) - (32 * facing90.x), 
                        factory.collisionCenter.y - (35 * factory.facing.y) - (32 * facing90.y));
                    health_none.draw(batch)
                } else if (health < Constants.highHealthThreshold) {
                    val factor = (health - Constants.lowHealthThreshold) / (Constants.highHealthThreshold - Constants.lowHealthThreshold)
                    health_some.setOrigin(0, 0)
                    health_some.setRotation(factory.facing.angle())
                    health_some.setColor(1, factor * 0.7f + 0.3f, factor * 0.2f + 0.3f, 1)
                    health_some.setPosition(
                        factory.collisionCenter.x - (35 * factory.facing.x) - (32 * facing90.x), 
                        factory.collisionCenter.y - (35 * factory.facing.y) - (32*facing90.y));
                    health_some.draw(batch)
                } else {
                    val factor = (health - Constants.highHealthThreshold) / (1 - Constants.highHealthThreshold)
                    health_full.setOrigin(0, 0)
                    health_full.setRotation(factory.facing.angle())
                    health_full.setColor((1 - factor) * 0.3f + 0.7f, 1, factor * 0.4f + 0.6f, 1)
                    health_full.setPosition(
                        factory.collisionCenter.x - (35 * factory.facing.x) - (32 * facing90.x), 
                        factory.collisionCenter.y - (35 * factory.facing.y) - (32 * facing90.y));
                    health_full.draw(batch)
                }
            }
        }

        /**
         * Normalize a value to exist between 0 and 1 (inclusive).
         * Mathematically the opposite of lerp(), figures out what proportion
         * a particular value is relative to start and stop coordinates.
         */
        def norm(value: Float , start: Float , stop: Float ) : Float = {
            return (value - start) / (stop - start)
    }
}

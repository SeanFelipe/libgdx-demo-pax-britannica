package srg.scala.paxbritannica.mainmenu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.math.Vector2
import srg.scala.paxbritannica.Resources

class Countdown (start_position: Vector2) extends Sprite {

    var finished = false
    var showed = true
    var fade = 0.0f
    var cnt = 5
    var delta: Float = _
    var position = start_position

    changeTexture(5)
    
    def reset() {
        changeTexture(5)
        finished = false
        showed = true
        fade = 0.0f
        cnt = 5
        this.setRotation(0)
        this.setScale(1f)
        this.setPosition(position.x, position.y)
        this.setColor(0, 0, 0, 0)
    }

    def changeTexture(id: Int) {

        id match {
            case 5 => this.set(Resources.cnt5)
            case 2 => this.set(Resources.cnt2)
            case 3 => this.set(Resources.cnt3)
            case 4 => this.set(Resources.cnt4)
            case _ => this.set(Resources.cnt1)
        }
        this.setRotation(0)
        this.setScale(1f)
        this.setPosition(position.x, position.y)
        this.setColor(0, 0, 0, 1)
    }

    override def draw(batch: Batch) {
        delta = Math.min(0.06f, Gdx.graphics.getDeltaTime())
        super.draw(batch)

        if (cnt < 1) {
            finished = true
            this.setColor(1, 1, 1, 0)
            return
        }

        if (showed) {
            fade = Math.min(fade + delta * 2f, 1)
        } else {
            fade = Math.max(fade - delta * 2f, 0)
        }
        this.setColor(1, 1, 1, fade)

        if (fade == 1) {
            showed = !showed
        }
        if (fade == 0) {
            showed = !showed
            cnt = cnt - 1
            changeTexture(cnt)
        }
    }
}

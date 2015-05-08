package srg.scala.paxbritannica

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas

object Resources {

	var title = new Sprite(new Texture(Gdx.files.internal("data/spritepack/title.png")))
	var credits = new Sprite(new Texture(Gdx.files.internal("data/spritepack/credits.png")))
	var atlas = new TextureAtlas(Gdx.files.internal("data/spritepack/packhigh.pack"))
	var music = Gdx.audio.newMusic(Gdx.files.internal("data/audio/music.mp3"))

	var factoryP1 = atlas.createSprite("factoryp1")
	var factoryP2 = atlas.createSprite("factoryp2")
	var factoryP3 = atlas.createSprite("factoryp3")
	var factoryP4 = atlas.createSprite("factoryp4")
	
	var factoryP1Small = atlas.createSprite("factoryp1")
	var factoryP2Small = atlas.createSprite("factoryp2")
	var factoryP3Small = atlas.createSprite("factoryp3")
	var factoryP4Small = atlas.createSprite("factoryp4")

	var fighterP1 = atlas.createSprite("fighterp1")
	var fighterP2 = atlas.createSprite("fighterp2")
	var fighterP3 = atlas.createSprite("fighterp3")
	var fighterP4 = atlas.createSprite("fighterp4")

	var bomberP1 = atlas.createSprite("bomberp1")
	var bomberP2 = atlas.createSprite("bomberp2")
	var bomberP3 = atlas.createSprite("bomberp3")
	var bomberP4 = atlas.createSprite("bomberp4")

	var frigateP1 = atlas.createSprite("frigatep1")
	var frigateP2 = atlas.createSprite("frigatep2")
	var frigateP3 = atlas.createSprite("frigatep3")
	var frigateP4 = atlas.createSprite("frigatep4")

	var debrisSmall = atlas.createSprite("debrissmall")
	var debrisMed = atlas.createSprite("debrismed")
	var debrisLarge = atlas.createSprite("debrislarge")

	var fish1 = atlas.createSprite("fish1")
	var fish2 = atlas.createSprite("fish2")
	var fish3 = atlas.createSprite("fish3")
	var fish4 = atlas.createSprite("fish4")
	var fish5 = atlas.createSprite("fish5")
	var fish6 = atlas.createSprite("fish6")
	var fish7 = atlas.createSprite("fish7")
	var fish8 = atlas.createSprite("fish8")

	var needle = atlas.createSprite("needle")

	var background = atlas.createSprite("background")

	var blackFade = atlas.createSprite("blackfade")

	var laser = atlas.createSprite("laser")
	var missile = atlas.createSprite("missile")
	var bomb = atlas.createSprite("bomb")

	var production1 = atlas.createSprite("production1")
	var production2 = atlas.createSprite("production2")
	var production3 = atlas.createSprite("production3")

	var production_tile1 = atlas.createSprite("productiontile")
	var production_tile2 = atlas.createSprite("productiontile")
	var production_tile3 = atlas.createSprite("productiontile")
	var production_tile4 = atlas.createSprite("productiontile")

	var upgradeOutline = atlas.createSprite("upgradeoutline")
	var frigateOutline = atlas.createSprite("frigateoutline")
	var bomberOutline = atlas.createSprite("bomberoutline")
	var fighterOutline = atlas.createSprite("fighteroutline")

	var healthNone = atlas.createSprite("healthnone")
	var healthSome = atlas.createSprite("healthsome")
	var healthFull = atlas.createSprite("healthfull")

	var aButton = atlas.createSprite("abutton")
	var aCpuButton = atlas.createSprite("acpubutton")
	var aPlayerButton = atlas.createSprite("aplayerbutton")
	
	var cpuButton = atlas.createSprite("cpubutton")
	var playerButton = atlas.createSprite("playerbutton")

	var cnt1 = atlas.createSprite("1")
	var cnt2 = atlas.createSprite("2")
	var cnt3 = atlas.createSprite("3")
	var cnt4 = atlas.createSprite("4")
	var cnt5 = atlas.createSprite("5")

	var spark = atlas.createSprite("spark")
	var bubble = atlas.createSprite("bubble")
	var bigbubble = atlas.createSprite("bigbubble")
	var explosion = atlas.createSprite("explosion")

	var factoryHeavyDamage1 = atlas.createSprite("factoryheavydamage1")
	var factoryHeavyDamage2 = atlas.createSprite("factoryheavydamage2")
	var factoryHeavyDamage3 = atlas.createSprite("factoryheavydamage3")
	var factoryLightDamage1 = atlas.createSprite("factorylightdamage1")
	var factoryLightDamage2 = atlas.createSprite("factorylightdamage2")
	var factoryLightDamage3 = atlas.createSprite("factorylightdamage3")

	var touchArea1 = atlas.createSprite("touchArea")
	var touchArea2 = atlas.createSprite("touchArea")
	var touchArea3 = atlas.createSprite("touchArea")
	var touchArea4 = atlas.createSprite("touchArea")
	
	var help = atlas.createSprite("help")
	var musicOnOff = atlas.createSprite("music")
	var back = atlas.createSprite("back")
	var settings = atlas.createSprite("settings")
	var checkboxOn = atlas.createSprite("checkboxon")
	var checkboxOff = atlas.createSprite("checkboxoff")


	def reInit() {
		dispose()
		
		val prefs = Gdx.app.getPreferences("paxbritannica")
		if (prefs.getInteger("antiAliasConfig", 1) == 0) {
			atlas = new TextureAtlas(Gdx.files.internal("data/spritepack/pack.pack"))
		} else {
			atlas = new TextureAtlas(Gdx.files.internal("data/spritepack/packhigh.pack"))
		}

        if (music != null) {
            music.stop()
            music.dispose()
        } 
        music = Gdx.audio.newMusic(Gdx.files.internal("data/audio/music.mp3"))

		factoryP1 = atlas.createSprite("factoryp1")
		factoryP2 = atlas.createSprite("factoryp2")
		factoryP3 = atlas.createSprite("factoryp3")
		factoryP4 = atlas.createSprite("factoryp4")
		
		factoryP1Small = atlas.createSprite("factoryp1")
		factoryP2Small = atlas.createSprite("factoryp2")
		factoryP3Small = atlas.createSprite("factoryp3")
		factoryP4Small = atlas.createSprite("factoryp4")

		fighterP1 = atlas.createSprite("fighterp1")
		fighterP2 = atlas.createSprite("fighterp2")
		fighterP3 = atlas.createSprite("fighterp3")
		fighterP4 = atlas.createSprite("fighterp4")

		bomberP1 = atlas.createSprite("bomberp1")
		bomberP2 = atlas.createSprite("bomberp2")
		bomberP3 = atlas.createSprite("bomberp3")
		bomberP4 = atlas.createSprite("bomberp4")

		frigateP1 = atlas.createSprite("frigatep1")
		frigateP2 = atlas.createSprite("frigatep2")
		frigateP3 = atlas.createSprite("frigatep3")
		frigateP4 = atlas.createSprite("frigatep4")

		debrisSmall = atlas.createSprite("debrissmall")
		debrisMed = atlas.createSprite("debrismed")
		debrisLarge = atlas.createSprite("debrislarge")

		fish1 = atlas.createSprite("fish1")
		fish2 = atlas.createSprite("fish2")
		fish3 = atlas.createSprite("fish3")
		fish4 = atlas.createSprite("fish4")
		fish5 = atlas.createSprite("fish5")
		fish6 = atlas.createSprite("fish6")
		fish7 = atlas.createSprite("fish7")
		fish8 = atlas.createSprite("fish8")

		needle = atlas.createSprite("needle")

		background = atlas.createSprite("background")

		blackFade = atlas.createSprite("blackfade")

		laser = atlas.createSprite("laser")
		missile = atlas.createSprite("missile")
		bomb = atlas.createSprite("bomb")

		production1 = atlas.createSprite("production1")
		production2 = atlas.createSprite("production2")
		production3 = atlas.createSprite("production3")

		production_tile1 = atlas.createSprite("productiontile")
		production_tile1.rotate90(true)
		production_tile1.rotate90(true)
		production_tile2 = atlas.createSprite("productiontile")
		production_tile2.rotate90(false)
		production_tile3 = atlas.createSprite("productiontile")
		production_tile4 = atlas.createSprite("productiontile")
		production_tile4.rotate90(true)

		upgradeOutline = atlas.createSprite("upgradeoutline")
		frigateOutline = atlas.createSprite("frigateoutline")
		bomberOutline = atlas.createSprite("bomberoutline")
		fighterOutline = atlas.createSprite("fighteroutline")

		healthNone = atlas.createSprite("healthnone")
		healthSome = atlas.createSprite("healthsome")
		healthFull = atlas.createSprite("healthfull")

		aButton = atlas.createSprite("abutton")
		aCpuButton = atlas.createSprite("acpubutton")
		aPlayerButton = atlas.createSprite("aplayerbutton")
		
		cpuButton = atlas.createSprite("cpubutton")
		playerButton = atlas.createSprite("playerbutton")

		cnt1 = atlas.createSprite("1")
		cnt2 = atlas.createSprite("2")
		cnt3 = atlas.createSprite("3")
		cnt4 = atlas.createSprite("4")
		cnt5 = atlas.createSprite("5")

		spark = atlas.createSprite("spark")
		bubble = atlas.createSprite("bubble")
		bigbubble = atlas.createSprite("bigbubble")
		explosion = atlas.createSprite("explosion")

		factoryHeavyDamage1 = atlas.createSprite("factoryheavydamage1")
		factoryHeavyDamage2 = atlas.createSprite("factoryheavydamage2")
		factoryHeavyDamage3 = atlas.createSprite("factoryheavydamage3")
		factoryLightDamage1 = atlas.createSprite("factorylightdamage1")
		factoryLightDamage2 = atlas.createSprite("factorylightdamage2")
		factoryLightDamage3 = atlas.createSprite("factorylightdamage3")

		touchArea1 = atlas.createSprite("touchArea")
		touchArea2 = atlas.createSprite("touchArea")
		touchArea3 = atlas.createSprite("touchArea")
		touchArea4 = atlas.createSprite("touchArea")
		
		help = atlas.createSprite("help")
		musicOnOff = atlas.createSprite("music")
		back = atlas.createSprite("back")
		settings = atlas.createSprite("settings")
		checkboxOn = atlas.createSprite("checkboxon")
		checkboxOff = atlas.createSprite("checkboxoff")

		title = new Sprite(new Texture(Gdx.files.internal("data/spritepack/title.png")))
		title.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear)
		credits = new Sprite(new Texture(Gdx.files.internal("data/spritepack/credits.png")))
		credits.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear)
	}

	def dispose() {
		atlas.dispose()
	}

}

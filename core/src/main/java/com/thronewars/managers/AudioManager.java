package com.thronewars.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.thronewars.GdxGame;
import com.thronewars.utils.RandomUtils;

/**
 * Manages background music and sound effects.
 */
public class AudioManager {
	
	private final GdxGame game;
	
	private Music currentMusic;
	public final Music MAIN_MENU_MUSIC;

	public final Sound SWORD_ATTACK_SOUND1;
	public final Sound SWORD_ATTACK_SOUND2;
	public final Sound SWORD_ATTACK_SOUND3;
	
	public AudioManager(GdxGame game) {
		this.game = game;
		
		// Music
		MAIN_MENU_MUSIC = Gdx.audio.newMusic(Gdx.files.internal("audio/music/MainMenu.mp3"));
		MAIN_MENU_MUSIC.setLooping(true);
		
		// Sounds
		SWORD_ATTACK_SOUND1 = Gdx.audio.newSound(Gdx.files.internal("audio/sounds/attack/Sword Attack 1.wav"));
		SWORD_ATTACK_SOUND2 = Gdx.audio.newSound(Gdx.files.internal("audio/sounds/attack/Sword Attack 2.wav"));
		SWORD_ATTACK_SOUND3 = Gdx.audio.newSound(Gdx.files.internal("audio/sounds/attack/Sword Attack 3.wav"));
	}
	
	public void playSwordAttackSound() {
		float volume = game.getSettingsManager().getSoundEffectsVolume();
		switch (RandomUtils.randomInt(1, 3)) {
			case 1: SWORD_ATTACK_SOUND1.play(volume); break;
			case 2: SWORD_ATTACK_SOUND2.play(volume); break;
			default: SWORD_ATTACK_SOUND3.play(volume); break;
		}
	}

	public void playMusic(Music music) {
        if (currentMusic == music && currentMusic.isPlaying()) {
            return;
        }

        stopMusic();

        currentMusic = music;
        currentMusic.setVolume(game.getSettingsManager().getMusicVolume());
        currentMusic.play();
    }
	
	public void toggleMusic() {
		if (currentMusic != null) {
			if (currentMusic.isPlaying()) {
				currentMusic.pause();
			}
			else {
				currentMusic.play();
			}
		}
	}
	
	public void stopMusic() {
		if (currentMusic != null) {
			currentMusic.stop();
			currentMusic = null;
		}
	}
	
	public void setMusicVolume(float volume) {
	    game.getSettingsManager().setMusicVolume(volume);

	    if (currentMusic != null) {
	        currentMusic.setVolume(game.getSettingsManager().getMusicVolume());
	    }
	}

	public void saveMusicVolume() {
	    game.getSettingsManager().saveMusicVolume();
	}

	public void setSoundEffectsVolume(float volume) {
	    game.getSettingsManager().setSoundEffectsVolume(volume);
	}

	public void saveSoundEffectsVolume() {
	    game.getSettingsManager().saveSoundEffectsVolume();
	}

	public void dispose() {
		stopMusic();
		MAIN_MENU_MUSIC.dispose();
		
		SWORD_ATTACK_SOUND1.dispose();
		SWORD_ATTACK_SOUND2.dispose();
		SWORD_ATTACK_SOUND3.dispose();
    }
}
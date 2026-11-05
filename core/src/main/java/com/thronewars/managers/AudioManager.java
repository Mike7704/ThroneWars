package com.thronewars.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.thronewars.GdxGame;

/**
 * Manages background music and sound effects.
 */
public class AudioManager {
	
	private final GdxGame game;
	
	private Music currentMusic;
	public final Music MAIN_MENU_MUSIC;

	public AudioManager(GdxGame game) {
		this.game = game;
		
		MAIN_MENU_MUSIC = Gdx.audio.newMusic(Gdx.files.internal("audio/music/MainMenu.mp3"));
		MAIN_MENU_MUSIC.setLooping(true);
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
    }
}
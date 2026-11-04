package com.thronewars.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * Manages background music and sound effects.
 */
public class AudioManager {

	private float musicVolume = 0.33f;
	
	private Music currentMusic;
	public final Music MAIN_MENU_MUSIC;

	public AudioManager() {
		MAIN_MENU_MUSIC = Gdx.audio.newMusic(Gdx.files.internal("audio/music/MainMenu.mp3"));
		MAIN_MENU_MUSIC.setLooping(true);
	}

	public void playMusic(Music music) {
        if (currentMusic == music && currentMusic.isPlaying()) {
            return;
        }

        stopMusic();

        currentMusic = music;
        currentMusic.setVolume(musicVolume);
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
	
	public void setVolume(float volume) {
        musicVolume = Math.max(0f, Math.min(volume, 1f));

        if (currentMusic != null) {
            currentMusic.setVolume(musicVolume);
        }
    }

	public void dispose() {
		stopMusic();
		MAIN_MENU_MUSIC.dispose();
    }
}
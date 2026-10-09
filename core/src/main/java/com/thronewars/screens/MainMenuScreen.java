package com.thronewars.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.thronewars.GdxGame;
import com.thronewars.ui.components.TWButton;

/**
 * Main menu screen for the game.
 */
public class MainMenuScreen implements Screen {

	private final Stage mainMenuStage;
	private final GdxGame game;
	
	public MainMenuScreen(GdxGame game) {
		this.game = game;
		
		mainMenuStage = new Stage(new ScreenViewport());
		
		createMainMenu();
	}
	
	private void createMainMenu() {
		Table table = new Table();
		table.setFillParent(true);
		
		float width = 240f;
		float height = 60f;
		
		TWButton playButton = new TWButton("Play", width, height);
		TWButton settingsButton = new TWButton("Settings", width, height);
		TWButton exitButton = new TWButton("Exit", width, height);
		
		playButton.setOnClick(() -> {
		    game.setScreen(new GameScreen(game));
		    Gdx.app.postRunnable(MainMenuScreen.this::dispose);
		});
		
		settingsButton.setOnClick(() -> System.out.println("Settings Button"));
		
		exitButton.setOnClick(() -> Gdx.app.exit());
		
		table.add(playButton).width(width).height(height).pad(10f);
		table.row();
		table.add(settingsButton).width(width).height(height).pad(10f);
		table.row();
		table.add(exitButton).width(width).height(height).pad(10f);
		
		mainMenuStage.addActor(table);
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(mainMenuStage);
	}

	@Override
	public void render(float delta) {
		input();
		update(delta);
		draw(delta);
	}

	private void input() {

	}

	private void update(float delta) {

	}

	private void draw(float delta) {
		ScreenUtils.clear(Color.BLACK);
		
		mainMenuStage.act(delta);
		mainMenuStage.draw();
	}
	
	@Override
	public void resize(int width, int height) {
		mainMenuStage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		mainMenuStage.dispose();
	}

}

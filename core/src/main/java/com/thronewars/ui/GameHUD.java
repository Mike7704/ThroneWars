
package com.thronewars.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.thronewars.entities.EntityUnit;
import com.thronewars.entities.PlayerUnitRoster;
import com.thronewars.managers.ScreenManager;
import com.thronewars.managers.UIManager;
import com.thronewars.screens.GameScreen;
import com.thronewars.ui.components.TWButton;
import com.thronewars.ui.components.TWUnitIcon;
import com.thronewars.ui.components.TWLabel;

/**
 * Displays the game HUD to manage troops etc.
 */
public class GameHUD {

	private final Stage stage;

	private final GameScreen gameScreen;
	
	private final TWLabel goldLabel;
	private final TWLabel playerBaseHealthLabel;
	private final TWLabel enemyBaseHealthLabel;
	private final TWLabel unitCountLabel;

	public GameHUD(UIManager uiManager, GameScreen gameScreen, PlayerUnitRoster playerUnitRoster) {
		stage = new Stage(new FitViewport(ScreenManager.VIEW_WIDTH, ScreenManager.VIEW_HEIGHT));
		this.gameScreen = gameScreen;
		
		Table root = new Table();
		root.setFillParent(true);
		stage.addActor(root);

		// Bottom HUD panel
		Table hud = new Table();
		hud.setFillParent(true);
		hud.bottom();
		hud.pad(15f);

		// Player roster unit characters
		Table unitsTable = new Table();

		for (EntityUnit unit : playerUnitRoster.getUnits()) {
			TWUnitIcon portrait = new TWUnitIcon(unit);

			TWLabel nameLabel = new TWLabel(unit.getName(), uiManager.getLabelSmallStyle());

			TWLabel costLabel = new TWLabel(unit.getCost() + " Gold", uiManager.getLabelSmallStyle());

			TWButton unitButton = new TWButton("", uiManager.getButtonStyle());

			Table buttonContent = new Table();
			buttonContent.add(portrait).size(64f).padRight(8f);

			Table textTable = new Table();
			textTable.add(nameLabel).left().row();
			textTable.add(costLabel).left();

			buttonContent.add(textTable).expandY().center();

			unitButton.add(buttonContent).expand().fill().pad(5f);

			unitsTable.add(unitButton).width(200f).height(90f).padRight(10f);
			
			unitButton.setOnClick(() -> gameScreen.recruitUnit(unit));
		}

		// Game information
		Table informationTable = new Table();

		goldLabel = new TWLabel("Gold: 0", uiManager.getLabelMediumStyle());
		playerBaseHealthLabel = new TWLabel("Your Base: 0", uiManager.getLabelSmallStyle());
		enemyBaseHealthLabel = new TWLabel("Enemy Base: 0", uiManager.getLabelSmallStyle());
		unitCountLabel = new TWLabel("Units: 0", uiManager.getLabelSmallStyle());

		informationTable.add(goldLabel).left().row();
		informationTable.add(playerBaseHealthLabel).left().row();
		informationTable.add(enemyBaseHealthLabel).left().row();
		informationTable.add(unitCountLabel).left();

		// Arrange both sections at the bottom
		hud.add(unitsTable).left().expandX();
		hud.add(informationTable).right();

		root.add(hud).expand().fill();
	}

	public void updateValues() {
		playerBaseHealthLabel.setText("Your Base: " + gameScreen.getPlayerHealth());
		goldLabel.setText("Gold: " + gameScreen.getPlayerGold());
		unitCountLabel.setText("Units: " + gameScreen.getPlayerUnits().size);
		
		enemyBaseHealthLabel.setText("Enemy Base: " + gameScreen.getOpponentHealth());
	}
	
	public void render(float delta) {
		stage.act(delta);
		stage.draw();
	}

	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	public void dispose() {
		stage.dispose();
	}

	public Stage getStage() {
		return stage;
	}
}

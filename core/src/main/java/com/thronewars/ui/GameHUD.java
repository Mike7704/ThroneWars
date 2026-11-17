
package com.thronewars.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.thronewars.entities.EntityUnit;
import com.thronewars.entities.UnitRoster;
import com.thronewars.entities.UnitCommand;
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
	private final UIManager uiManager;
	
	private TWLabel goldLabel;
	private TWLabel playerBaseHealthLabel;
	private TWLabel enemyBaseHealthLabel;
	private TWLabel unitCountLabel;

	public GameHUD(UIManager uiManager, GameScreen gameScreen, UnitRoster playerUnitRoster) {
		stage = new Stage(new FitViewport(ScreenManager.VIEW_WIDTH, ScreenManager.VIEW_HEIGHT));
		this.uiManager = uiManager;
		this.gameScreen = gameScreen;
		
		Table root = new Table();
		root.setFillParent(true);
		stage.addActor(root);

		// Bottom HUD panel
		Table hud = new Table();
		hud.setFillParent(true);
		hud.bottom();
		hud.pad(15f);

		Table recruitUnitsTable = recruitUnitsTable(playerUnitRoster);
		Table commandUnitsTable = commandUnitsTable();
		Table gameInfoTable = gameInfoTable();

		// Arrange sections at the bottom
		hud.add(commandUnitsTable).colspan(2).center().padBottom(10f).row();
		hud.add(recruitUnitsTable).left().expandX();
		hud.add(gameInfoTable).right();

		root.add(hud).expand().fill();
	}

	/**
	 * Creates a table containing the player's unit roster with buttons to recruit units.
	 */
	private Table recruitUnitsTable(UnitRoster playerUnitRoster) {
		Table recruitUnitsTable = new Table();

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

			recruitUnitsTable.add(unitButton).width(200f).height(90f).padRight(10f);
			
			unitButton.setOnClick(() -> gameScreen.recruitPlayerUnit(unit));
		}
		
		return recruitUnitsTable;
	}
	
	/**
	 * Creates a table containing buttons for unit commands (Retreat, Defend, Attack).
	 */
	private Table commandUnitsTable() {
		Table commandUnitsTable = new Table();

		TWButton retreatButton = new TWButton("RETREAT", uiManager.getButtonStyle());
		TWButton defendButton = new TWButton("DEFEND", uiManager.getButtonStyle());
		TWButton attackButton = new TWButton("ATTACK", uiManager.getButtonStyle());

		retreatButton.setOnClick(() -> gameScreen.setPlayerUnitCommand(UnitCommand.RETREAT));
		defendButton.setOnClick(() -> gameScreen.setPlayerUnitCommand(UnitCommand.DEFEND));
		attackButton.setOnClick(() -> gameScreen.setPlayerUnitCommand(UnitCommand.ATTACK));

		commandUnitsTable.add(retreatButton).width(250f).height(90f).padRight(10f);
		commandUnitsTable.add(defendButton).width(250f).height(90f).padRight(10f);
		commandUnitsTable.add(attackButton).width(250f).height(90f);
		
		return commandUnitsTable;
	}
	
	/**
	 * Creates a table displaying game information such as gold, base health, and unit count.
	 */
	private Table gameInfoTable() {
		Table gameInfoTable = new Table();

		goldLabel = new TWLabel("Gold: 0", uiManager.getLabelMediumStyle());
		playerBaseHealthLabel = new TWLabel("Your Base: 0", uiManager.getLabelSmallStyle());
		enemyBaseHealthLabel = new TWLabel("Enemy Base: 0", uiManager.getLabelSmallStyle());
		unitCountLabel = new TWLabel("Units: 0", uiManager.getLabelSmallStyle());

		gameInfoTable.add(goldLabel).left().row();
		gameInfoTable.add(playerBaseHealthLabel).left().row();
		gameInfoTable.add(enemyBaseHealthLabel).left().row();
		gameInfoTable.add(unitCountLabel).left();
		
		return gameInfoTable;
	}
	
	/**
	 * Updates the values displayed in the HUD based on the current game state.
	 */
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

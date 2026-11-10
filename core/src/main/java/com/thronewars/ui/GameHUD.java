
package com.thronewars.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.thronewars.entities.EntityCharacter;
import com.thronewars.entities.PlayerCharacterRoster;
import com.thronewars.managers.ScreenManager;
import com.thronewars.managers.UIManager;
import com.thronewars.ui.components.TWButton;
import com.thronewars.ui.components.TWCharacterIcon;
import com.thronewars.ui.components.TWLabel;

/**
 * Displays the game HUD to manage troops etc.
 */
public class GameHUD {

	private final Stage stage;

	private final TWLabel goldLabel;
	private final TWLabel playerBaseHealthLabel;
	private final TWLabel enemyBaseHealthLabel;
	private final TWLabel unitCountLabel;

	public GameHUD(UIManager uiManager, PlayerCharacterRoster playerCharacterRoster) {
		stage = new Stage(new FitViewport(ScreenManager.VIEW_WIDTH, ScreenManager.VIEW_HEIGHT));

		Table root = new Table();
		root.setFillParent(true);
		stage.addActor(root);

		// Bottom HUD panel
		Table hud = new Table();
		hud.setFillParent(true);
		hud.bottom();
		hud.pad(15f);

		// Player roster characters
		Table charactersTable = new Table();

		for (EntityCharacter character : playerCharacterRoster.getCharacters()) {
			TWCharacterIcon portrait = new TWCharacterIcon(character);

			TWLabel nameLabel = new TWLabel(character.getName(), uiManager.getLabelSmallStyle());

			TWLabel costLabel = new TWLabel(character.getCost() + " Gold", uiManager.getLabelSmallStyle());

			TWButton characterButton = new TWButton("", uiManager.getButtonStyle());

			Table buttonContent = new Table();
			buttonContent.add(portrait).size(64f).padRight(8f);

			Table textTable = new Table();
			textTable.add(nameLabel).left().row();
			textTable.add(costLabel).left();

			buttonContent.add(textTable).expandY().center();

			characterButton.add(buttonContent).expand().fill().pad(5f);

			charactersTable.add(characterButton).width(200f).height(90f).padRight(10f);
		}

		// Game information
		Table informationTable = new Table();

		goldLabel = new TWLabel("Gold: 500", uiManager.getLabelMediumStyle());
		playerBaseHealthLabel = new TWLabel("Your Base: 100%", uiManager.getLabelSmallStyle());
		enemyBaseHealthLabel = new TWLabel("Enemy Base: 100%", uiManager.getLabelSmallStyle());
		unitCountLabel = new TWLabel("Units: 1", uiManager.getLabelSmallStyle());

		informationTable.add(goldLabel).left().row();
		informationTable.add(playerBaseHealthLabel).left().row();
		informationTable.add(enemyBaseHealthLabel).left().row();
		informationTable.add(unitCountLabel).left();

		// Arrange both sections at the bottom
		hud.add(charactersTable).left().expandX();
		hud.add(informationTable).right();

		root.add(hud).expand().fill();
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

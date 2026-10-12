package com.thronewars.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;

/**
 * Loads and manages game assets using LibGDX's AssetManager.
 */
public class GameAssets {

	private static final String UI_ATLAS = "textures/ui/throne-wars-ui.atlas";

	private static final String UI_FONT = "fonts/CinzelDecorative-Bold.otf";
	private static final int UI_FONT_SIZE = 24;
	
	private static final String GAME_BACKGROUND_WINTER = "textures/environment/winter/3.png";

	private final AssetManager assetManager;

	private boolean loaded;

	public GameAssets() {
		assetManager = new AssetManager();
		configureLoaders();
	}

	/**
	 * Configures the loaders for the AssetManager to handle FreeType fonts.
	 */
	private void configureLoaders() {
		InternalFileHandleResolver resolver = new InternalFileHandleResolver();

		// Register the FreeType font generator loader
		assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));

		// Register font generation for .otf files
		assetManager.setLoader(BitmapFont.class, ".otf", new FreetypeFontLoader(resolver));
	}

	/**
	 * Loads the game assets using the AssetManager.
	 */
	public void load() {
		if (loaded) {
			return;
		}

		// Queue the UI atlas
		assetManager.load(UI_ATLAS, TextureAtlas.class);

		// Queue a BitmapFont generated from the OTF file
		FreeTypeFontLoaderParameter fontParams = new FreeTypeFontLoaderParameter();

		fontParams.fontFileName = UI_FONT;
		fontParams.fontParameters.size = UI_FONT_SIZE;

		assetManager.load(UI_FONT, BitmapFont.class, fontParams);

		// Queue the game background
		assetManager.load(GAME_BACKGROUND_WINTER, Texture.class);

		// Complete the initial load before screens use the assets
		assetManager.finishLoading();

		// Configure the background after it has loaded
		Texture background = getGameBackground();
		background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

		loaded = true;
	}

	public TextureAtlas getUIAtlas() {
		return assetManager.get(UI_ATLAS, TextureAtlas.class);
	}

	public BitmapFont getUIFont() {
		return assetManager.get(UI_FONT, BitmapFont.class);
	}

	public Texture getGameBackground() {
		return assetManager.get(GAME_BACKGROUND_WINTER, Texture.class);
	}

	/**
	 * Updates the AssetManager's loading process.
	 *
	 * @return true if all assets are loaded, false otherwise.
	 */
	public boolean update() {
		boolean finished = assetManager.update();

		if (finished) {
			loaded = true;
		}

		return finished;
	}

	public float getProgress() {
		return assetManager.getProgress();
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void dispose() {
		assetManager.dispose();
	}
}

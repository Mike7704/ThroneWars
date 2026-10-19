package com.thronewars.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
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

	private static final String UI_FONT_FILE = "fonts/Cinzel-Bold.otf";
	private static final String UI_FONT_SMALL = "generated/cinzel-small.otf";
	private static final String UI_FONT_MEDIUM = "generated/cinzel-medium.otf";
	private static final String UI_FONT_LARGE = "generated/cinzel-large.otf";
	private static final int UI_FONT_SIZE_SMALL = 20;
	private static final int UI_FONT_SIZE_MEDIUM = 40;
	private static final int UI_FONT_SIZE_LARGE = 60;
	
	private static final String GAME_BACKGROUND_WINTER = "textures/environment/winter/3.png";

	// Unit images
	private static final String JON_SNOW_IDLE = "textures/units/jon-snow/idle.png";
	
	private final AssetManager assetManager;

	private boolean loaded;

	public GameAssets() {
		assetManager = new AssetManager();
		configureLoaders();
	}

	/**
	 * Configures the loaders used by the AssetManager.
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

		queueFonts();
		queueTextures();
		
		// Ensure all queued assets are available before configuring them.
		assetManager.finishLoading();
		
		configureTextures();
		
	    loaded = true;
	}

	/**
	 * Queues all UI fonts for loading.
	 */
	private void queueFonts() {		
	    loadFont(UI_FONT_SMALL, UI_FONT_SIZE_SMALL);
	    loadFont(UI_FONT_MEDIUM, UI_FONT_SIZE_MEDIUM);
	    loadFont(UI_FONT_LARGE, UI_FONT_SIZE_LARGE);
	}
	
	/**
	 * Loads a font with the specified file name and size.
	 *
	 * @param assetKey The key to identify the font in the AssetManager.
	 * @param size The desired font size.
	 */
	private void loadFont(String assetKey, int size) {
		FreeTypeFontLoaderParameter fontParams = new FreeTypeFontLoaderParameter();

		fontParams.fontFileName = UI_FONT_FILE;
		fontParams.fontParameters.size = size;
		fontParams.fontParameters.minFilter = TextureFilter.Linear;
		fontParams.fontParameters.magFilter = TextureFilter.Linear;
		
		assetManager.load(assetKey, BitmapFont.class, fontParams);
	}

	/**
	 * Queues texture atlases and individual textures for loading.
	 */
	private void queueTextures() {
	    assetManager.load(UI_ATLAS, TextureAtlas.class);

	    assetManager.load(GAME_BACKGROUND_WINTER, Texture.class);
	    assetManager.load(JON_SNOW_IDLE, Texture.class);
	}

	/**
	 * Configures texture filtering and wrapping after loading.
	 */
	private void configureTextures() {
	    configureBackground();
	    configureCharacterTextures();
	}

	/**
	 * Configures the game background texture.
	 */
	private void configureBackground() {
		Texture background = getGameBackground();

		setTextureFilter(background, TextureFilter.Nearest);
		background.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}

	/**
	 * Configures character sprite textures.
	 */
	private void configureCharacterTextures() {
	    setTextureFilter(getJonSnowIdle(), TextureFilter.Nearest);
	}

	/**
	 * Configures the filtering used when a texture is scaled.
	 *
	 * @param texture The texture to configure.
	 * @param filter The filtering mode to apply.
	 */
	private void setTextureFilter(Texture texture, TextureFilter filter) {
		texture.setFilter(filter, filter);
	}
	
	public TextureAtlas getUIAtlas() {
		return assetManager.get(UI_ATLAS, TextureAtlas.class);
	}

	public BitmapFont getSmallFont() {
	    return assetManager.get(UI_FONT_SMALL, BitmapFont.class);
	}

	public BitmapFont getMediumFont() {
	    return assetManager.get(UI_FONT_MEDIUM, BitmapFont.class);
	}

	public BitmapFont getLargeFont() {
	    return assetManager.get(UI_FONT_LARGE, BitmapFont.class);
	}

	public Texture getGameBackground() {
		return assetManager.get(GAME_BACKGROUND_WINTER, Texture.class);
	}

	public Texture getJonSnowIdle() {
	    return assetManager.get(JON_SNOW_IDLE, Texture.class);
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

package com.thronewars.assets;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
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
	
	// Unit images
	private static final String UNIT_TEXTURE_DIRECTORY = "textures/units/";
	private static final String TEXTURE_EXTENSION = ".png";
	
	// Effect images
	private static final String SHADOW_TEXTURE = "textures/effects/shadow.png";
	
	private final AssetManager assetManager;

	private final Map<UnitDefinition, UnitAssets> unitAssets = new HashMap<>();
	
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
	 * Queues textures for loading.
	 */
	private void queueTextures() {
	    assetManager.load(UI_ATLAS, TextureAtlas.class);
	    
	    // Effects
	    assetManager.load(SHADOW_TEXTURE, Texture.class);
	    
	    // Unit characters
	    for (UnitDefinition unit : UnitDefinitions.ALL) {
	        loadUnitTextures(unit);
	    }
	}

	/**
	 * Configures texture filtering and wrapping after loading.
	 */
	private void configureTextures() {
	    // Unit characters
	    for (UnitDefinition unit : UnitDefinitions.ALL) {
	        configureUnitTextures(unit);
	    }
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
	
	/**
	 * Queues all animation textures for a unit character.
	 *
	 * @param unit The unit character whose animation textures should be loaded.
	 */
	private void loadUnitTextures(UnitDefinition unit) {
	    for (UnitAnimation animation : unit.getAnimations()) {
	        String path = getUnitTexturePath(unit, animation);
	        assetManager.load(path, Texture.class);
	    }
	}

	/**
	 * Configures the filtering for all of a unit character's animation textures.
	 *
	 * @param unit The unit character whose animation textures should be configured.
	 */
	private void configureUnitTextures(UnitDefinition unit) {
		for (UnitAnimation animation : unit.getAnimations()) {
			setTextureFilter(getUnitTexture(unit, animation), TextureFilter.Nearest);
		}
	}

	/**
	 * Builds the asset path for a unit character's animation texture.
	 *
	 * @param unit The unit character to get the texture path for.
	 * @param animation The animation represented by the texture.
	 * @return The full asset path.
	 */
	private String getUnitTexturePath(UnitDefinition unit, UnitAnimation animation) {
		return UNIT_TEXTURE_DIRECTORY + unit.getId() + "/" + animation.getFileName() + TEXTURE_EXTENSION;
	}

	/**
	 * Returns a previously loaded unit character animation texture.
	 *
	 * @param unit The unit character to get the textures for.
	 * @param animation The animation to retrieve.
	 * @return The requested texture.
	 */
	public Texture getUnitTexture(UnitDefinition unit, UnitAnimation animation) {
		
		if (unit == null) {
			throw new IllegalArgumentException("Unit cannot be null.");
		}
		
		if (animation == null) {
			throw new IllegalArgumentException("Animation cannot be null.");
		}
		
		if (!unit.hasAnimation(animation)) {
			throw new IllegalArgumentException(unit.getId() + " does not support " + animation);
	    }
		
		return assetManager.get(getUnitTexturePath(unit, animation), Texture.class);
	}
	
	/**
	 * Returns the CharacterAssets for a given unit character.
	 *
	 * @param unit The unit character to retrieve assets for.
	 * @return The UnitAssets associated with the unit character.
	 */
	public UnitAssets getUnitAssets(UnitDefinition unit) {
		if (unit == null) {
			throw new IllegalArgumentException("Unit definition cannot be null.");
		}

		UnitAssets assets = unitAssets.get(unit);

		// If the assets have not been created yet, create and cache them
		if (assets == null) {
			Map<UnitAnimation, Texture> textures = new EnumMap<>(UnitAnimation.class);

			for (UnitAnimation animation : unit.getAnimations()) {
				textures.put(animation, getUnitTexture(unit, animation));
			}

			assets = new UnitAssets(unit, textures);
			unitAssets.put(unit, assets);
		}

		return assets;
	}
	
	public Texture getShadowTexture() {
	    return assetManager.get(SHADOW_TEXTURE, Texture.class);
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
	
	public void dispose() {
		assetManager.dispose();
	}
}

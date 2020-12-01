package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.PlayScreen;

public class MyGame extends Game {

	public static final int WIDTH = 400;
	public static final int Hieght = 208;
	public SpriteBatch batch;
	public static final float pxPerMeter = 100;

	public static final short EMPTY_BIT = 0;
	public static final short DEFAULT_BIT = 1;
	public static final short PRINCESS_BIT = 2;
	public static final short COINS_BIT = 4;
	public static final short COLLECTED_BIT = 8;
	public static final short MONSTER_BIT = 16;
	public static final short MONSTER_HEAD_BIT = 32;
	public static final short BARRIER_BIT = 64;


	public static AssetManager assetManager ;


	@Override
	public void create() {
		batch = new SpriteBatch();
		assetManager = new AssetManager();
		assetManager.load("sounds/music.wav" , Music.class);
		assetManager.load("sounds/collect_sound.wav" , Sound.class);
		assetManager.load("sounds/hit.wav" , Sound.class);
		assetManager.load("sounds/gameOver.wav" , Sound.class);
		assetManager.finishLoading();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		assetManager.dispose();
		batch.dispose();
	}
}


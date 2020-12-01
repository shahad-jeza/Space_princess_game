package com.mygdx.game.Charcters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.MyGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Screens.PlayScreen;

public class Coins extends InteractiveObject {
    public Coins(PlayScreen screen, Rectangle rectangle) {
        super(screen, rectangle);
        fixture.setUserData(this);
        CategoryFilter(MyGame.COINS_BIT);
    }

    @Override
    public void headHit() {
        Gdx.app.log("coins" , "collision");
        CategoryFilter(MyGame.COLLECTED_BIT);
        getCell().setTile(null);
        Hud.increaseScore(100);
        MyGame.assetManager.get("sounds/collect_sound.wav" , Sound.class).play();
    }
}
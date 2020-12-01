package com.mygdx.game.Charcters;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlayScreen;


public abstract class Monsters extends Sprite {
    protected World world;
    protected PlayScreen screen ;
    public Body body;
    public Vector2 velocity;
    private boolean toRight;
    public Monsters(PlayScreen screen , float x , float y ){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x,y);
        defineMonsters();
        velocity = new Vector2((float) 0.7,0);
        toRight = true;

    }
    protected abstract void defineMonsters();
    public abstract void update(float deltaTime);
    public abstract void hitOnHead();

    public void reVelocity(boolean x , boolean y ){
        if(x)
            velocity.x = -velocity.x;
        if(y)
            velocity.y = -velocity.y;
    }
}

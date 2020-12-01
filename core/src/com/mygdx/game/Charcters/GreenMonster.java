package com.mygdx.game.Charcters;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Screens.PlayScreen;

public class GreenMonster extends Monsters {
    private float stateTimer ;
    private TextureRegion walk ;
    private boolean setToDestroy ;
    private boolean destroy  ;
    private boolean runDirection;


    public GreenMonster(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        walk = (new TextureRegion(screen.getAtlas().findRegion("monster1") , 1 , 1 , 85 ,85));
        stateTimer = 0;
        setBounds(getX() , getY() , 16 /MyGame.pxPerMeter ,16 / MyGame.pxPerMeter);
        setToDestroy = false ;
         destroy = false;
         runDirection = true;
    }

    public void update( float deltaTime){
        stateTimer += deltaTime;
        if(setToDestroy && !destroy){
            world.destroyBody(body);
            destroy = true ;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("monster3"),1,1,85,85));
            stateTimer = 0;
        }
        else if(!destroy) {
            body.setLinearVelocity(velocity);
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            setRegion(walk);
        }
    }


    @Override
    protected void defineMonsters() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX() , getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody ;// will move and interact with the world
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(6/ MyGame.pxPerMeter);

        fixtureDef.filter.categoryBits = MyGame.MONSTER_BIT;
        fixtureDef.filter.maskBits = MyGame.DEFAULT_BIT | MyGame.COINS_BIT |MyGame.MONSTER_BIT|MyGame.PRINCESS_BIT|MyGame.BARRIER_BIT;


        fixtureDef.shape = circleShape;
        body.createFixture(fixtureDef).setUserData(this);

        PolygonShape polygonShape = new PolygonShape();
        Vector2[]vector2s = new Vector2[4];
        vector2s[0] = new Vector2(-5,8).scl(1/MyGame.pxPerMeter);
        vector2s[1] = new Vector2(5,8).scl(1/MyGame.pxPerMeter);
        vector2s[2] = new Vector2(-3,3).scl(1/MyGame.pxPerMeter);
        vector2s[3] = new Vector2(3,3).scl(1/MyGame.pxPerMeter);
        polygonShape.set(vector2s);

        fixtureDef.shape = polygonShape;
        fixtureDef.restitution = 0.5f;
        fixtureDef.filter.categoryBits = MyGame.MONSTER_HEAD_BIT;
        body.createFixture(fixtureDef).setUserData(this);
    }

    public void draw(Batch batch){
        if(!destroy || stateTimer < 1){
            super.draw(batch);
        }
    }

    @Override
    public void hitOnHead() {
        Hud.increaseScore(100);
        setToDestroy = true ;
        MyGame.assetManager.get("sounds/hit.wav" , Sound.class).play();
    }
    public void flip(boolean x , boolean y){
        if((body.getLinearVelocity().x>0 || !runDirection) && !walk.isFlipX()){
            walk.flip(true,false);
            runDirection = false;
        }
        else if ((body.getLinearVelocity().x < 0 || runDirection) && walk.isFlipX()){
            walk.flip(true , false);
            runDirection = true;
        }
    }
}

package com.mygdx.game.Charcters;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGame;
import com.mygdx.game.Screens.PlayScreen;

public class Princess extends Sprite {
    public enum State {FALLING ,JUMPING , STANDING , RUNNING , DEAD};
    public State current ;
    public State previous ;
    public World world;
    public Body body ;
    private TextureRegion princessStand;
    private TextureRegion princessDead;
    private Animation<TextureRegion> princessRun;
    private Animation<TextureRegion> princessJump;
    private float stateTimer;
    private boolean runDirection;
    private boolean princessIsDead;

    public Princess( PlayScreen screen){
        super(screen.getAtlas().findRegion("idle--1-"));
        this.world = screen.getWorld();
        current = State.STANDING;
        previous = State.STANDING;
        stateTimer = 0;
        runDirection = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i =3 ; i<6; i++) // the images for running are 3,4,5,6 in the PNG file
            frames.add(new TextureRegion(getTexture() , i*102 , 1,100,109));
            princessRun = new Animation<TextureRegion>(0.1f,frames);
            frames.clear();

        for (int i = 1 ; i<2 ; i++)
            frames.add(new TextureRegion(getTexture() , i*100 , 0 , 100 , 109));
            princessJump = new Animation<TextureRegion>(0.1f,frames);

        definePrincess();
        princessStand = new TextureRegion(getTexture() , 1 , 1,100,109);
        setBounds(0,0,16/MyGame.pxPerMeter , 16/MyGame.pxPerMeter);
        setRegion(princessStand);

        princessDead = new TextureRegion(screen.getAtlas().findRegion("dead"),613,25,102,85);

    }
    public void update(float deltaTime){
        setPosition(body.getPosition().x-getWidth()/2,body.getPosition().y - getHeight()/2);
        setRegion(getFrame(deltaTime));
    }
    public TextureRegion getFrame(float deltaTime){
        current = getState();
        TextureRegion textureRegion ;
        switch (current){
            case DEAD:
            case FALLING:
                textureRegion = princessDead;
                break;
            case RUNNING:
                textureRegion = (TextureRegion) princessRun.getKeyFrame(stateTimer , true);
                break;
            case JUMPING:
                textureRegion = (TextureRegion) princessJump.getKeyFrame(stateTimer);
                break;
            case STANDING:
            default:
                textureRegion = princessStand;
                break;
        }
        if((body.getLinearVelocity().x<0 || !runDirection) && !textureRegion.isFlipX()){
            textureRegion.flip(true,false);
            runDirection = false;
        }
        else if ((body.getLinearVelocity().x > 0 || runDirection) && textureRegion.isFlipX()){
            textureRegion.flip(true , false);
            runDirection = true;
        }

        stateTimer = current == previous ? stateTimer +deltaTime : 0;
        previous = current;
        return textureRegion;
    }
    public State getState(){
        if (princessIsDead)
            return State.DEAD;
        else if(body.getLinearVelocity().y>0 && current ==State.JUMPING || body.getLinearVelocity().y<0 && previous==State.JUMPING)  // that is mean she is jumping
            return State.JUMPING;
        else if(body.getPosition().y<-5)   // she is falling down
            return State.FALLING;
        else if(body.getLinearVelocity().x != 0 )  // she is moving to left or right
            return State.RUNNING;
        else
            return State.STANDING;
    }


    public void hit(){ // when the princess is hit by an enemy
        MyGame.assetManager.get("sounds/music.wav" , Music.class).stop();
        MyGame.assetManager.get("sounds/gameOver.wav" , Sound.class).play();
        princessIsDead = true;
        Filter filter = new Filter();
        filter.maskBits = MyGame.EMPTY_BIT;
        for (Fixture fixture : body.getFixtureList())
            fixture.setFilterData(filter);
        body.applyLinearImpulse(new Vector2(0,1f) , body.getWorldCenter() , true);
    }

    public void falling(){
        if(body.getPosition().y <0) {
            princessIsDead = true;
        }
    }

    public boolean isDead(){
        return princessIsDead;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public void definePrincess(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32/ MyGame.pxPerMeter,32/ MyGame.pxPerMeter);
        bodyDef.type = BodyDef.BodyType.DynamicBody ;// will move and intract with the world
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(6/ MyGame.pxPerMeter);

        fixtureDef.filter.categoryBits = MyGame.PRINCESS_BIT;
        fixtureDef.filter.maskBits = MyGame.DEFAULT_BIT | MyGame.COINS_BIT |MyGame.MONSTER_BIT|MyGame.MONSTER_HEAD_BIT|MyGame.BARRIER_BIT;


        fixtureDef.shape = circleShape;
        body.createFixture(fixtureDef).setUserData(this);
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/MyGame.pxPerMeter , 5 / MyGame.pxPerMeter) , new Vector2(2/MyGame.pxPerMeter , 5 / MyGame.pxPerMeter));
        fixtureDef.shape = head;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("head");
    }

    public void jump(){
        if(current != State.JUMPING){
            body.applyLinearImpulse(new Vector2(0,4f),body.getWorldCenter() , true);
            current=State.JUMPING;
        }
    }
}

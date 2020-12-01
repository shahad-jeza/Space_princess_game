package com.mygdx.game.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Charcters.*;
import com.mygdx.game.MyGame;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        int collisDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;


        if(fixtureA.getUserData() == "head" || fixtureB.getUserData() == " head"){
            Fixture head = fixtureA.getUserData() == "head" ? fixtureA : fixtureB;
            Fixture object = head == fixtureA ? fixtureB : fixtureA;

            if(object.getUserData() != null && InteractiveObject.class.isAssignableFrom(object.getUserData().getClass())){
                ((InteractiveObject)object.getUserData()).headHit();
            }
        }

        switch (collisDef){
            case MyGame.MONSTER_HEAD_BIT | MyGame.PRINCESS_BIT :
                if(fixtureA.getFilterData().categoryBits == MyGame.MONSTER_HEAD_BIT)
                    ((Monsters)fixtureA.getUserData()).hitOnHead();
                else
                    ((Monsters)fixtureB.getUserData()).hitOnHead();
                break;
            case MyGame.MONSTER_BIT | MyGame.BARRIER_BIT:
                if(fixtureA.getFilterData().categoryBits == MyGame.MONSTER_BIT) {
                    ((Monsters) fixtureA.getUserData()).reVelocity(true, false);// just reverse in x
                    ((Monsters) fixtureA.getUserData()).flip(true, false);
                }
                else
                    ((Monsters)fixtureB.getUserData()).reVelocity(true,false);
                     ((Monsters)fixtureB.getUserData()).flip(true,false);
                break;
            case MyGame.PRINCESS_BIT | MyGame.MONSTER_BIT:
                if(fixtureA.getFilterData().categoryBits == MyGame.PRINCESS_BIT)
                    ((Princess)fixtureA.getUserData()).hit();
                else
                    ((Princess)fixtureB.getUserData()).hit();
                break;
            case MyGame.COINS_BIT | MyGame.PRINCESS_BIT :
                if(fixtureA.getFilterData().categoryBits == MyGame.COINS_BIT)
                    ((Coins)fixtureA.getUserData()).headHit();
                else
                    ((Coins)fixtureB.getUserData()).headHit();
                break;
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}

package com.mygdx.game.Objects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Charcters.Coins;
import com.mygdx.game.Charcters.GreenMonster;
import com.mygdx.game.Charcters.Monsters;
import com.mygdx.game.MyGame;
import com.mygdx.game.Screens.PlayScreen;

public class Creator {
    private Array<GreenMonster>monsters;


    public Creator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bodyDef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        //create the body and fixture for the ground index = 2
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody; // not affected by physics laws
            bodyDef.position.set((rectangle.getX()+rectangle.getWidth()/2)/ MyGame.pxPerMeter  , (rectangle.getY()+rectangle.getHeight()/2)/ MyGame.pxPerMeter) ;
            body = world.createBody(bodyDef);
            polygonShape.setAsBox(rectangle.getWidth()/2 / MyGame.pxPerMeter , rectangle.getHeight()/2 / MyGame.pxPerMeter);
            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
        }

        // create the body and fixture for the coins index = 3

        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            new Coins(screen, rectangle);
        }

        // create the monsters index = 5
        monsters = new Array<GreenMonster>();
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            monsters.add(new GreenMonster(screen , rectangle.getX() / MyGame.pxPerMeter , rectangle.getY()/MyGame.pxPerMeter));
        }


        //  create the body and fixture for the barriers index = 4
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody; // not affected by physics laws
            bodyDef.position.set((rectangle.getX()+rectangle.getWidth()/2)/ MyGame.pxPerMeter  , (rectangle.getY()+rectangle.getHeight()/2)/ MyGame.pxPerMeter) ;
            body = world.createBody(bodyDef);
            polygonShape.setAsBox(rectangle.getWidth()/2 / MyGame.pxPerMeter , rectangle.getHeight()/2 / MyGame.pxPerMeter);
            fixtureDef.shape = polygonShape;
            fixtureDef.filter.categoryBits = MyGame.BARRIER_BIT;
            body.createFixture(fixtureDef);

        }
    }
    public Array<GreenMonster> getMonsters() {
        return monsters;
    }
}

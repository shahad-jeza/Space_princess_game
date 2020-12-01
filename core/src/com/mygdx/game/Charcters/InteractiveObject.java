package com.mygdx.game.Charcters;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.MyGame;
import com.mygdx.game.Screens.PlayScreen;

public abstract class InteractiveObject {
    protected World world ;
    protected TiledMap map ;
    protected TiledMapTile tiledMapTile ;
    protected Rectangle rectangle;
    protected Body body;
    protected Fixture fixture;

    public InteractiveObject(PlayScreen screen, Rectangle rectangle){
        this.world = screen.getWorld();
        this.map = screen.getMap() ;
        this.rectangle = rectangle;

        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();

        bodyDef.type = BodyDef.BodyType.StaticBody; // not affected by physics laws
        bodyDef.position.set((rectangle.getX()+rectangle.getWidth()/2)/ MyGame.pxPerMeter , (rectangle.y+rectangle.getHeight()/2)/ MyGame.pxPerMeter);
        body = world.createBody(bodyDef);
        polygonShape.setAsBox(rectangle.getWidth()/2 / MyGame.pxPerMeter, rectangle.getHeight()/2/ MyGame.pxPerMeter);
        fixtureDef.shape = polygonShape;
        fixture = body.createFixture(fixtureDef);
        fixtureDef.restitution = 0;
    }

    public abstract void headHit();
    public void CategoryFilter(short bit){
        Filter filter = new Filter();
        filter.categoryBits = bit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(1);
        return layer.getCell((int)(body.getPosition().x*MyGame.pxPerMeter / 16) ,
                ((int) (body.getPosition().y * MyGame.pxPerMeter / 16)));
    }
}

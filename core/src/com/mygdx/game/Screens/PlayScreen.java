package com.mygdx.game.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Charcters.Monsters;
import com.mygdx.game.Charcters.Princess;
import com.mygdx.game.MyGame;
import com.mygdx.game.Objects.Creator;
import com.mygdx.game.Objects.WorldContactListener;
import com.mygdx.game.Scenes.Hud;
import org.graalvm.compiler.loop.MathUtil;


public class PlayScreen implements Screen {

    private MyGame game;
    private OrthographicCamera gamecamera;
    private Viewport gamePort;
    private Hud hud ;

// creating the tiled map varibals

    private TmxMapLoader mapLoader ;
    private TiledMap map ;
    private OrthogonalTiledMapRenderer renderer;

// Box2D = is a 2D physics laws simulator

    private World world;
    private Box2DDebugRenderer box2DDebugRenderer ;
    private Princess player ;
    private TextureAtlas atlas;
    private Music music ;
    private Creator creator ;
    private static Rectangle worldBounds;


    public PlayScreen(MyGame game){

        atlas = new TextureAtlas("princess_and_monsters.pack");
        this.game = game;
        gamecamera = new OrthographicCamera();
        gamePort = new FillViewport(MyGame.WIDTH/MyGame.pxPerMeter , MyGame.Hieght/MyGame.pxPerMeter , gamecamera);
        hud = new Hud(game.batch);

        // settings the tiled map I created

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("maps/new_map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map , 1 / MyGame.pxPerMeter);


        gamecamera.position.set(gamePort.getWorldWidth() /2, gamePort.getWorldHeight() /2 , 0);
        float viewPortWidth = gamePort.getWorldWidth();
        float viewPortHeight = gamePort.getWorldHeight();
        gamecamera.position.x = MathUtils.clamp(gamecamera.position.x , viewPortWidth/2 , 400 - viewPortWidth/2);
        gamecamera.position.y = MathUtils.clamp(gamecamera.position.y , viewPortHeight/2 , 208 - viewPortHeight/2);

        // amount of physical gravity in the world
        world = new World(new Vector2(0,-10 ) , true);
        box2DDebugRenderer = new Box2DDebugRenderer();
        creator = new Creator(this);
        player = new Princess(this);
        world.setContactListener((ContactListener) new WorldContactListener());
        music = MyGame.assetManager.get("sounds/music.wav" , Music.class);
        music.setLooping(true); // so it will be played over and over agian
        music.play();
        box2DDebugRenderer.setDrawBodies(false);
    }

    public static void setWorldBounds(float width , float height){
        worldBounds = new Rectangle(0,0,width,height);
    }


    public TextureAtlas getAtlas(){
        return atlas;
    }


    @Override
    public void show() {

    }

    // new method to handle inputs from the user

    public void handleInput(float deltaTime){
        if(player.current != Princess.State.DEAD){
            if(Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
                player.jump();
            if ( Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.body.getLinearVelocity().x<=2)
                player.body.applyLinearImpulse(new Vector2(0.05f , 0) , player.body.getWorldCenter() ,true);
            if ( Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.body.getLinearVelocity().x>=-2)
                player.body.applyLinearImpulse(new Vector2(-0.05f , 0) , player.body.getWorldCenter() ,true);
        }

    }
    public void update(float deltaTime){
        handleInput(deltaTime);
        world.step(1/60f , 6 ,2);
        player.update(deltaTime);
        player.falling();
        for (Monsters monsters : creator.getMonsters())
            monsters.update(deltaTime);
        if(player.current != Princess.State.DEAD) {
            gamecamera.position.x = player.body.getPosition().x;
        }


        float viewPortWidth = gamePort.getWorldWidth();
        float viewPortHeight = gamePort.getWorldHeight();
        gamecamera.position.x = MathUtils.clamp(gamecamera.position.x , viewPortWidth/2 , 400 - viewPortWidth/2);
        gamecamera.position.y = MathUtils.clamp(gamecamera.position.y , viewPortHeight/2 , 208 - viewPortHeight/2);

        gamecamera.update();
        renderer.setView(gamecamera);
    }

    @Override
    public void render(float delta) {

        update(delta);

        // just to clear screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //to render the map
        renderer.render();

        // to render Box2D
        box2DDebugRenderer.render(world , gamecamera.combined);

        // to render the princces and monsters
        game.batch.setProjectionMatrix(gamecamera.combined);
        game.batch.begin();
        player.draw(game.batch);
        for (Monsters monsters : creator.getMonsters())
            monsters.draw(game.batch);
        game.batch.end();

        // to render the hud ( score )

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        if(gameOver()){
            game.setScreen(new GameOver(game));
            dispose();
        }
    }

    public TiledMap getMap(){
        return map;
    }
    public World getWorld(){
        return world;
    }

    public boolean gameOver(){
        if (player.current == Princess.State.DEAD && player.getStateTimer() > 2){
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        box2DDebugRenderer.dispose();
        hud.dispose();

    }
}

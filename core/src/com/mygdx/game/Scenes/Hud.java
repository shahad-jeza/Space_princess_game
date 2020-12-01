package com.mygdx.game.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGame;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import org.graalvm.compiler.phases.common.NodeCounterPhase;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;


import java.awt.*;

public class Hud implements Disposable {
    public Stage stage ;
    private Viewport viewport;
 // create the score varibals
    private static Integer score ;
    private Label scoreWord;


// creating the labels
    private static Label   scoreLabel;
    Label scoreWordLabel;


    public Hud(SpriteBatch sb){
        score = 0;
        viewport = new FitViewport(MyGame.WIDTH , MyGame.Hieght, new OrthographicCamera());
        stage = new Stage(viewport , sb);
        Table table = new Table();
        table.top();
        table.setFillParent(true);

        //assigning the initial values and styling
        scoreLabel = new Label(String.format("%06d" , score) , new Label.LabelStyle(new BitmapFont() , Color.WHITE));
        scoreWordLabel = new Label("SCORE" , new Label.LabelStyle(new BitmapFont() , Color.WHITE));

        // create a table to hold and display the labels
        table.add(scoreWordLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();



        stage.addActor(table);
    }

    public static void increaseScore(int num){  // a method to keep track of score
        score += num ;
        scoreLabel.setText(String.format("%06d" , score) );
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

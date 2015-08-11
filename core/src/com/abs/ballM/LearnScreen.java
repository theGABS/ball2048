package com.abs.ballM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import javax.xml.soap.Text;


/**
 * Created by k on 29.07.15.
 */
public class LearnScreen extends AbstractScreen {
    private float width, height;
    //private Stage stage = new Stage();
    private Table table = new Table();
    private I18NBundle myBundle;
    private Texture background = new Texture("fon22.png");
    private Texture howPlayTexture = new Texture("how_play.png");
//        private Skin skin = new Skin(Gdx.files.internal("skin/skin.json"),
//                new TextureAtlas(Gdx.files.internal("skin/pack.atlas")));

    //private Skin skin = new Skin();
    private TextButton buttonPlay;
    private SpriteBatch batch;
    private BitmapFont font;

    private OrthographicCamera fullCamera;

    public LearnScreen(){
        SLogger.log("constructor", this);
        batch = new SpriteBatch();
        fullCamera = new OrthographicCamera();
        myBundle = I18NBundle.createBundle(Gdx.files.internal("i18n/MyBundle"));
        String[] helloText = {myBundle.get("helloText1") , myBundle.get("helloText2") , myBundle.get("helloText3")};


//        BitmapFont font = MyGame.fontGeneration(Gdx.graphics.getWidth() / 30);
//        skin.addRegions(new TextureAtlas(Gdx.files.internal("skin/pack.atlas")));
//        skin.add("font", font, font.getClass());
//        skin.load(Gdx.files.internal("skin/skin.json"));

        table.setFillParent(true);
        //table.setWidth(200);
        table.setDebug(true);



//        Label label1 = new Label(helloText[0], skin);
//        Label label2 = new Label(helloText[1], skin);
//        Label label3 = new Label(helloText[2], skin);

        stage.clear();
        buttonPlay = new TextButton("Play", skin);
        //buttonPlay.setPosition(100, 100);
        buttonPlay.setWidth(Gdx.graphics.getWidth());
        buttonPlay.setHeight(Gdx.graphics.getWidth() / 3.7f);
        buttonPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.clickSound.play();
                game.setScreen(new GameScreen());
            }
        });
//        TextButton buttonHowPlay = new TextButton("How play", skin);
//        TextButton buttonShare = new TextButton("Share", skin);
//        TextButton buttonScore = new TextButton(highScore, skin);
//        table.add(buttonPlay).row();
//        table.add(buttonHowPlay).row();
//        table.add(buttonShare).row();
//        table.add(buttonScore).row();

//        table.add(label1).row();
//        table.add(label2).row();
//        table.add(label3).row();
        //table.add(buttonPlay).align(Align.bottom);


        stage.addActor(buttonPlay);
    }


    @Override
    public void render(float delta){

        //Gdx.input.setInputProcessor(stage);


        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, width, height);
        stage.getBatch().end();

        //super.render(delta);

        stage.act(delta);
        stage.draw();

        batch.begin();
        float padding = width * 0.1f;
        float bottom = height - font.drawWrapped(batch, myBundle.get("howPlay"), width * 0.02f, height * 0.98f, width * 0.96f).height - (width / 1.8f) - padding;
        batch.draw(howPlayTexture, 0, bottom, width, width / 1.8f);
        batch.end();
    }

    @Override
    public void resize(int width, int height){
        super.resize(width, height);
        this.width = width;
        this.height = height;
//        stage.setViewport(new ExtendViewport(width, height));
//        stage.getViewport().update(width, height, true);
        fullCamera.viewportWidth = width;
        fullCamera.viewportHeight = height;
        fullCamera.position.set(width / 2, -fullCamera.viewportHeight / 2f + height, 0);
        fullCamera.update();
        batch.setProjectionMatrix(fullCamera.combined);
        font = fontGeneration(width/25);
        font.setColor(Color.BLACK);
    }

}

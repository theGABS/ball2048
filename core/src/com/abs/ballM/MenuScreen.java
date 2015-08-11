package com.abs.ballM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

/**
 * Created by k on 31.07.15.
 */
class MenuScreen extends AbstractScreen {

    //private MyGame game;
    float aspectRatio;
    float colorValue = 0;
    float time;
    //MyGame game;
    String howStart = game.myBundle.get("howStart");
    String[] helloText = {game.myBundle.get("helloText1"), game.myBundle.get("helloText2"), game.myBundle.get("helloText3")};

    private Stage stage = new Stage();
    private Table table = new Table();
    private TextButton buttonPlay, buttonHowPlay, buttonShare, buttonScore;
//        private Skin skin = new Skin(Gdx.files.internal("skin/skin.json"),
//                new TextureAtlas(Gdx.files.internal("skin/pack.atlas")));

    String highScore;

    public SpriteBatch batch;


    public MenuScreen() {
//        this.myGame = myGame;
//        this.game = _game;
        //skin.add();

        batch = new SpriteBatch();


        float width, height;
        width = game.fullCamera.viewportWidth;
        height = game.fullCamera.viewportHeight;

        highScore = "highScore: " + MyGame.prefs.getInteger("highScore");

//        BitmapFont font = fontGeneration(Gdx.graphics.getWidth() / 13);
//        skin.addRegions(new TextureAtlas(Gdx.files.internal("skin/pack.atlas")));
//        skin.add("font", font, font.getClass());
//        skin.load(Gdx.files.internal("skin/skin.json"));

        table.setFillParent(true);
        table.setDebug(true);

        buttonPlay = new TextButton("Play", skin);

        aspectRatio = buttonPlay.getStyle().up.getMinWidth() / buttonPlay.getStyle().up.getMinHeight();
        //buttonPlay.setWidth(100);
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.clickSound.play();
                game.setScreen(new GameScreen());
            }
        });
        buttonHowPlay = new TextButton("How play", skin);
        buttonHowPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.clickSound.play();
                game.setScreen(new LearnScreen());
            }
        });
        buttonShare = new TextButton("Share", skin);
        buttonShare.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.clickSound.play();
                game.requestHandler.share();
            }
        });
        buttonScore = new TextButton(highScore, skin);

//            table.add(buttonPlay).width(width * 0.8f).height(width * 0.8f/aspectRatio).row();
//            table.add(buttonHowPlay).width(width * 0.8f).height(width * 0.8f / aspectRatio).row();
//            table.add(buttonShare).width(width * 0.8f).height(width * 0.8f/aspectRatio).row();
//            table.add(buttonScore).width(width * 0.8f).height(width * 0.8f / aspectRatio).row();

//            table.add(buttonPlay).row();
//            table.add(buttonHowPlay).row();
//            table.add(buttonShare).row();
//            table.add(buttonScore).row();

        //stage.addActor(table);

        float padding = 10;
        stage.addActor(buttonPlay);
        stage.addActor(buttonHowPlay);
        stage.addActor(buttonShare);
        stage.addActor(buttonScore);

        buttonPlay.setY(height - buttonPlay.getHeight() - padding);
        buttonHowPlay.setY(height - (buttonPlay.getHeight() - padding) * 2);
        buttonShare.setY(height - (buttonPlay.getHeight() - padding) * 3);
        buttonScore.setY(height - (buttonPlay.getHeight() - padding) * 4);


    }

    @Override
    public void render(float dt) {
        Gdx.input.setInputProcessor(stage);
        if (game.frame == 5) {
            game.loadHard();
        }
        game.frame++;
        //if(!startRender){ game.loadHard(); startRender = true;  }
        colorValue = (float) ((1 + Math.sin((time += dt) * 1.6f)) * 0.5f);


        batch.setProjectionMatrix(game.fullCamera.combined);
        batch.begin();
        batch.draw(game.background, 0, 0, game.fullCamera.viewportWidth, game.fullCamera.viewportHeight);


        //TODO in new libGDX supported font-shadow
//            for(int j = 0; j < 2; j++) {
//                float acm = fullCamera.viewportHeight*0.98f;
//                float sm = 2*(1-j);
//                font.setColor(new Color(j, j, j, 1));
//
//                for (int i = 0; i < 2; i++) {
//                    font.drawWrapped(batch, helloText[i], fullCamera.viewportWidth * 0.02f - sm , acm - sm, fullCamera.viewportWidth * 0.96f);
//                    acm -= font.getWrappedBounds(helloText[i], fullCamera.viewportWidth * 0.96f).height + 35;
//                    //batch.draw(line, fullCamera.viewportWidth * 0.02f, acm + 10, fullCamera.viewportWidth * 0.96f, 15);
//                }
//            }


//            font.setColor(colorValue, colorValue, colorValue, 1);
//            font.draw(batch, howStart, fullCamera.viewportWidth / 2 - font.getBounds(howStart).width / 2,
//                    fullCamera.viewportHeight*0.12f + font.getBounds(howStart).height / 2);
//
//            String highScore =  "highScore: " + prefs.getInteger("highScore");
//            font.draw(batch, highScore, fullCamera.viewportWidth / 2 - font.getBounds(highScore).width / 2, 40);

        batch.end();


        stage.act(dt);
        stage.draw();

//            if (Gdx.input.justTouched() && frame > 5) {
//                clickSound.play();
//                //game.setScreen(game.gameScreen);
//            }
    }


    @Override
    public void resize(int width, int height) {
        game.camera.viewportWidth = 480f;
        game.camera.viewportHeight = 480f * height / width;
        game.camera.position.set(game.camera.viewportWidth / 2f, game.camera.viewportHeight / 2f, 0); // show in top
        game.camera.update();

        game.fullCamera.viewportWidth = width;
        game.fullCamera.viewportHeight = height;
        game.fullCamera.position.set(game.fullCamera.viewportWidth / 2f, game.fullCamera.viewportHeight / 2f, 0);
        game.fullCamera.update();

        game.font = fontGeneration(width / 13);
//        BitmapFont skinFont = skin.getFont("font");
//        if(skinFont != null){
//            skinFont.dispose();
//            skinFont = game.font;
//        }
        stage.setViewport(new ExtendViewport(width, height));
        stage.getViewport().update(width, height, true);


        //table.setScale(0.5f);
//            table.getCells().toArray()[0].width(width * 0.8f).height(width * 0.8f / aspectRatio).row();
//            table.getCells().toArray()[1].width(width * 0.8f).height(width * 0.8f / aspectRatio).row();
//            table.getCells().toArray()[2].width(width * 0.8f).height(width * 0.8f / aspectRatio).row();
//            table.getCells().toArray()[3].width(width * 0.8f).height(width * 0.8f / aspectRatio).row();

        buttonPlay.setWidth(width * 0.8f);
        buttonPlay.setHeight(width * 0.8f / aspectRatio);

        buttonHowPlay.setWidth(width * 0.8f);
        buttonHowPlay.setHeight(width * 0.8f / aspectRatio);

        buttonShare.setWidth(width * 0.8f);
        buttonShare.setHeight(width * 0.8f / aspectRatio);

        buttonScore.setWidth(width * 0.8f);
        buttonScore.setHeight(width * 0.8f / aspectRatio);


        float padding = 10;
        buttonPlay.setY(height - (buttonPlay.getHeight() + padding));
        buttonHowPlay.setY(height - (buttonPlay.getHeight() + padding) * 2);
        buttonShare.setY(height - (buttonPlay.getHeight() + padding) * 3);
        buttonScore.setY(height - (buttonPlay.getHeight() + padding) * 4);

        buttonPlay.setX(width * 0.1f);
        buttonHowPlay.setX(width * 0.1f);
        buttonShare.setX(width * 0.1f);
        buttonScore.setX(width * 0.1f);
    }
}

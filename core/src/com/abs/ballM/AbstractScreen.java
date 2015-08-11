package com.abs.ballM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;


/**
 * Created by k on 31.07.15.
 */
public abstract class AbstractScreen implements Screen {

    //protected Stage displayStage;
    //protected AgarioGame game;
    //protected AssetManager assetManager;

    public static MyGame game;
    static Stage stage = new Stage();
    public static Skin skin;
    public static InputMultiplexer inputMultiplexer = new InputMultiplexer();

    public static BitmapFont fontGeneration(int size) {

        String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
        String RUSSIAN_CHARACTERS = "АБВГДЕЁЖЗИІЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
                + "абвгдеёжзиійклмнопрстуфхцчшщъыьэюя";

        BitmapFont font;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ClearSansBold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.characters = RUSSIAN_CHARACTERS + FONT_CHARACTERS;
        font = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        return font;

    }

    //protected List<Disposable> disposableList = new ArrayList<Disposable>();




    public void update(){
        //do nothing
    }


    @Override
    public void render(float delta){
        //stage.act(delta);
        //stage.draw();
    }



//    public static BitmapFont getOpponentFont(int size) {
//        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
//                Gdx.files.internal("fonts/ProximaNova-Bold.ttf"));
//        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        FileHandle file = Gdx.files.internal("russian.txt");
//        parameter.characters = file.readString("windows-1251");;
//        parameter.size = size;
//        parameter.color = Color.BLACK;
//        BitmapFont font = generator.generateFont(parameter);
//        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//        return font;
//    }

//    protected void setDisplayStage(Stage stage) {
//        displayStage = stage;
//        Gdx.input.setInputProcessor(stage);
//    }

    @Override
    public void show() {
//        if(stage != null){
//            stage.dispose();
//            Gdx.app.log("stage", "stage dispose, in AbstractScreen");
//        }
//
////        if(skin != null){
////            skin.dispose();
////            skin = new
////        }
//        Gdx.app.log("stage", "stage = new Stage(), in AbstractScreen");
//        stage = new Stage();



        //InputProcessor inputProcessorOne = stage;
        //InputProcessor inputProcessorTwo = new CustomInputProcessorTwo();

        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void resize(int width, int height) {
        stage.setViewport(new ExtendViewport(width, height));
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
//        if(stage != null) {
//            stage.clear();
//            Gdx.app.log("stage", "stage clean in abstractScreen, class" + this.getClass().toString());
//        }else{
//            stage = new Stage();
//        }
    }

    @Override
    public void dispose() {

    }

    ///////////////////////////////////////////////////////

}
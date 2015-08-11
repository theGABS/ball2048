package com.abs.ballM;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Random;



public class MyGame extends Game implements ApplicationListener {

//    static BitmapFont fontGeneration(float inputSize) {
//        int size = (int) inputSize;
//        Gdx.app.log("", "size :" + size);
//        String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
//                + "0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
//        String RUSSIAN_CHARACTERS = "АБВГДЕЁЖЗИІЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
//                + "абвгдеёжзиійклмнопрстуфхцчшщъыьэюя";
//
//        BitmapFont font;
//        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ClearSansBold.ttf"));
//        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        parameter.size = size;
//        parameter.characters = RUSSIAN_CHARACTERS + FONT_CHARACTERS;
//        font = generator.generateFont(parameter);
//        generator.dispose(); // don't forget to dispose to avoid memory leaks!
//        return font;
//
//    }


    BitmapFont         font;
    OrthographicCamera camera, fullCamera;
    MenuScreen         menuScreen;
    GameScreen         gameScreen;
    I18NBundle myBundle;
    Texture background;
    Texture blackGradient;
    int                frame = 0;
    Random rn =        new Random();
    Sound clickSound;
    Sound goodSound;
    public static Preferences prefs;

    public interface RequestHandler {
        void confirm(ConfirmInterface confirmInterface);
        void loadAds();
        void showAds();
        void share();
    }

    public interface ConfirmInterface {
        public void yes();
        public void no();
        public void showLibGDX();
    }

    public void showConfirmDialog() {
        Gdx.app.log("bitch", "showConfircDialog core");
        requestHandler.confirm(new ConfirmInterface() {
            @Override
            public void yes() {
                Gdx.app.exit();
            }

            @Override
            public void no() {
                // The user clicked no! Do nothing
            }

            @Override
            public void showLibGDX() {

            }
        });
    }

    RequestHandler requestHandler;

    public MyGame(RequestHandler requestHandler)
    {
        this.requestHandler = requestHandler;
    }

    public void loadAds(){
        requestHandler.loadAds();
    }

    public void showAds(){
        requestHandler.showAds();
    }

    public void loadHard(){
//        try {
//            Thread.sleep(1000);                 //1000 milliseconds is one second.
//        } catch(InterruptedException ex) {
//            Thread.currentThread().interrupt();
//        }
        //gameScreen = new GameScreen();
        //loadAds();
    }

    @Override
    public void create() {
        AbstractScreen.game = this;
        myBundle =       I18NBundle.createBundle(Gdx.files.internal("i18n/MyBundle"));
        background =     new Texture("fon22.png");
        blackGradient =  new Texture("black_gradient.png");
        camera =         new OrthographicCamera();
        fullCamera =     new OrthographicCamera();
        prefs =          Gdx.app.getPreferences("Ball2048");
        if (!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
        }

        AbstractScreen.skin = new Skin();
        BitmapFont font = AbstractScreen.fontGeneration(Gdx.graphics.getWidth() / 13);
        AbstractScreen.skin.addRegions(new TextureAtlas(Gdx.files.internal("skin/pack.atlas")));
        AbstractScreen.skin.add("font", font, font.getClass());
        AbstractScreen.skin.load(Gdx.files.internal("skin/skin.json"));


        menuScreen =     new MenuScreen();
        setScreen(menuScreen);

        clickSound = Gdx.audio.newSound(Gdx.files.internal("click.mp3"));
        goodSound = Gdx.audio.newSound(Gdx.files.internal("good.mp3"));


    }



}


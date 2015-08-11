package com.abs.ballM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import java.util.regex.Pattern;

/**
 * Created by k on 31.07.15.
 */
class GameScreen extends AbstractScreen implements InputProcessor {

    enum State{
        PAUSE,
        RUN,
        GAME_OVER
    }

    class Ball {
        Body body;
        int color;
        float time;


        public Ball(int color) {
            this();
            this.color = color;
        }

        public Ball() {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;

            CircleShape dynamicCircle = new CircleShape();
            dynamicCircle.setRadius(BALL_SIZE / 10f / 2f);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = dynamicCircle;
            fixtureDef.density = 1.0f;
            fixtureDef.friction = 0.1f;
            fixtureDef.restitution = 0.22f;

            bodyDef.position.set(Vector2.Zero); // center top screen
            body = world.createBody(bodyDef);

            body.createFixture(fixtureDef);
            //body.setLinearVelocity(velocity);
        }

        public void draw(SpriteBatch batch){
            float scale = 1.05f;
            //Texture useTexture = ballTexture[color];
            Texture useTexture = getBallTexture(color);
            batch.draw(useTexture, body.getPosition().x * 10 - BALL_SIZE / 2f * scale,
                    body.getPosition().y * 10 - BALL_SIZE / 2f * scale, BALL_SIZE * scale, BALL_SIZE * scale);
        }
    }




    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        Gdx.app.log("bitch", "keycode = " + keycode);
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            switch (state) {
                case PAUSE:
                    game.showConfirmDialog();
                    break;
                case RUN:
                    state = State.PAUSE;
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        mouse.x = screenX * 480f / Gdx.graphics.getWidth();
        mouse.y = screenY * 480f * Gdx.graphics.getHeight() / Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        return false;
    }

    @Override
    public void show() {
        //inputMultiplexer.addProcessor(this);
        //Gdx.input.setInputProcessor(inputMultiplexer);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void pause() {
        int highScore = game.prefs.getInteger("highScore");
        if (score > highScore) {
            game.prefs.putInteger("highScore", score);

        }
        String ballSave = "";
        for (int i = 0; i < balls.size; i++) {
            ballSave += balls.get(i).body.getPosition().x + ',' +
                    balls.get(i).body.getPosition().y + ',' + balls.get(i).color;
            if (i < balls.size - 1) {
                ballSave += '|';
            }
        }
        game.prefs.putString("ball", ballSave);
        Gdx.app.log("tt", ballSave);
        game.prefs.flush();
    }

    public void loadGame() {
        try {
            String ballLoad = game.prefs.getString("ball");
            String[] parts = ballLoad.split(Pattern.quote("|"));
            for (int i = 0; i < parts.length; i++) {
                String[] param = parts[i].split(Pattern.quote(","));
                Ball tmpBall = new Ball();
                tmpBall.body.setTransform(new Vector2(Float.parseFloat(param[0]), Float.parseFloat(param[1])), 0);
                tmpBall.color = Integer.parseInt(param[2]);
                balls.add(tmpBall);
            }
        } catch (Exception e) {

        }
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        mouse.x = screenX * 480f / Gdx.graphics.getWidth();
        mouse.y = screenY * 480f * Gdx.graphics.getHeight() / Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        switch (state) {
            case PAUSE:
                state = State.RUN;
                break;
            case RUN:
//                if (timePassed < 0.3) {
//                    //if the user did not manage to shoot - adjust last
//                    if (balls.size != 0) {
//                        balls.get(balls.size - 1).body.setLinearVelocity(new Vector2(screenX - Gdx.graphics.getWidth() / 2f, -screenY).scl(0.125f * 480 / Gdx.graphics.getHeight()));
//                    }
//                }
                readyForShot = true;
        }
        return true;
    }

    void shotBall() {
        float xcor = -36 + 1.1f * BALL_SIZE * (2.5f + timePassed / timeShot);
        xcor = 240;
        shotBall(new Vector2(mouse.x - xcor, -mouse.y).scl(0.125f * 480 / 800f));
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (readyForShot) {
            final float waitBeforeShot = 0.2f;
            if (timePassed < waitBeforeShot) {
                //if the user did not manage to shoot - adjust last
                if (balls.size != 0) {
                    balls.get(balls.size - 1).body.setLinearVelocity(new Vector2(screenX - Gdx.graphics.getWidth() / 2f, -screenY).scl(0.125f * 480 / Gdx.graphics.getHeight()));
                }
            } else {
                shotBall();
                timePassed = 0;
                firstShot = false;
            }
        }
        readyForShot = false;
        return true;
    }


    class BallEffect {
        float x, y;
        float startX, startY;
        float endX, endY;
        int color;
        float timeShow;

        BallEffect(Vector2 vec, int color) {
            this.startX = vec.x;
            this.startY = vec.y;
            this.color = color;
            timeShow = game.rn.nextInt(300) / 1000f;
        }

        BallEffect(Ball ball, Ball endBall) {
            this(ball.body.getPosition(), ball.color);
            this.endX = endBall.body.getPosition().x;
            this.endY = endBall.body.getPosition().y;
        }

        void move() {
            x = startX + (endX - startX) * timeShow;
            y = startY + (endY - startY) * timeShow;
        }
    }

    Vector2 mouse = new Vector2();
    Texture floor, arrow;
    //Texture line = new Texture("line2.png");
    ShapeRenderer shapeRenderer;
    Array<BallEffect> effects = new Array<BallEffect>();
    FPSLogger fpsLogger = new FPSLogger();
    Array<Ball> balls = new Array<Ball>();
    Texture[] tBall = new Texture[6];
    Texture[] ballTexture = new Texture[6];
    Graph graphBall = new Graph();
    World world = new World(new Vector2(0, -20), true);
    String howContinue;
    boolean firstShot = true;
    boolean readyForShot = false;
    Sound shotSound = Gdx.audio.newSound(Gdx.files.internal("33276__mastafx__shot.wav")); // TODO rename
    Sound fonMusic;
    boolean musicLoad = false;
    SpriteBatch batch;

    State state = State.RUN;
    final int BOX_VELOCITY_ITERATIONS = 6;
    final int BOX_POSITION_ITERATIONS = 3;
    final int MAX_BALL = 40;
    final int COLOR_COUNT = 6;
    final float BALL_SIZE = 480 / 5f;
    final float BALL_SIZE_DIV_TEN_SQR = (float) Math.pow(BALL_SIZE, 2) / 100f;
    int frame = 0;
    int score = 0;
    int level = 0;
    int[] nextColor = new int[3];
    final float timeShot = 0.4f;
    float timePassed;
    float time;


    //AssetManager assetManager = new AssetManager();

//        Color[] ballColor = {
//                new Color(0.3f, 1, 0.3f, 1),  // green
//                new Color(0.2f, 0.2f, 1, 1), // blue ?
//                new Color(0.3f, 1, 1, 1),        // cuan
//                new Color(0.9f, 0.25f, 1, 1), // purpur
//                new Color(1f, 1f, 0.2f, 1),   // yellow
//                new Color(1, 0.1f, 0.2f, 1),  // red
//        };


//    Color[] ballColor = {
//            new Color(0.93f, 0.89f, 0.854f, 1),  // 1
//            new Color(0.95f, 0.87f, 0.78f, 1),   // 4
//            new Color(0.94f, 0.64f, 0.49f, 1),   // 16
//            new Color(0.96f, 0.54f, 0.4f, 1),    // 64
//            new Color(0.96f, 0.40f, 0.3f, 1),    // 256
//            new Color(0.98f, 0.35f, 0.25f, 1),   // 1024
//            new Color(1f, 0.28f, 0.18f, 1),      // 4096
//            new Color(1f, 0.20f, 0.10f, 1),      // 16k
//            new Color(1f, 0.28f, 0.18f, 1),      // 64k
//            new Color(1f, 0.28f, 0.18f, 1),      // 256k
//            new Color(1f, 0.28f, 0.18f, 1),      // 1kk
//            new Color(1f, 0.28f, 0.18f, 1),      // 4kkk
//    };

    Color[] ballColor = {
            new Color(0.93f, 0.89f, 0.5f, 1),  // 1
            new Color(0.94f, 0.75f, 0.5f, 1),   // 4
            new Color(0.94f, 0.64f, 0.49f, 1),   // 16
            new Color(0.96f, 0.54f, 0.4f, 1),    // 64
            new Color(0.96f, 0.40f, 0.3f, 1),    // 256
            new Color(0.98f, 0.35f, 0.25f, 1),   // 1024
            new Color(1f, 0.28f, 0.18f, 1),      // 4096
            new Color(1f, 0.20f, 0.10f, 1),      // 16k
            new Color(1f, 0.28f, 0.18f, 1),      // 64k
            new Color(1f, 0.28f, 0.18f, 1),      // 256k
            new Color(1f, 0.28f, 0.18f, 1),      // 1kk
            new Color(1f, 0.28f, 0.18f, 1),      // 4kkk
    };



    Color getBallColor(int number){
        return ballColor[number];
    }

    Texture getBallTexture(int number){
        if(number < 6){
            return ballTexture[number];
        }else{
            return ballTexture[5];
        }
    }


    public void createBall(Vector2 velocity, Vector2 position) {
        Ball tmpBall = new Ball(getNextColor());
        tmpBall.body.setTransform(position, 0);
        tmpBall.body.setLinearVelocity(velocity);

        balls.add(tmpBall);
        timePassed = 0;
        readyForShot = false;
    }

    public void createBall(Vector2 velocity) {
        float xcor = -36 + 1.1f * BALL_SIZE * (2.5f + timePassed / timeShot);
        xcor = 240;
        createBall(velocity, new Vector2(xcor / 10f, game.camera.viewportHeight / 10f));
    }

    public void shotBall(Vector2 velocity) {
        createBall(velocity);
        shotSound.play(0.2f);
    }

    public int getNextColor() { // actually its not color, its colorIndex
        int tmp = nextColor[2];
        nextColor[2] = nextColor[1];
        nextColor[1] = nextColor[0];
        nextColor[0] = game.rn.nextInt(3);
        return tmp;
    }

    public GameScreen() {
        //this.game = game;
//            assetManager.load("fonmusic.ogg", Sound.class);
//
//            if (!musicLoad && assetManager.isLoaded("fonmusic.ogg")){
//                fonMusic = assetManager.get("fonmusic.ogg", Sound.class);
//                long soundId = fonMusic.play(0.2f);
//                fonMusic.setLooping(soundId, true);
//                musicLoad = true;
//                Gdx.app.log("", "musicLoad");
//            }
        //assetManager.finishLoading(); //Important!


        Gdx.input.setCatchBackKey(true);

//        TextButton button = new TextButton("test", skin);
//        stage.addActor(button);

        stage.clear();
        TextButton buttonPlay = new TextButton("Play again", skin);
        //buttonPlay.setPosition(100, 100);
        buttonPlay.setWidth(Gdx.graphics.getWidth());
        buttonPlay.setHeight(Gdx.graphics.getWidth() / 3.7f);
        buttonPlay.setY(Gdx.graphics.getHeight() / 2 );
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.clickSound.play();
                newGame(false);
                //game.setScreen(new GameScreen());
            }
        });

        TextButton buttonToMenu = new TextButton("Menu", skin);
        //buttonPlay.setPosition(100, 100);
        buttonToMenu.setWidth(Gdx.graphics.getWidth());
        buttonToMenu.setHeight(Gdx.graphics.getWidth() / 3.7f);
        buttonToMenu.setY(Gdx.graphics.getHeight() / 2 + buttonToMenu.getHeight());
        buttonToMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.clickSound.play();
                //newGame(false);
                game.setScreen(new MenuScreen());
            }
        });

        stage.addActor(buttonPlay);
        stage.addActor(buttonToMenu);
        //game = _game;
        shapeRenderer = new ShapeRenderer();
        batch =         new SpriteBatch();


        //line.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

//        String suf = "4";
//        tBall[0] = new Texture("ballgreen" + suf + ".png"); // BLUE todo rename
//        tBall[1] = new Texture("ball" + suf + ".png");
//        tBall[2] = new Texture("ballcyan" + suf + ".png");
//        tBall[3] = new Texture("ballpurple" + suf + ".png");
//        tBall[4] = new Texture("ballyellow" + suf + ".png");
//        tBall[5] = new Texture("ballred" + suf + ".png");
        //floor = new Texture("floor11.jpg");
        arrow = new Texture("arrow.png"); //alreay its just "finger-touch", but not arrow
        arrow.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);


        ballTexture[0] = new Texture("ball_yellow.png");
        ballTexture[1] = new Texture("ball_orange.png");
        ballTexture[2] = new Texture("ball_red1.png");
        ballTexture[3] = new Texture("ball_red2.png");
        ballTexture[4] = new Texture("ball_red3.png");
        ballTexture[5] = new Texture("ball_blue.png");


        //Gdx.input.setInputProcessor(this);

        BodyDef groundBodyDef = new BodyDef();

        groundBodyDef.position.set(new Vector2(0, 0));
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox((48.0f) * 2, 0.0f);
        world.createBody(groundBodyDef).createFixture(groundBox, 0.0f);

        groundBodyDef.position.set(new Vector2(0, 0));
        groundBox.setAsBox(0, 100.0f);
        world.createBody(groundBodyDef).createFixture(groundBox, 0.0f);

        groundBodyDef.position.set(new Vector2(48, 0));
        groundBox.setAsBox(0, 100.0f);
        world.createBody(groundBodyDef).createFixture(groundBox, 0.0f);


        Gdx.app.log("t44", game.prefs.getString("ball"));
//            if(!prefs.getString("ball").isEmpty()){
//                loadSaveGame();
//            }else {
//                newGame(true);
//            }

        newGame(true);
        howContinue = game.myBundle.get("howToContinue");
    }

    void newGame(boolean first) {
        Gdx.input.setInputProcessor(this);
        //score = level * 100;
        score = 0;
        state = State.RUN;

        if (!first) {
            //game.showAds();
        }

        for (int i = balls.size - 1; i >= 0; i--) {
            world.destroyBody(balls.get(i).body);
            balls.removeIndex(i);
        }

        float[][] startBallC = new float[14][2];
        for (int i = 0; i < 10; i++) {
            startBallC[i][0] = BALL_SIZE / 20 + (i / 2) * 2f * BALL_SIZE / 20f;
            startBallC[i][1] = BALL_SIZE / 15f + BALL_SIZE / 10f * (i % 2);
        }
        int[] startBallColor = new int[14];
        for (int i = 0; i < 14; i++) {
            startBallColor[i] = i % 3;
        }
        startBallColor[9] = 0;
        startBallColor[8] = 0;
        startBallColor[6] = 0;

        //startBallColor[5] = 1;


        for (int i = 0; i < 14; i++) {
            nextColor[2] = startBallColor[i];
            createBall(new Vector2(game.rn.nextInt(11) - 5, -30), new Vector2(startBallC[i][0], startBallC[i][1]));
        }
        nextColor[2] = 0;

    }

    void physicalWork(float dt) {

        if (frame % 1 == 0) {
            graphBall.createGraph(balls.size);

            for (int i = 0; i < balls.size; i++) {
                Ball ballI = balls.get(i);
                for (int j = 0; j < balls.size; j++) {
                    Ball ballJ = balls.get(j);
                    if (i != j && ballI.color == ballJ.color) {
                        Vector2 jv = ballI.body.getPosition();
                        Vector2 iv = ballJ.body.getPosition();
                        if (jv.dst2(iv) * 0.88f < BALL_SIZE_DIV_TEN_SQR) { // 0.88 This means that between the balls may be a little big distance
                            graphBall.addEdge(i, j);
                        }
                    }
                }
            }

            Array<Array<Integer>> tmp = graphBall.searchGroups();


            //for (Array<Integer> group : tmp) {
            if (tmp.size > 0) {
                game.goodSound.play();
                score += Math.pow(4, balls.get(tmp.get(0).get(0)).color + 1);
                Array<Integer> group = tmp.get(0);
                Gdx.app.log("f", group.toString());
                Gdx.app.log("f", Integer.toString(balls.size));
                for (int i = 3; i >= 1; i--) {
                    int index = group.get(i);
                    effects.add(new BallEffect(balls.get(index), balls.get(group.get(0))));
                    world.destroyBody(balls.get(index).body);
                    balls.removeIndex(index);
                }
                balls.get(group.get(0)).color++;
            }


            //}

        }

        world.step(dt, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);
    }

    @Override
    public void render(float dt) {


//            Gdx.app.log("", Boolean.toString(musicLoad) + Boolean.toString(assetManager.isLoaded("fonmusic.ogg")));
//            if (!musicLoad && assetManager.isLoaded("fonmusic.ogg")){
//                fonMusic = assetManager.get("fonmusic.ogg", Sound.class);
//                long soundId = fonMusic.play(0.2f);
//                fonMusic.setLooping(soundId, true);
//                musicLoad = true;
//                Gdx.app.log("", "musicLoad");
//            }

//            if(frame == 20){
//                long soundId = fonMusic.play(0.2f);
//                fonMusic.setLooping(soundId, true);
//            }
        //Gdx.app.log("dt", Float.toString(dt));
        frame++;
        fpsLogger.log();
        //level = score / 100.0f;
        //timeShot = (float) (1.8 * Math.pow(2.7, -(score / 100.0f) / 15) + 0.4 + balls.size / (float) MAX_BALL);
        game.font.setColor(1, 1, 1, 1);


//            batch.setProjectionMatrix(fullCamera.combined);
//            batch.begin();
//            batch.draw(floor, 0, Gdx.graphics.getHeight() -  Gdx.graphics.getWidth()*1.33f -Gdx.graphics.getWidth() , Gdx.graphics.getWidth(), Gdx.graphics.getWidth()* floor.getHeight()/ floor.getWidth());
//            batch.end();

        batch.setProjectionMatrix(game.camera.combined);
        batch.enableBlending();
        batch.begin();

        batch.setColor(1, 1, 1, 1);
//            batch.setColor(1,1,1,0.1f);
//            batch.draw(background, rn.nextInt(200) - 300, rn.nextInt(200) - 300, 480 + rn.nextInt(500), 640 + rn.nextInt(500));
        batch.draw(game.background, 0, 0, 480, game.camera.viewportHeight);


        batch.setColor(1, 1, 1, 1);


        float scale = 1.15f;
        for (Ball ball : balls) {

//                batch.draw(tBall[ball.color % COLOR_COUNT],ball.body.getPosition().x*10 - BALL_SIZE/2f*scale,
//                        ball.body.getPosition().y*10 - BALL_SIZE/2f*scale , BALL_SIZE*scale, BALL_SIZE*scale);

            //Color color = new Color(1 - ball.color * 0.2f, 1 - ball.color * 0.2f, 1 - ball.color * 0.2f, 1);
//            batch.setColor(ballColor[ball.color]);
//            batch.draw(tBall[0], ball.body.getPosition().x * 10 - BALL_SIZE / 2f * scale,
//                    ball.body.getPosition().y * 10 - BALL_SIZE / 2f * scale, BALL_SIZE * scale, BALL_SIZE * scale);
//            batch.setColor(Color.WHITE);

            ball.draw(batch);


//                for(int j = 0; j < 4; j+=2) {
//                    font.setColor(new Color(j, j, j, 1));
//                    font.draw(batch, str , ball.body.getPosition().x*10 - font.getBounds(str).width/2 -j, ball.body.getPosition().y*10 + font.getBounds(str).height/2 +j);
//                }
        }


        for (int i = 0; i < 3; i++) {
            //batch.setColor(ballColor[nextColor[i]]);
            if(i != 2){ batch.setColor(0.5f, 0.5f, 0.5f, 1);}else{
                batch.setColor(0.5f + 0.5f * timePassed / timeShot, 0.5f + 0.5f * timePassed / timeShot, 0.5f + 0.5f * timePassed / timeShot, 1);
            }
            float smallBallScale = scale * 0.5f;
            batch.draw(getBallTexture(nextColor[i]), -36 + 0.7f * BALL_SIZE * (i + timePassed / timeShot), game.camera.viewportHeight - BALL_SIZE / 1.7f, BALL_SIZE * smallBallScale, BALL_SIZE * smallBallScale);
            //String str = Integer.toString((int)Math.pow(4,nextColor[i]));
            //font.draw(batch, str , -36 + 1.1f*BALL_SIZE*(i + timePassed/timeShot) + BALL_SIZE/2f*scale - font.getBounds(str).width/2, camera.viewportHeight  + font.getBounds(str).height/2 - 15);

        }


        //Gdx.app.log("t", "fr");
        for (int i = effects.size - 1; i >= 0; i--) {
            BallEffect effect = effects.get(i);
            effect.timeShow += dt * 3; // TODO maybe its shitcode
            scale = 1.25f - effect.timeShow * 0.1f;

            batch.setColor(1, 1, 1, 1 - effect.timeShow);
            batch.draw(getBallTexture(effect.color), effect.x * 10 - BALL_SIZE * scale / 2f, effect.y * 10 - BALL_SIZE * scale / 2f, BALL_SIZE * scale, BALL_SIZE * scale);

            batch.setColor(1, 1, 1, effect.timeShow / 2f);
            batch.draw(getBallTexture(effect.color + 1), effect.x * 10 - BALL_SIZE * scale / 2f, effect.y * 10 - BALL_SIZE * scale / 2f, BALL_SIZE * scale, BALL_SIZE * scale);

//                for(int j = balls.size - 1; j >= 0; j--){
//                    if(balls.get(j).color != effect.color) continue;
//                    Vector2 jv = balls.get(j).body.getPosition();
//                    if( jv.dst2(effect.x, effect.y) < Math.pow(BALL_SIZE*0.05*(1+scale),2)  ){
//                        effects.add(new BallEffect(balls.get(j)));
//                        world.destroyBody(balls.get(j).body);
//                        balls.removeIndex(j);
//                    }
//                }

            effects.get(i).move();
            if (effects.get(i).timeShow > 0.8) {
                effects.removeIndex(i);
            }


        }

        if (firstShot && state.equals(State.RUN)) {

            batch.draw(arrow, 320, 100, 40, 150, arrow.getWidth(), arrow.getHeight(), 1, 1,
                    (float) (25 + Math.sin(frame * 0.07f) * 10f), 0, 0,
                    arrow.getWidth(), arrow.getHeight(), false, false);
        }

        if (readyForShot) {
            float xcor = -36 + 1.1f * BALL_SIZE * (2.5f + timePassed / timeShot);
            Vector2 v = new Vector2(0 - mouse.y, mouse.x - xcor);
            float degrees = -(float) (180 + (Math.atan2((0 - mouse.y), xcor - mouse.x) * 180.0d / Math.PI));
            //
            // batch.draw(line, xcor, game.camera.viewportHeight, 0, 0, v.len(), 15, 1, 1, degrees, 0, 0, line.getWidth(), line.getHeight(), false, false);
        }

        batch.setColor(1,1,1, balls.size / (float) MAX_BALL);
        batch.draw(game.blackGradient, 0,0,480, 100);
        batch.setColor(Color.WHITE);

        batch.end();


//        shapeRenderer.setProjectionMatrix(game.camera.combined);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//
//        Gdx.gl.glEnable(GL20.GL_BLEND);
//        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//
//        float opacity = 0.05f + 0.1f * timePassed / timeShot;
//        shapeRenderer.setColor(1, 1, 1, 0.05f);
//
//
//        shapeRenderer.end();



        float fullWidth = Math.min(480 * game.fullCamera.viewportHeight / 640, game.fullCamera.viewportWidth);

        batch.setProjectionMatrix(game.fullCamera.combined);
        batch.begin();

        float cameraToFull = game.fullCamera.viewportWidth / (float) game.camera.viewportWidth;

        for (Ball ball : balls) {

            String str = Integer.toString((int) Math.pow(4, ball.color));
            for (int j = 0; j < 4; j += 2) {
                //game.font.setColor(new Color(j, j, j, 1));
                game.font.setColor(new Color(j, j, j, 1));
                game.font.draw(batch, str, (ball.body.getPosition().x * 10) * cameraToFull - game.font.getBounds(str).width / 2 - j, (ball.body.getPosition().y * 10) * cameraToFull + game.font.getBounds(str).height / 2 - j);
            }
        }


        String textScore = "score: " + score + "  ";
        String textLeft = "  balls " + balls.size + " / " + MAX_BALL + " ";

        for (int j = 0; j < 2; j++) {
            game.font.setColor(new Color(j, j, j, 1));
            game.font.draw(batch, textScore, fullWidth - game.font.getBounds(textScore).width - j, game.fullCamera.viewportHeight * 0.983f - j);
            game.font.draw(batch, textLeft, fullWidth - game.font.getBounds(textLeft).width - j, game.font.getBounds(textLeft).height + game.fullCamera.viewportHeight * 0.024f - j);
        }
//        Gdx.app.log("", "timePassed / timeShot :" + timePassed / timeShot);
//        Gdx.app.log("", "timePassed  :" + timePassed);
//        Gdx.app.log("", "timeShot  :" + timeShot);
        for (int i = 0; i < 3; i++) {
            //batch.draw(tBall[nextColor[i]], -36 + 1.1f * BALL_SIZE * (i + timePassed / timeShot), camera.viewportHeight - BALL_SIZE / 2, BALL_SIZE * scale, BALL_SIZE * scale);
            String str = Integer.toString((int) Math.pow(4, nextColor[i]));
            game.font.setScale(0.5f);

            game.font.draw(batch, str, (-36 + 0.7f * BALL_SIZE * (i + timePassed / timeShot) + BALL_SIZE / 4 * scale) * cameraToFull - game.font.getBounds(str).width / 2, (game.camera.viewportHeight - 30) * cameraToFull + game.font.getBounds(str).height / 2);
            game.font.setScale(1);
        }


//            String textLevel = "  level: " + Integer.toString(level+1);
//            font.draw(batch, textLevel, 0 , font.getBounds(textLevel).height*1.1f);

        switch (state) {
            case RUN:
                physicalWork(dt);
                if (!firstShot) {
                    //if ((timePassed += dt) > timeShot) shotBall();
                    timePassed += dt;
                    if(timePassed > 0.4f){timePassed = 0.4f;}
                    //if (balls.size > MAX_BALL) newGame(false);
                    if(balls.size > 15) { // TODO not MAX_BALL/2 must be MAX_BALL
                        state = State.GAME_OVER;
                        Animation.add("gameOverBlack", 1, 0.4f, 700, new Animation.Sin());
                        Animation.add("gameOverOpacity", 0, 0.85f, 700, new Animation.Sin());
                        //Animation.add("gameOverBlack", 1, 0.4f, 600, new Animation.Linear());
                        //Animation.add("gameOverOpac", 1, 0.4f, 1);
                    }
                } else {
                    timePassed = 0.4f;
                }
                break;
            case PAUSE:
                float colorValue = (float) ((1 + Math.sin((time += dt) * 1.6f)) * 0.5f);
                game.font.setColor(colorValue, colorValue, colorValue, 1);
                batch.setColor(1, 1, 1, 0.7f);
                batch.draw(game.background, 0, 0, game.fullCamera.viewportWidth, game.fullCamera.viewportHeight);
                game.font.draw(batch, howContinue, fullWidth / 2 - game.font.getBounds(howContinue).width / 2,
                        game.fullCamera.viewportHeight / 2 + game.font.getBounds(howContinue).height / 2);
                break;
            case GAME_OVER:
                Gdx.input.setInputProcessor(inputMultiplexer);
                float lightness = Animation.get("gameOverBlack");
                batch.setColor(lightness, lightness, lightness, Animation.get("gameOverOpacity"));
                batch.draw(game.background, 0, 0, game.fullCamera.viewportWidth, game.fullCamera.viewportHeight);
        }

        //Gdx.gl.glBlendFunc(GL20.GL_ONE, GL20.GL_DST_COLOR);
        //batch.draw(light,0,0);

        batch.end();
        batch.setColor(1, 1, 1, 1f);

        if(state == State.GAME_OVER){
            stage.act();
            stage.draw();
        }


        //super.render(dt);

        // System.out.println(json.prettyPrint(balls));
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log("resize", "width: " + width + " height: " + height);
        float aspectRatio = (float) width / (float) height;

//            if(width * 640 > height * 480){
//                camera.viewportWidth = 480f *width/height *640/480;
//                camera.viewportHeight = 640f;
//                camera.position.set(240,  camera.viewportHeight/2 , 0);
//                camera.update();
//            }else {
//                camera.viewportWidth = 480f;
//                camera.viewportHeight = 640f * height / width * 480 / 640;
//                camera.position.set(camera.viewportWidth / 2f,  - camera.viewportHeight / 2f + 640 , 0);
//                camera.update();
//            }

        game.camera.viewportWidth = 480f;
        game.camera.viewportHeight = 480f / aspectRatio;
        game.camera.position.set(game.camera.viewportWidth / 2f, -game.camera.viewportHeight / 2f + game.camera.viewportHeight, 0);
        game.camera.update();

        int fontSize;
        if (width * 640 > height * 480) {
            game.fullCamera.viewportWidth = height * aspectRatio;
            game.fullCamera.viewportHeight = height;
            game.fullCamera.position.set(240 * height / 640, 0 - game.fullCamera.viewportHeight / 2f + height, 0);
            game.fullCamera.update();
            fontSize = (int) (height / 12 * (480 / 640f));
            //game.font = game.fontGeneration((int) (height / 12 * (480 / 640f)));
        } else {
            game.fullCamera.viewportWidth = width;
            game.fullCamera.viewportHeight = 640f * height / width * width / 640;
            game.fullCamera.position.set(game.fullCamera.viewportWidth / 2f, 0 - game.fullCamera.viewportHeight / 2f + height, 0);
            game.fullCamera.update();
            fontSize = width / 13;
        }
        Gdx.app.log("resize", "fontSize: " + fontSize);
        if(game.font != null){game.font.dispose();}
        game.font = fontGeneration(fontSize);

    }
}

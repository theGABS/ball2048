package com.abs.ballM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
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
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;
import java.util.regex.Pattern;

/**
 * Created by k on 31.07.15.
 */
class GameScreen extends AbstractScreen{
    Vector2 mouse = new Vector2();
    Texture arrow = new Texture("arrow.png");  //already its just "finger-touch", but not arrow
    Texture line = new Texture("line2.png");
    Texture[] ballTexture = new Texture[8];

    SpriteBatch batch = new SpriteBatch();

    Array<Ball> balls = new Array<Ball>();
    Array<BallEffect> effects = new Array<BallEffect>();

    FPSLogger fpsLogger = new FPSLogger();


    Graph graphBall = new Graph();
    World world = new World(new Vector2(0, -20), true);

    boolean firstShot = true;
    boolean readyForShot = false;


    Sound shotSound = Gdx.audio.newSound(Gdx.files.internal("shot.mp3"));

    InputHandler inputHandler = new InputHandler(this);

    public State state = State.RUN;

    final int BOX_VELOCITY_ITERATIONS = 6;
    final int BOX_POSITION_ITERATIONS = 3;

    final int MAX_BALL = 40;

    final float BALL_SIZE = 480 / 5f;
    final float BALL_SIZE_DIV_TEN_SQR = (float) Math.pow(BALL_SIZE, 2) / 100f;

    int countBallShot = 0;

    int frame = 0;
    int score = 0;
    int[] nextColor = new int[3];

    final float timeShot = 0.4f;
    float timePassed = 0.4f;

    Group groupNewOrLoad = new Group();
    Group groupGameOver  = new Group();
    Group groupPause     = new Group();


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
//
//    Color[] ballColor = {
//            new Color(0.93f, 0.89f, 0.5f, 1),  // 1
//            new Color(0.94f, 0.75f, 0.5f, 1),   // 4
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

    enum State{
        PAUSE,
        RUN,
        GAME_OVER,
        NEW_OR_LAST,
    }

    class Ball  {
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
            //bodyDef.bullet = true;

            CircleShape dynamicCircle = new CircleShape();
            dynamicCircle.setRadius(BALL_SIZE / 10f / 2f);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = dynamicCircle;
            fixtureDef.density = 0.01f;
            fixtureDef.friction = 0.1f;
            fixtureDef.restitution = 0.22f;

            bodyDef.position.set(Vector2.Zero); // center top screen
            body = world.createBody(bodyDef);

            body.createFixture(fixtureDef);
            //body.setLinearVelocity(velocity);
        }

        public void draw(SpriteBatch batch){
            float scale = 1.025f;
            //Texture useTexture = ballTexture[color];
            Texture useTexture = getBallTexture(color);
            batch.draw(useTexture, body.getPosition().x * 10 - BALL_SIZE / 2f * scale,
                    body.getPosition().y * 10 - BALL_SIZE / 2f * scale, BALL_SIZE * scale, BALL_SIZE * scale);
        }

        public String toSaveString(){
            return "" + body.getPosition().x + ',' + body.getPosition().y + ',' + color;
        }
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


    public void checkHighScore(){
        int highScore = game.prefs.getInteger("highScore");
        if (score > highScore) {
            game.prefs.putInteger("highScore", score);
        }
    }

    public void saveGame(){
        checkHighScore();
        String ballSave = "";
        for (int i = 0; i < balls.size; i++) {
            ballSave +=  balls.get(i).toSaveString();
            if (i < balls.size - 1) { ballSave += '|'; }
        }

        game.prefs.putString("ball", ballSave);
        game.prefs.putInteger("score", score);
        game.prefs.putLong("whenSaved", System.currentTimeMillis());
        game.prefs.flush();
        SLogger.log("gameSave, balls : " + ballSave);
    }

    public void loadGame() {
        try {
            SLogger.log("loadGameFromSave", this);
            String ballLoad = game.prefs.getString("ball");
            String[] parts = ballLoad.split(Pattern.quote("|"));
            for (int i = 0; i < parts.length; i++) {
                String[] param = parts[i].split(Pattern.quote(","));
                Ball tmpBall = new Ball();
                tmpBall.body.setTransform(new Vector2(Float.parseFloat(param[0]), Float.parseFloat(param[1])), 0);
                tmpBall.color = Integer.parseInt(param[2]);
                balls.add(tmpBall);
            }
            score = game.prefs.getInteger("score");
            state = State.RUN;
            SLogger.log("loadGame", this);
        } catch (Exception e) {
            e.printStackTrace();
            newGame(true);
            state = State.RUN;
        }
    }


    void shotBall() {
        //float xcor = -36 + 1.1f * BALL_SIZE * (2.5f + timePassed / timeShot);
        float xcor = 240;
        shotBall(new Vector2(mouse.x - xcor, -mouse.y).scl(0.125f * 480 / 800f));
    }

    Texture getBallTexture(int number){
        if(number < 8){
            return ballTexture[number];
        }else{
            return ballTexture[7];
        }
    }


    public void createBall(Vector2 velocity, Vector2 position) {
        Ball tmpBall = new Ball(getNextColor());
        tmpBall.body.setTransform(position, 0);
        tmpBall.body.setLinearVelocity(velocity);

        balls.add(tmpBall);
        timePassed = 0;
        readyForShot = false;
        Animation.add("leftBallOpacity", 1, 0.0f, 2000, new Animation.Sin());
    }

    public void createBall(Vector2 velocity) {
        float xcor = -36 + 1.1f * BALL_SIZE * (2.5f + timePassed / timeShot);
        xcor = 240;
        createBall(velocity, new Vector2(xcor / 10f, game.camera.viewportHeight / 10f));
    }

    public void shotBall(Vector2 velocity) {
        createBall(velocity);
        shotSound.play(0.2f);
        timePassed = 0;
        firstShot = false;
        countBallShot++;
    }


    public int getNextColor() { // actually its not color, its colorIndex
        int tmp = nextColor[2];
        nextColor[2] = nextColor[1];
        nextColor[1] = nextColor[0];
        nextColor[0] = game.rn.nextInt(4);
        return tmp;
    }

    public GameScreen() {
        initSceneGui();
        initAnotherGui();

        createPhysicalWorld();

        if (game.prefs.getString("ball").equals("notSaveGame") ||
            game.prefs.getString("ball").equals("")) {
            newGame(true);
        } else {
            state = State.NEW_OR_LAST;
        }
    }

    void createPhysicalWorld(){
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
    }

    void initSceneGui(){
        inputMultiplexer.addProcessor(inputHandler);

        stage.clear();

        //NEW GAME

        TextButton buttonNew = new TextButton(game.myBundle.get("newGame"), skin);
        buttonNew.setWidth(Gdx.graphics.getWidth());
        buttonNew.setHeight(Gdx.graphics.getWidth() / 3.7f);
        buttonNew.setY(Gdx.graphics.getHeight() / 2 + buttonNew.getHeight());
        buttonNew.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.clickSound.play();
                newGame(false);
                //game.setScreen(new GameScreen());
            }
        });

        TextButton buttonLoad = new TextButton(game.myBundle.get("loadGame"), skin);
        buttonLoad.setWidth(Gdx.graphics.getWidth());
        buttonLoad.setHeight(Gdx.graphics.getWidth() / 3.7f);
        buttonLoad.setY(Gdx.graphics.getHeight() / 2);
        buttonLoad.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.clickSound.play();
                SLogger.log("buttonLoad clicked", this);
                loadGame();
                //game.setScreen(new GameScreen());
            }
        });

        //GAME OVER

        TextButton buttonPlay = new TextButton(game.myBundle.get("playAgain"), skin);
        buttonPlay.setWidth(Gdx.graphics.getWidth());
        buttonPlay.setHeight(Gdx.graphics.getWidth() / 3.7f);
        buttonPlay.setY(Gdx.graphics.getHeight() / 2 - buttonPlay.getHeight());
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.clickSound.play();
                newGame(false);
                //game.setScreen(new GameScreen());
            }
        });




        TextButton buttonToMenu = new TextButton(game.myBundle.get("menu"), skin);
        buttonToMenu.setWidth(Gdx.graphics.getWidth());
        buttonToMenu.setHeight(Gdx.graphics.getWidth() / 3.7f);
        buttonToMenu.setY(Gdx.graphics.getHeight() / 2);
        buttonToMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.clickSound.play();
                game.prefs.putString("ball", "notSaveGame"); //
                game.prefs.flush();
                //newGame(false);
                game.setScreen(new MenuScreen());
            }
        });

        TextButton buttonShare = new TextButton(game.myBundle.get("share"), skin);
        buttonShare.setWidth(Gdx.graphics.getWidth());
        buttonShare.setHeight(Gdx.graphics.getWidth() / 3.7f);
        buttonShare.setY(Gdx.graphics.getHeight() / 2 - buttonPlay.getHeight()*2);
        buttonShare.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.clickSound.play();
                game.requestHandler.shareRecord(score);
            }
        });

        //PAUSE

        TextButton buttonСontinue = new TextButton(game.myBundle.get("continue"), skin);
        buttonСontinue.setWidth(Gdx.graphics.getWidth());
        buttonСontinue.setHeight(Gdx.graphics.getWidth() / 3.7f);
        buttonСontinue.setY(Gdx.graphics.getHeight() / 2);
        buttonСontinue.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.clickSound.play();
                state = State.RUN;
            }
        });

        TextButton buttonRestart = new TextButton(game.myBundle.get("restart"), skin);
        buttonRestart.setWidth(Gdx.graphics.getWidth());
        buttonRestart.setHeight(Gdx.graphics.getWidth() / 3.7f);
        buttonRestart.setY(Gdx.graphics.getHeight() / 2 - buttonToMenu.getHeight());
        buttonRestart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                newGame(false);
                state = State.RUN;
            }
        });

        TextButton buttonToMenuFromPause = new TextButton(game.myBundle.get("menu"), skin);
        buttonToMenuFromPause.setWidth(Gdx.graphics.getWidth());
        buttonToMenuFromPause.setHeight(Gdx.graphics.getWidth() / 3.7f);
        buttonToMenuFromPause.setY(Gdx.graphics.getHeight() / 2 - buttonToMenu.getHeight() * 2);
        buttonToMenuFromPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.clickSound.play();
//                game.prefs.putString("ball", "notSaveGame"); // TODO
//                game.prefs.flush();
                //newGame(false);
                game.setScreen(new MenuScreen());
            }
        });





        groupGameOver.addActor(buttonPlay);
        groupGameOver.addActor(buttonToMenu);
        groupGameOver.addActor(buttonShare);

        groupNewOrLoad.addActor(buttonNew);
        groupNewOrLoad.addActor(buttonLoad);

        groupPause.addActor(buttonСontinue);
        groupPause.addActor(buttonRestart);
        groupPause.addActor(buttonToMenuFromPause);

        stage.addActor(groupGameOver);
        stage.addActor(groupNewOrLoad);
        stage.addActor(groupPause);
    }

    void initAnotherGui(){
        line.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        arrow.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        ballTexture[0] = new Texture("balls_0.png");
        ballTexture[1] = new Texture("balls_1.png");
        ballTexture[2] = new Texture("balls_2.png");
        ballTexture[3] = new Texture("balls_3.png");
        ballTexture[4] = new Texture("balls_4.png");
        ballTexture[5] = new Texture("balls_5.png");
        ballTexture[6] = new Texture("balls_6.png");
        ballTexture[7] = new Texture("balls_7.png");
    }


    void newGame(boolean first) {
        score = 0;
        countBallShot = 0;
        state = State.RUN;

//        if (!first) {
//            game.showAds();
//        }

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


    @Override
    public void render(float dt) {

//        try {
//            Thread.sleep(80);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        frame++;
        fpsLogger.log();

        game.font.setColor(1, 1, 1, 1);



        batch.setProjectionMatrix(game.camera.combined);
        assert(batch.isBlendingEnabled());
        batch.begin();

        batch.draw(game.background, 0, 0, 480, game.camera.viewportHeight);

        // optimization, sort before render, becouse "switchTexture" load CPU or GPU
        balls.sort(new Comparator<Ball>() {
            @Override
            public int compare(Ball a, Ball b) {
                return a.color - b.color;
            }
        });


        for (Ball ball : balls) {
            ball.draw(batch);
        }

        float scale = 1.04f;
        for (int i = 0; i < 3; i++) {
            float light;
            if (i == 2) {
                light = 0.5f + 0.5f * timePassed / timeShot;
            }else{
                light = 0.5f;
            }

            batch.setColor(light, light, light, 1);
            float smallBallScale = scale * 0.5f;
            batch.draw(getBallTexture(nextColor[i]), -36 + 0.7f * BALL_SIZE * (i + timePassed / timeShot), game.camera.viewportHeight - BALL_SIZE / 1.7f, BALL_SIZE * smallBallScale, BALL_SIZE * smallBallScale);

        }

        for (int i = effects.size - 1; i >= 0; i--) {
            BallEffect effect = effects.get(i);
            effect.timeShow += dt * 3; // TODO maybe its shitcode
            scale = 1.25f - effect.timeShow * 0.1f;

            batch.setColor(1, 1, 1, 1 - effect.timeShow);
            batch.draw(getBallTexture(effect.color), effect.x * 10 - BALL_SIZE * scale / 2f, effect.y * 10 - BALL_SIZE * scale / 2f, BALL_SIZE * scale, BALL_SIZE * scale);

            batch.setColor(1, 1, 1, effect.timeShow / 2f);
            batch.draw(getBallTexture(effect.color + 1), effect.x * 10 - BALL_SIZE * scale / 2f, effect.y * 10 - BALL_SIZE * scale / 2f, BALL_SIZE * scale, BALL_SIZE * scale);

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
            xcor = 240;
            Vector2 v = new Vector2(0 - mouse.y, mouse.x - xcor);
            float degrees = -(float) (180 + (Math.atan2((0 - mouse.y), xcor - mouse.x) * 180.0d / Math.PI));
            //
            batch.draw(line, xcor, game.camera.viewportHeight, 0, 0, v.len(), 15, 1, 1, degrees, 0, 0, line.getWidth(), line.getHeight(), false, false);
        }


        batch.end();


        float fullWidth = Math.min(480 * game.fullCamera.viewportHeight / 640, game.fullCamera.viewportWidth);

        batch.setProjectionMatrix(game.fullCamera.combined);
        batch.begin();

        float cameraToFull = game.fullCamera.viewportWidth / (float) game.camera.viewportWidth;

        for (Ball ball : balls) {

            String str = Integer.toString((int) Math.pow(4, ball.color)*2);
            if(str.length() > 3){ game.font.setScale(0.80f); }
            if(str.length() > 4){ game.font.setScale(0.70f); }
            if(str.length() > 5){ game.font.setScale(0.65f); }
            game.font.draw(batch, str, (ball.body.getPosition().x * 10) * cameraToFull - game.font.getBounds(str).width / 2 , (ball.body.getPosition().y * 10) * cameraToFull + game.font.getBounds(str).height / 2 );
            game.font.setScale(1);

        }


        String textScore = "score: " + score + "  ";

        batch.setColor(1,1,1, Animation.get("leftBallOpacity"));
        batch.draw(game.blackGradient, 0,0,game.fullCamera.viewportWidth, game.fullCamera.viewportHeight * 0.15f);
        batch.setColor(Color.WHITE);


        String textLeft = "  balls " + balls.size + " / " + MAX_BALL + " ";


        game.font.setColor(new Color(1, 1, 1, Animation.get("leftBallOpacity")));
        game.font.draw(batch, textLeft, fullWidth * 0.5f - game.font.getBounds(textLeft).width * 0.5f , game.font.getBounds(textLeft).height + game.fullCamera.viewportHeight * 0.024f );
        game.font.setColor(new Color(1, 1, 1, 1));
        game.font.draw(batch, textScore, fullWidth - game.font.getBounds(textScore).width , game.fullCamera.viewportHeight * 0.983f );



        game.font.setScale(0.5f);
        for (int i = 0; i < 3; i++) {
            String str = Integer.toString((int) Math.pow(4, nextColor[i])*2);
            game.font.draw(batch, str, (-36 + 0.7f * BALL_SIZE * (i + timePassed / timeShot) + BALL_SIZE / 4 * scale) * cameraToFull - game.font.getBounds(str).width / 2, (game.camera.viewportHeight - 30) * cameraToFull + game.font.getBounds(str).height / 2);
        }
        game.font.setScale(1);

        switch (state) {
            case RUN:
                physicalWork(dt);
                if (!firstShot) {
                    //if ((timePassed += dt) > timeShot) shotBall();
                    timePassed += dt;
                    if(timePassed > 0.4f){timePassed = 0.4f;}
                    //if (balls.size > MAX_BALL) newGame(false);

                    if( (balls.size > 15 && GlobalVars.DEBUG) || ( !GlobalVars.DEBUG &&
                            balls.size > MAX_BALL)) {
                        checkHighScore();
                        state = State.GAME_OVER;
                        Animation.add("gameOverBlack", 1, 0.4f, 700, new Animation.Sin());
                        Animation.add("gameOverOpacity", 0, 0.85f, 700, new Animation.Sin());
                    }
                } else {
                    timePassed = 0.4f;
                }
                break;
            case PAUSE:
                float lightness = Animation.get("pauseBlack");
                batch.setColor(lightness, lightness, lightness, Animation.get("pauseOpacity"));
                batch.draw(game.background, 0, 0, game.fullCamera.viewportWidth, game.fullCamera.viewportHeight);
                batch.setColor(Color.WHITE);
                game.font.draw(batch, "Your shot: " + countBallShot + " balls", 10, game.fullCamera.viewportHeight*0.95f);
                game.font.draw(batch, "Your score: " + score, 10, game.fullCamera.viewportHeight*0.87f);
                break;


            case GAME_OVER:
                //Gdx.input.setInputProcessor(inputMultiplexer);
                lightness = Animation.get("gameOverBlack");
                batch.setColor(lightness, lightness, lightness, Animation.get("gameOverOpacity"));
                batch.draw(game.background, 0, 0, game.fullCamera.viewportWidth, game.fullCamera.viewportHeight);
                game.font.draw(batch, "Your shot: " + countBallShot + " balls", 10, game.fullCamera.viewportHeight*0.95f);
                game.font.draw(batch, "Your score: " + score, 10, game.fullCamera.viewportHeight*0.87f);
                break;
            case NEW_OR_LAST:
                game.font.draw(batch, "Your have saved game", 10, game.fullCamera.viewportHeight*0.95f);

        }

        //Gdx.gl.glBlendFunc(GL20.GL_ONE, GL20.GL_DST_COLOR);
        //batch.draw(light,0,0);

        batch.end();
        batch.setColor(1, 1, 1, 1f);

        groupNewOrLoad.setVisible(false);
        groupGameOver.setVisible(false);
        groupPause.setVisible(false);

        if(state == State.GAME_OVER){
            groupGameOver.setVisible(true);
            //groupNewOrLoad.setVisible(false);
            stage.act();
            stage.draw();
        }

        if(state == State.NEW_OR_LAST){
            //Gdx.input.setInputProcessor(inputMultiplexer);
            //groupGameOver.setVisible(false);
            groupNewOrLoad.setVisible(true);
            stage.act();
            stage.draw();
        }

        if(state == State.PAUSE){
            groupPause.setVisible(true);
            stage.act();
            stage.draw();
        }
    }

    void physicalWork(float dt) {

        if (frame % 1 == 0) {
            graphBall.createGraph(balls.size);

            for (int i = 0; i < balls.size; i++) {  // TODO SORT!!!!!!!!!!! dont all ball check with all ball, only ball with something color check with ball with same color
                Ball ballI = balls.get(i);
                for (int j = i + 1; j < balls.size; j++) {
                    Ball ballJ = balls.get(j);
                    if (ballI.color == ballJ.color) {
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
    public void show() {
        SLogger.log("show");
        //inputMultiplexer.addProcessor(inputHandler); // TODO  TEST THIS
        // we add inputHandler in ininSceneGui();
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void pause() {
        saveGame();
    }

    @Override
    public void hide(){
        SLogger.log("hide");
        inputMultiplexer.removeProcessor(inputHandler);
        checkHighScore();
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

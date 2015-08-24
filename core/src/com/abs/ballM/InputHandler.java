package com.abs.ballM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by k on 15.08.15.
 */
public class InputHandler extends InputAdapter {

    private GameScreen gameScreen;
    public InputHandler(GameScreen gameScreen){
        this.gameScreen = gameScreen;
    }

    @Override
    public boolean keyDown(int keycode) {
        SLogger.log("keycode = " + keycode);
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            switch (gameScreen.state) {
                case PAUSE:
                    gameScreen.game.showConfirmDialog();
                    break;
                case RUN:
                    gameScreen.state = GameScreen.State.PAUSE;
                    Animation.add("pauseBlack", 1, 0.3f, 700, new Animation.Sin());
                    Animation.add("pauseOpacity", 0, 0.85f, 700, new Animation.Sin());
                    gameScreen.saveGame();
            }
        }
        return false;
    }



    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        gameScreen.mouse.x = screenX * 480f / Gdx.graphics.getWidth();
        gameScreen.mouse.y = screenY * 480f * Gdx.graphics.getHeight() / Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        gameScreen.mouse.x = screenX * 480f / Gdx.graphics.getWidth();
        gameScreen.mouse.y = screenY * 480f * Gdx.graphics.getHeight() / Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        if(gameScreen.state == GameScreen.State.RUN){
            gameScreen.readyForShot = true;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (gameScreen.readyForShot) {
            final float waitBeforeShot = 0.2f;
            if (gameScreen.timePassed < waitBeforeShot) {
                //if the user did not manage to shoot - adjust last
                if (gameScreen.balls.size != 0) {
                    gameScreen.balls.get(gameScreen.balls.size - 1).body.setLinearVelocity(new Vector2(screenX - Gdx.graphics.getWidth() / 2f, -screenY).scl(0.125f * 480 / Gdx.graphics.getHeight()));
                }
            } else {
                gameScreen.shotBall();

            }
        }
        gameScreen.readyForShot = false;
        return true;
    }


}

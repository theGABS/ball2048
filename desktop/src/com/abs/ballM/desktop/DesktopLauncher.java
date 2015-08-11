package com.abs.ballM.desktop;

import com.abs.ballM.MyGame;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher implements  MyGame.RequestHandler{
    private static DesktopLauncher application;
    public static void main (String[] arg) {
        if (application == null) {
            application = new DesktopLauncher();
        }
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 640;
        config.width = 480;

//        config.height = 960;
//        config.width = 540;
//
//        config.height = 320;
//        config.width = 240;
        //config.addIcon("../res/drawable-hdpi/ic_launcher.png", Files.FileType.Internal);
        new LwjglApplication(new MyGame(application), config);
    }


    @Override
    public void confirm(final MyGame.ConfirmInterface confirmInterface) {
        confirmInterface.showLibGDX();
    }

    @Override
    public void loadAds(){
        // for desktop no advertising
    }

    @Override
    public void share(){
        // for desktop does not need
    }

    @Override
    public void showAds(){
        // for desktop no advertising
    }
}

package com.abs.ballM;

import com.badlogic.gdx.Gdx;

/**
 * Created by k on 09.08.15.
 */
public class SLogger {
    public static void log(String str, Object obj){
        Gdx.app.log("TAG", str + obj.getClass().toString());
    }

    public static void log(String str){
        Gdx.app.log("TAG", str );
    }
}

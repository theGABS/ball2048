package com.abs.ballM;

import java.util.HashMap;

/**
 * Created by k on 09.08.15.
 */


public class Animation {

    interface Timing{
        float getValue(float delta);
    }

    static class Linear implements Timing{
        public float getValue(float delta){
            return delta;
        }
    }

    static class Sin implements Timing{
        public float getValue(float delta){
            return (float) ( 1 + Math.sin( delta * Math.PI - Math.PI/2))/2.f;
        }
    }

    static class OneAnimation {
        private float from = 0;
        private float to = 1;
        private long startTime;
        private long duration = 1000;
        private Timing timing;
        public OneAnimation(float from, float to, long howLong, Timing timing){
            startTime = System.currentTimeMillis();
            this.from = from;
            this.to = to;
            this.duration = howLong;
            this.timing = timing;
        }

        public float value(){
            float time = (System.currentTimeMillis() - startTime)/(float)duration;
            time = Math.max(0, Math.min(time, 1)); //  0 <= time <= 1
            float delta = timing.getValue(time);
            float value = from + (to-from)*delta;
            return value;
        }
    }

    private static HashMap<String, OneAnimation> animations = new HashMap<String, OneAnimation>();

    public static void add(String name, float from, float to, long duration, Timing timing){
        OneAnimation oneAnimation = new OneAnimation(from, to, duration, timing);
        animations.put(name, oneAnimation);
    }

    public static void add(String name, float from, float to, long duration){
        OneAnimation oneAnimation = new OneAnimation(from, to, duration, new Linear());
        animations.put(name, oneAnimation);
    }

    public static float get(String name){
        if(animations.containsKey(name)) {
            return animations.get(name).value();
        }else{
            SLogger.log("animations has not key: " + name);
            return 0;
        }
    }
}

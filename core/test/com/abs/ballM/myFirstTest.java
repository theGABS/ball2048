// MainActivityTest.java

package com.abs.ballM;

// Static imports for assertion methods
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import junit.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;





//@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
//@RunWith(RobolectricGradleTestRunner.class)
public class myFirstTest {
    //private MainActivity activity;
    //private MyGame game;


    // @Before => JUnit 4 annotation that specifies this method should run before each test is run
    // Useful to do setup for objects that are needed in the test
    @Before
    public void setup() {
        // Convenience method to run MainActivity through the Activity Lifecycle methods:
        // onCreate(...) => onStart() => onPostCreate(...) => onResume()
        //activity = Robolectric.setupActivity(MainActivity.class);
        //game = new MyGame();
    }

    // @Test => JUnit 4 annotation specifying this is a test to be run
    // The test simply checks that our TextView exists and has the text "Hello world!"
//    @Test
//    public void validateTextViewContent() {
////        TextView tvHelloWorld = (TextView) activity.findViewById(R.id.tvHelloWorld);
////        assertNotNull("TextView could not be found", tvHelloWorld);
////        assertTrue("TextView contains incorrect text",
////                "Hello world!".equals(tvHelloWorld.getText().toString()));
//
//    }

    @Test
    public void testGraph(){
        Graph graph = new Graph();
        graph.createGraph(5);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);


        assertEquals(graph.searchGroups().get(0).size, 4);

    }
}
package li.safare.processing.separation;

import li.safare.processing.staf.*;
import li.safare.processing.staf.Number;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: sapara
 * Date: 7/20/13
 * Time: 9:09 AM
 * To change this template use File | Settings | File Templates.
 */

public class Main extends MApplet {
    ArrayList<Thing> things;
    Color backgroundColor;

    @Override
    public void setup() {
        size(1280, 720, P2D);
        Number r = new Number(0,255);
        Number g = new Number(0,255);
        Number b = new Number(0,255);
        backgroundColor = new Color(r,g,b).setNoise(0.001f);
        Thing.setGlobalMaxVelocity(new Number(3));
        Thing.setGlobalMaxForce(new Number(0.2f));
        Thing.setGlobalRadius(new Number(10,15));
        things = new ArrayList<Thing>();
        for (int i = 0; i < 2; i++){
            int x = (int) random(width);
            int y = (int) random(height);
            Thing thing = new Thing(x,y);
            things.add(thing);
        }
    }

    @Override
    public void draw() {
        backgroundColor.regenerate();
        background(backgroundColor.get());
        for (Thing thing : things){
//            thing.seek(new PVector(mouseX,mouseY));
//            thing.separate(things);
            thing.applyBehaviors(things);
            thing.edge();
            thing.update();
            thing.display(Thing.CIRCLE);
        }

        fill(255);
//        text("Click and drag the mouse to generate new vehicles.",10,height-16);
    }
    @Override
    public void mouseDragged() {
        things.add(new Thing(mouseX,mouseY));
    }

}


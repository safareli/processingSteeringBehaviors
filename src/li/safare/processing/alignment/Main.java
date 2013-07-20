package li.safare.processing.alignment;/**
 * Created with IntelliJ IDEA.
 * User: sapara
 * Date: 7/20/13
 * Time: 8:28 AM
 * To change this template use File | Settings | File Templates.
 */

import li.safare.processing.staf.MApplet;
import li.safare.processing.staf.Number;
import li.safare.processing.staf.Thing;

import java.util.ArrayList;

public class Main extends MApplet {
    ArrayList<Thing> things;

    @Override
    public void setup() {
        size(640, 360,P2D);
//        size(displayWidth, displayHeight,P2D);

        things = new ArrayList<Thing>();
        Thing.setGlobalMaxVelocity(new Number(2,5));
        Thing.setGlobalMaxForce(new Number(0.05f,0.5f));
        Thing.setGlobalRadius(new Number(3,5));
        for (int i = 0; i < 300; i++){
            int x = (int) random(width);
            int y = (int) random(height);
            Thing thing = new Thing(x,y);
            things.add(thing);
        }
    }

    @Override
    public void draw() {
        background(100);
        this.mouseEntered();
        for (Thing thing : things){
            thing.align(things);
            thing.edge();
            thing.update();
            thing.display(Thing.ARROW);
        }

        fill(255);
        text("Click and drag the mouse to generate new vehicles.",10,height-16);
    }
    @Override
    public void mouseDragged() {
        things.add(new Thing(mouseX,mouseY));
    }

}

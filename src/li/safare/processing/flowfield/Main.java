package li.safare.processing.flowfield;
import li.safare.processing.staf.*;
import li.safare.processing.staf.Number;
import processing.core.*;

import java.util.ArrayList;

public class Main extends MApplet {
    ArrayList<Thing> things;
    FlowField flowField;
    Color backgroundColor;

    @Override
    public void setup() {
        size(640, 360, P2D);
        Number c = new Number(0,100);
        backgroundColor = new Color(c,c,c);
        c = new Number(100,150);
        Color flowFieldColor = new Color(c,c,c);
//        size(displayWidth, displayHeight,P2D);
        flowField = new FlowField(this,flowFieldColor,20);
        things = new ArrayList<Thing>();
        Thing.setGlobalMaxVelocity(new Number(1,5));
        Thing.setGlobalMaxForce(new Number(0.05f,0.5f));
        Thing.setGlobalRadius(new Number(2));
        for (int i = 0; i < 300; i++){
            int x = (int) random(width);
            int y = (int) random(height);
            Thing thing = new Thing(x,y);
            things.add(thing);
        }
    }

    @Override
    public void draw() {
        background(backgroundColor.get());
        flowField.update();
        if (DEBAG) flowField.display();
        for (Thing thing : things){
            if (isMouseIn()){
                thing.flee(new PVector(mouseX, mouseY));
            }
            thing.follow(flowField);
            thing.edge();
            thing.update();
            thing.display(Thing.ARROW);
        }
    }

    @Override
    public void mousePressed() {
        DEBAG = !DEBAG;
    }
}

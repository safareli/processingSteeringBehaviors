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
    private ArrayList<Thing> things;
    private Color backgroundColor;
    private int _blendI = 0;
    private int[] _blendMode= {BLEND, ADD, SUBTRACT, DARKEST, LIGHTEST, DIFFERENCE, EXCLUSION, MULTIPLY, SCREEN, REPLACE};
    private String[] _blendModeText= {"BLEND", "ADD", "SUBTRACT", "DARKEST", "LIGHTEST", "DIFFERENCE", "EXCLUSION", "MULTIPLY", "SCREEN", "REPLACE"};
    private boolean showText = true;
    private boolean isBlend = true;

    //    BLEND - linear interpolation of colours: C = A*factor + B. This is the default blending mode.
//    ADD - additive blending with white clip: C = min(A*factor + B, 255)
//    SUBTRACT - subtractive blending with black clip: C = max(B - A*factor, 0)
//    DARKEST - only the darkest colour succeeds: C = min(A*factor, B)
//  * LIGHTEST - only the lightest colour succeeds: C = max(A*factor, B)
//    DIFFERENCE - subtract colors from underlying image.
//    EXCLUSION - similar to DIFFERENCE, but less extreme.
//  * MULTIPLY - multiply the colors, result will always be darker.
//  * SCREEN - opposite multiply, uses inverse values of the colors.
//    REPLACE - the pixels entirely replace the others and don't utilize alpha (transparency) values
    @Override
    public void setup() {
        size(displayWidth, displayHeight, P2D);
        Number r = new Number(0,50);
        Number g = new Number(0,255);
        Number b = new Number(0,255);
        Number a = new Number(255);
        backgroundColor = new Color(r,g,b,a).setNoise(0.001f);
        Thing.setGlobalMaxVelocity(new Number(5));
        Thing.setGlobalMaxForce(new Number(1.5f));
        Thing.setGlobalRadius(new Number(10,15));
        things = new ArrayList<Thing>();
        for (int i = 0; i < 200; i++){
            int x = (int) random(width);
            int y = (int) random(height);
            Thing thing = new Thing(x,y);
            things.add(thing);
        }
    }

    @Override
    public void draw() {
//        if (isBlend){
//            blendMode(_blendMode[_blendI]);
//        }
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

//        if (showText){
//            fill(255);
//            text(_blendModeText[_blendI], 10, 10);
//            text("press \"b\" to deactivate blending mode", 10, 30);
//            text("press \"h\" to hide text",10,50);
//            text("Click and drag the mouse to generate new vehicles.",10,height-16);
//        }
        fill(255);
        text("frames"+frameRate,10,height-16);
    }

    @Override
    public void mouseDragged() {
        things.add(new Thing(mouseX,mouseY));
    }


    @Override
    public void keyPressed() {
//        if (key == CODED) {
//            if (keyCode == UP) {
//                if (_blendI + 1 == _blendMode.length){
//                    _blendI = 0;
//                }else{
//                    _blendI++;
//                }
//            } else if (keyCode == DOWN) {
//                if (_blendI == 0){
//                    _blendI = _blendMode.length -1 ;
//                }else{
//                    _blendI--;
//                }
//            }
//        }else if(key == 'b' || key == 'B'){
//            isBlend = !isBlend;
//        }else if(key == 'h' || key == 'H'){
//            showText = !showText;
//        }
    }
}


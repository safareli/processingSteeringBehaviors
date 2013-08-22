package li.safare.processing.staf;

import processing.core.PApplet;

/**
 * Created with IntelliJ IDEA.
 * User: sapara
 * Date: 7/20/13
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class MApplet extends PApplet{
    public boolean DEBAG = true;
    public MApplet(){
        Holder.setPApplet(this);
    }
    public Boolean isMouseIn(){

        return !(mouseX < 10 || mouseX > width - 10 || mouseY < 10 || mouseY > height - 10);
    }
    @Override
    public void background(int rgba){
        strokeWeight(0);
        fill(rgba);
        rect(0,0,width,height);
    }
}

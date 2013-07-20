package li.safare.processing.staf;

/**
 * Created with IntelliJ IDEA.
 * User: sapara
 * Date: 7/20/13
 * Time: 10:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class Number {
    private float i,from,to;
    private boolean isRandom = false;

    public Number(float from,float to) {
        this.from = from;
        this.to = to;
        isRandom = true;
    }
    public Number(float i) {
        this.i = i;
    }

    public float getFrom(){
        return (isRandom)?from:i;
    }
    public float getTo(){
        return (isRandom)?to:i;
    }
    public int getInt(){
        return (int) getFloat();
    }
    public float getFloat(){
        if (isRandom){
            return Holder.getPApplet().random(from, to);
        }else{
            return i;
        }
    }

}

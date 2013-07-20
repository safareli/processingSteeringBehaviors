package li.safare.processing.staf;

/**
 * Created with IntelliJ IDEA.
 * User: sapara
 * Date: 7/20/13
 * Time: 10:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class Holder {
    private static MApplet pApplet;

    public static MApplet getPApplet(){
        return pApplet;
    }
    public static void setPApplet(MApplet pApplet){
        Holder.pApplet = pApplet;
    }
}

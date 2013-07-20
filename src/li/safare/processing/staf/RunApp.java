package li.safare.processing.staf;

import processing.core.PApplet;

/**
 * Created with IntelliJ IDEA.
 * User: sapara
 * Date: 7/20/13
 * Time: 8:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class RunApp {
//    static String className = "alignment";
//    static String className = "flowfield";
    static String className = "separation";


    public static void main(String[] args) {
        PApplet.main(new String[]{"--present", "li.safare.processing."+className+".Main"});
    }
}

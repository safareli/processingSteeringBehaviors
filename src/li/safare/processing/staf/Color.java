package li.safare.processing.staf;

import processing.core.PApplet;

/**
 * Created with IntelliJ IDEA.
 * User: sapara
 * Date: 7/19/13
 * Time: 9:27 PM
 * some color functions ar taken from processing PApplet.java
 * url : https://github.com/processing/processing/blob/master/core/src/processing/core/PApplet.java#L9965
 */

public class Color {
    private Number r,g,b,a;//generators
    private int _r,_g,_b,_a; //generated int values
    private float nR,nG,nB,nA; //noise steps
    boolean isNoise;
    private float noiseStepSize = 0.01f;

    public Color(Number r, Number g ,Number b ){
        this(r, g, b, new Number(255));
    }
    public Color(Number r, Number g ,Number b,Number a ){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        regenerate();
    }
    public Color setNoise(){
        return setNoise(0.01f);
    }

    public Color setNoise(float stepSize) {
        noiseStepSize = stepSize;
        isNoise = true;
        nR = Holder.getPApplet().random(1000,2000);
        nG = Holder.getPApplet().random(2000,3000);
        nB = Holder.getPApplet().random(3000,4000);
        nA = Holder.getPApplet().random(4000,5000);
        regenerate();
        return this;
    }
    public int get(){
        return getInt();
    }
    private int getInt(){
        int r = getR();
        int g = getG();
        int b = getB();
        int a = getA();
        if (r > 255) r = 255; else if (r < 0) r = 0;
        if (g > 255) g = 255; else if (g < 0) g = 0;
        if (b > 255) b = 255; else if (b < 0) b = 0;
        if (a > 255) a = 255; else if (a < 0) a = 0;

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public void regenerate(){
        if (isNoise){
            MApplet p= Holder.getPApplet();
            nR += noiseStepSize;
            nG += noiseStepSize;
            nB += noiseStepSize;
            nA += noiseStepSize;

            _r = (int) PApplet.map(p.noise(nR), 0, 1, r.getFrom(), r.getTo());
            _g = (int) PApplet.map(p.noise(nG),0,1,g.getFrom(),g.getTo());
            _b = (int) PApplet.map(p.noise(nB),0,1,b.getFrom(),b.getTo());
            _a = (int) PApplet.map(p.noise(nA),0,1,a.getFrom(),a.getTo());

        }else{
            _r = r.getInt();
            _g = g.getInt();
            _b = b.getInt();
            _a = a.getInt();
        }
    }

    public int getR() {
        return _r;
    }

    public int getG() {
        return _g;
    }

    public int getB() {
        return _b;
    }

    public int getA() {
        return _a;
    }

    static public Color randomRGB(){
        Number n = new Number(0,255);
        return new Color(n,n,n);
    }

    static public Color randomRGBa(){
        Number n = new Number(0,255);
        return new Color(n,n,n,n);
    }

}

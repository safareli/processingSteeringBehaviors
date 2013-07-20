package li.safare.processing.staf;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Created with IntelliJ IDEA.
 * User: sapara
 * Date: 7/19/13
 * Time: 5:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class FlowField {
    private MApplet p;
    private int resolution;
    private PVector field [][];
    int cols;
    int rows;
    float zoff = 0.0f;
    Color color;
    public FlowField(MApplet p, Color color, int resolution){
        this.p = p;
        this.color =  color;
        this.resolution = resolution;
        cols = p.width  / resolution;
        rows = p.height / resolution;
        field = new PVector[cols][rows];
        update();
    }
    public void update(){
        float xoff = 0;
        for (int i = 0; i < cols; i++) {
            float yoff = 0;
            for (int j = 0; j < rows; j++) {
                float alpha= PApplet.map(p.noise(xoff,yoff,zoff), 0, 1, 0, PApplet.TWO_PI);
                field[i][j] = new PVector(PApplet.sin(alpha),PApplet.cos(alpha));
                yoff += 0.15;
            }
            xoff += 0.15;
        }
        zoff += 0.01;
    }

    public PVector getDirection(PVector location){
        int column = (int)PApplet.constrain(location.x / resolution,0,cols-1);
        int row= (int)PApplet.constrain(location.y / resolution,0,rows-1);
        return field[column][row].get();
    }

    public void display(){
        for ( int x = 0 ; x < cols; x++ ){
            for ( int y = 0 ; y < rows; y++ ){
                int xC = x*resolution + resolution/2;
                int yC = y*resolution + resolution/2;
                PVector direction = field[x][y].get();
                direction.setMag(resolution);
                float x1 = xC - direction.x/2;
                float y1 = yC - direction.y/2;
                float x2 = xC + direction.x/2;
                float y2 = yC + direction.y/2;
                p.stroke(color.get());
                p.line(x1,y1, x2,y2);
            }
        }
    }
}

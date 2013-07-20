package li.safare.processing.staf;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: sapara
 * Date: 7/18/13
 * Time: 10:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class Thing {
    private MApplet p;

    public static final int CIRCLE = 1;
    public static final int ARROW = 2;
    private static Number _maxVelocity,_maxForce,_radius,_mass;

    private Color color;
    private PVector location, velocity, acceleration;
    private float radius, maxForce, maxVelocity, mass;
    private float alignmentDistance, separationDistance,arriveDistance,fleeDistance;

    public Thing(int x, int y){
        this(x,y,PVector.random2D(), _maxVelocity.getFloat(), _maxForce.getFloat());
    }
    public Thing(int x, int y, PVector initialVelocity, float maxVelocity, float maxForce){
        p = Holder.getPApplet();
        radius = (_radius == null) ? 1 : _radius.getFloat();
        mass = (_mass == null) ? 1 : _mass.getFloat();

        arriveDistance = 100;
        fleeDistance = 100;
        alignmentDistance = 25;
        separationDistance = radius*2;

        this.maxVelocity = maxVelocity;
        this.maxForce = maxForce;

        location = new PVector(x,y);
        velocity = initialVelocity.get();
        velocity.setMag(maxVelocity);
        Number r = new Number(100,255);
        Number g = new Number(0,200);
        Number b = new Number(0);
        color = new Color(r,g,b).setNoise(0.001f);
        acceleration = new PVector(0,0);
    }

    public static void setGlobalMaxVelocity(Number maxVelocity){
        _maxVelocity = maxVelocity;
    }
    public static void setGlobalMaxForce(Number maxForce){
        _maxForce = maxForce;
    }
    public static void setGlobalRadius(Number radius){
        _radius = radius;
    }

    public void update() {
        velocity.add(acceleration);
        velocity.limit(maxVelocity);
        location.add(velocity);
        acceleration.mult(0);
    }
    public void edge(){
        if (location.x < 0)         location.x = p.width;
        if (location.x > p.width)   location.x = 0;
        if (location.y < 0)         location.y = p.height;
        if (location.y > p.height)  location.y = 0;
    }

    public void applyForce(PVector force) {
        PVector nForce = PVector.div(force, mass);
        acceleration.add(nForce);
    }


    public void display(int type) {
        color.regenerate();
        switch (type){
            case ARROW:
                float theta = velocity.heading() + PApplet.PI/2;
                p.fill(color.get());
                p.stroke(color.get());
                p.strokeWeight(1);
                p.pushMatrix();
                p.translate(location.x, location.y);
                p.rotate(theta);
//                p.text(color.getRBG(),10,10);
                p.beginShape();
                p.vertex(0, -radius * 2);
                p.vertex(-radius, radius *2);
                p.vertex(radius, radius *2);
                p.endShape(PApplet.CLOSE);
                p.popMatrix();
                break;

            case CIRCLE:
                p.fill(color.get());
                p.stroke(color.get());
                p.strokeWeight(1);
                p.pushMatrix();
                p.translate(location.x, location.y);
//                p.text(color.getRBG(),10,10);
                p.ellipse(0, 0,radius*2,radius*2);
                p.popMatrix();
                break;
        }
    }

    public PVector getLocation() {
        return location;
    }


    private void steer(PVector desired){
        applyForce(steeringForce(desired));
    }

    public PVector steeringForce(PVector desired){
        PVector steer = PVector.sub(desired, velocity);
        steer.limit(maxForce);
        return steer;
    }
    public void applyBehaviors(ArrayList<Thing> vehicles) {
        PVector separateForce;
        separateForce = separateForce(vehicles);
        PVector seekForce = seekForce(new PVector(p.mouseX,p.mouseY));
        separateForce.mult(3);
        seekForce.mult(0.5f);
        applyForce(separateForce);
        applyForce(seekForce);
    }

    public PVector seekForce(PVector to){
        PVector desired = PVector.sub(to,location);
        return steeringForce(desired);
    }
    public void seek(PVector to) {
        applyForce(seekForce(to));
    }

    public PVector arriveForce(PVector to){
        PVector desired = PVector.sub(to,location);
        float distance = desired.mag();
        if (distance < arriveDistance){
            float m = PApplet.map(distance, 0, arriveDistance, 0, maxVelocity);
            desired.setMag(m);
        } else {
            desired.setMag(maxVelocity);
        }

        return steeringForce(desired);
    }
    public void arrive(PVector to) {
        applyForce(arriveForce(to));
    }

    public PVector fleeForce(PVector from){
        PVector desired = PVector.sub(location,from);
        float distance = desired.mag();
        if (distance < fleeDistance){
            desired.div(distance);
            desired.limit(maxVelocity);
            return steeringForce(desired);
        }else{
            return new PVector(0,0);
        }
    }

    public void flee(PVector from) {
        applyForce(fleeForce(from));
    }

    public PVector followForce(FlowField flow) {
        PVector desired = flow.getDirection(location);
        desired.mult(maxVelocity);
        return  steeringForce(desired);
    }

    public void follow(FlowField flow) {
        applyForce(followForce(flow));

    }

    public PVector alignForce(ArrayList<Thing> things) {
        int count = 0;
        PVector sum = new PVector(0,0);
        for (Thing thing : things){
            float distance = PVector.dist(thing.location, location);

            if (distance > 0 && distance < alignmentDistance){
                sum.add(thing.velocity);
                count++;
            }
        }
        if (count > 0){
            sum.div(count);
            sum.setMag(maxVelocity / count);
            return  steeringForce(sum);
        }else
            return new PVector(0,0);
    }

    public void align(ArrayList<Thing> things) {
        applyForce(alignForce(things));
    }
    public PVector separateForce(ArrayList<Thing> things){
        PVector sum = new PVector();
        int count = 0;
        for (Thing thing : things) {
            float d = PVector.dist(location, thing.location);
            if ((d > 0) && (d < separationDistance)) {
                PVector diff = PVector.sub(location, thing.location);
                diff.normalize();
                diff.div(d);
                sum.add(diff);
                count++;
            }
        }
        if (count > 0) {
//            sum.div(count);
            sum.setMag(maxVelocity * 2);
            return steeringForce(sum);
        }else{
            return new PVector(0,0);
        }
    }
    public void separate(ArrayList<Thing> things) {
        applyForce(separateForce(things));
    }

}

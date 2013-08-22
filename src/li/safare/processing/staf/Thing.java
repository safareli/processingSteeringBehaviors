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
    private static Number globalMaxVelocity, globalMaxForce, globalRadius,_mass;

    private Color color;
    private PVector location, velocity, acceleration;
    private float radius, maxForce, maxVelocity, mass;
    private float alignmentDistance, separationDistance,arriveDistance,fleeDistance;

    public Thing(int x, int y){
        this(x, y, PVector.random2D(), globalMaxVelocity.getFloat(), globalMaxForce.getFloat());
    }
    public Thing(int x, int y, PVector initialVelocity, float maxVelocity, float maxForce){
        p = Holder.getPApplet();
        radius = (globalRadius == null) ? 1 : globalRadius.getFloat();
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

    // set global max velocity for every Thing
    public static void setGlobalMaxVelocity(Number maxVelocity){
        globalMaxVelocity = maxVelocity;
    }

    // set global max force for every Thing
    public static void setGlobalMaxForce(Number maxForce){
        globalMaxForce = maxForce;
    }

    // set global radius for every Thing
    public static void setGlobalRadius(Number radius){
        globalRadius = radius;
    }

    // update Things location
    public void update() {
        // add acceleration to velocity
        velocity.add(acceleration);
        // limit velocity using maxVelocity
        velocity.limit(maxVelocity);
        // add velocity to location
        location.add(velocity);
        // clear acceleration
        acceleration.mult(0);
    }

    // update Thing location if it is of the edge
    public void edge(){
        if (location.x < 0)         location.x = p.width;
        if (location.x > p.width)   location.x = 0;
        if (location.y < 0)         location.y = p.height;
        if (location.y > p.height)  location.y = 0;
    }

    //apply force to Thing
    public void applyForce(PVector force) {
        // force = mass * acceleration
        // acceleration = force / mass
        PVector nForce = PVector.div(force, mass);
        acceleration.add(nForce);
    }

    // display Thing
    public void display(int type) {
        // regenerate color
        color.regenerate();
        switch (type){
            // draw ARROW like object
            case ARROW:
                //get direction of velocity
                float theta = velocity.heading() + PApplet.PI/2;
                p.fill(color.get());
                p.stroke(color.get());
                p.strokeWeight(1);
                p.pushMatrix();
                p.translate(location.x, location.y);
                p.rotate(theta);
                p.beginShape();
                p.vertex(0, -radius * 2);
                p.vertex(-radius, radius *2);
                p.vertex(radius, radius *2);
                p.endShape(PApplet.CLOSE);
                p.popMatrix();
                break;

            // draw CIRCLE like object
            case CIRCLE:
                p.fill(color.get());
                p.stroke(color.get());
                p.strokeWeight(1);
                p.pushMatrix();
                p.translate(location.x, location.y);
                p.ellipse(0, 0,radius*2,radius*2);
                p.popMatrix();
                break;
        }
    }

//    public PVector getLocation() {
//        return location;
//    }

    // move in direction of desired location
    private void steer(PVector desired){
        //apply steer Force
        applyForce(steerForce(desired));
    }
    // return force that makes Thing move in direction of desired location
    public PVector steerForce(PVector desired){
        // steer = desired - velocity
        PVector steer = PVector.sub(desired, velocity);
        //limit force using maxForce
        steer.limit(maxForce);
        return steer;
    }

    //TODO make thees weights be dynamic
    // apply behaviours with weight of each force
    public void applyBehaviors(ArrayList<Thing> vehicles) {
        if (p.isMouseIn()){
            PVector seekForce = seekForce(new PVector(p.mouseX,p.mouseY));
            seekForce.mult(0.5f);
            applyForce(seekForce);
        }

        PVector separateForce = separateForce(vehicles);
        separateForce.mult(2);
        applyForce(separateForce);

        PVector alignForce = alignForce(vehicles);
        alignForce.mult(1);
        applyForce(alignForce);
    }

    // return force that makes Thing move to some location
    public PVector seekForce(PVector to){
        // get desired force
        PVector desired = PVector.sub(to,location);
        // pass desired to steerForce function and return result
        return steerForce(desired);
    }
    // seek to some location
    public void seek(PVector to) {
        // apply force returned from seekForce
        applyForce(seekForce(to));
    }

    // return force that makes Thing seek to some location but it stops when target is reached
    public PVector arriveForce(PVector to){
        // get desired
        PVector desired = PVector.sub(to,location);
        //get distance between Thing and target
        float distance = desired.mag();
        // if Thing is in area where it needs to slow down
        if (distance < arriveDistance){
            // map distance between Thing end target  to velocity
            float m = PApplet.map(distance, 0, arriveDistance, 0, maxVelocity);
            // set magnitude
            desired.setMag(m);
        } else {
            // if not set magnitude maxVelocity
            desired.setMag(maxVelocity);
        }
        // return steer force from counted desired force
        return steerForce(desired);
    }

    // arrive to some location
    public void arrive(PVector to) {
        applyForce(arriveForce(to));
    }

    //returns force that makes Ting move away from some location
    public PVector fleeForce(PVector from){
        // force pointing from some location to Thing's location
        PVector desired = PVector.sub(location,from);
        float distance = desired.mag();
        // if distance is less then fleeDistance flee from that location
        if (distance < fleeDistance){
            // divide desired velocity by distance so if Thing is
            // too close to some location flee force will be stronger
            desired.div(distance);
            desired.limit(maxVelocity);
            return steerForce(desired);
        }else{
            // else return vector (0,0)
            return new PVector(0,0);
        }
    }
    // flee from some location
    public void flee(PVector from) {
        applyForce(fleeForce(from));
    }

    // return force that makes Thing move in direction of flow field
    public PVector followForce(FlowField flow) {
        PVector desired = flow.getDirection(location);
        desired.mult(maxVelocity);
        return  steerForce(desired);
    }
    // follow flow field
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
            return  steerForce(sum);
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
            return steerForce(sum);
        }else{
            return new PVector(0,0);
        }
    }
    public void separate(ArrayList<Thing> things) {
        applyForce(separateForce(things));
    }

}

package Tanks;

import processing.core.PApplet;
import processing.core.PImage;

import java.lang.Math;
import java.util.ArrayList;

public class Projectile {
    private double x;
    private double y;
    private double dx;
    private double dy;
    private final double angle;
    private final int[] colorlist;

    private final App parent;

    private final int FPS;
    private final double powerLevel;
    private Boolean explosion = false;
    private int countFrame = 0;
    private int increment = 0;
    private int incrementyellow = 0;
    private int incrementorange = 0;
    private double xcollision;
    private double ycollision;
    private Tank victimTank;
    private int radius;
    private Tank player;

    /**
     * @param parent App.java
     * @param x position x
     * @param y position y
     * @param powerLevel terrain power level when shooting
     * @param angle the angle of the turret
     * @param player the current player
     * @param colorlist list of int to pass into fill() to add color to the projectile making them matche the tank
     */
    public Projectile(App parent, double x, double y,double powerLevel, double angle, int[] colorlist, Tank player) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.powerLevel = powerLevel; // Ensure power level is within [0, 100]
        this.colorlist = colorlist;
        this.FPS = 30;
        // Calculate initial velocity based on power level
        double velocity = 6 * powerLevel;
        // Calculate initial horizontal and vertical velocity components
        
        this.dx = velocity * Math.sin(-angle) ; //CALCULATE X DIRECTION WITH ADDITIONIAL IMPACT LIKE POWERLEVEL
        this.dy = velocity * Math.cos(-angle);
        this.radius = 30;
        this.player = player;
    }

    /**
     * Changing the Radius (EXTENSION) we also change the increment(s) to illustrate the explosion
     */
    public void setRadius() {this.radius = 60;
                            incrementorange+=20;
                            increment+=35;
                            incrementyellow+=15;}


    public double getX() {return x;}
    public double getY() {return y;}

    /**
     * @return returning the count Frame as we only draw the explosion in 12 frames
     */
    public int getcountFrame() {return countFrame;}

    /**
     * @return the position x when projectile hit the terrain
     */
    public double getxcollision() {return xcollision;}

    /**
     * @return the position y when projectile hit the terrain
     */
    public double getycollision() {return ycollision;}

    /**
     * @return the current radius
     */
    public int getRadius() {return radius;}

    /**
     * find the victim 
     * @return the tank thats got shot
     */
    public Tank getvictimTank() {return victimTank;}

    /**
     * @param windValue the random wind value which would affect the tank
     * @param movingaverage the terrain heigh calculated by the average of the next 32px
     * @param tankList the array list of tanks object
     * @param parachute the parachute image
     */
    public void draw(int windValue, float[] movingaverage, ArrayList<Tank> tankList, PImage parachute) {
        // Apply gravity
        
        // Update projectile position
        if (!explosion){
        
            if (y < (double) movingaverage[(int) Math.round(x)]) {

            // Draw projectile
            parent.noStroke();
            parent.fill(colorlist[0], colorlist[1], colorlist[2]);
            parent.ellipse((float) x, (float) y,10, 10);

            dy += 3.6*2;
            dx += windValue*0.03/FPS;
            x += dx / FPS;
            y += dy / FPS;

            } else if (y >= (double) movingaverage[(int) Math.round(x)]) {  
                
                //PImage parachuteImg = parent.getParachutImage();
                
                for (int e = 0; e < radius*2; e++){
                    int index = (int) (e + x - radius);
                    if (index < 0) { index = 0;}
                    else if (index > 896) { index = 895;}
                    double ypoint =  Math.sqrt(radius*radius - (e-radius)*(e-radius));
                    double chord = ypoint;
                    double uppery = y - chord;
                    double lowery = y + ypoint;

                    double terrainHeight = 640 - movingaverage[index];
                    double loweryheight = 640 - lowery;
                    double upperyHeight = 640 - uppery;
                    
                    if (terrainHeight > upperyHeight) {
                        movingaverage[index] += (float) (2*chord);}
                    else if(terrainHeight <= upperyHeight && terrainHeight > loweryheight){
                        movingaverage[index] = (float) lowery;
                    }else{
                        continue;}
                    }
                    explosion = true;
                }
        }
        else { //DRAWING EXPLOSION
            xcollision = x;
            ycollision = y;

            countFrame++;
            increment+=4;
            incrementorange+=3;
            incrementyellow+=2;
            parent.noStroke();
            parent.fill(255, 0, 0); //red
            parent.ellipse((float) x, (float) y,10+increment, 10+increment);
            
            parent.fill(255, 153, 0); //orange
            parent.ellipse((float) x, (float) y,1+incrementorange, 1+incrementorange);

            parent.fill(255, 255, 0);  //yellow
            parent.ellipse((float) x, (float) y,0+incrementyellow, 0+incrementyellow);

            for (Tank tank: tankList){
                double distance = Math.sqrt((xcollision - tank.getX())*(xcollision - tank.getX()) + (ycollision - tank.getY())*(ycollision - tank.getY()));
                
                if (distance <= radius){
                    tank.setHealth((int) - distance*2/12);
                    player.setscore((int) distance*2/12);
                    parent.getscoreboard().put(player.getOrder(), player.getscore());
                }
                }
           
            } 
        }
    }


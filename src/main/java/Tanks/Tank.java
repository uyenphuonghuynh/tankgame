package Tanks;


import processing.core.PApplet;
import java.util.ArrayList;

import java.util.*;

public class Tank {
    /**
     * position x of tank
     */
    protected int x; 
    /**
     * position y of tank
     */
    protected float y;
    /**
     * the fuel of tank
     */
    protected int fuel;
    /**
     *  the health of tank
    */
    protected int health;
    /**
     *  the power of tank
    */
    protected int power;
    //protected int parachute = 3;
    /**
     *  the angle of tank
    */
    protected float angle; // Direction the tank is facing
    /**
     *  the speed of tank
    */
    protected float speed; // Speed of the tank
    /**
     *  the order of tank
    */
    protected String order;
    //protected int score;
    /**
     *  the color list of tank
    */
    private int[] colorlist;
    /**
     *  the board width
    */
    public static int WIDTH = 864; //CELLSIZE*BOARD_WIDTH;
    /**
     *  the board height
    */
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;
    private final App parent;
    /**
     *  the projectile list 
    */
    public ArrayList<Projectile> projectileList = new ArrayList<>();
    /**
     *  the cellsize 
    */
    public static final int CELLSIZE = 32;
    /**
     *  the cellheight
    */
    public static final int CELLHEIGHT = 32;
    /**
     *  the frame per second
    */
    public int FPS = 30;

    private int countFrameTank = 0;
    private int increment = 0;
    private int incrementyellow = 0;
    private int incrementorange = 0;
    private float[] movingaverage;

    /**
     * 
     * @param parent App.java
     * @param x position of the x coordinate
     * @param y position of the y coordinate
     * @param color the color reading from config 
     * @param order the order of the tank: A/B/C/D
     */
    public Tank(App parent, int x, int y, String color, String order) { //INITIALIZE TANK 
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.angle = 0;
        this.speed = 2; // Set a default speed
        this.colorlist = new int[3];
        this.order = order;
        this.fuel = 250;
        this.health = 100;
        this.power = 50;
        //this.score =0;
        //movingaverage = parent.getMovingAverage();
        String[] colorstringlist = color.split(",");
        for (int i =0; i < colorstringlist.length; i++){
            this.colorlist[i] = Integer.parseInt(colorstringlist[i]);
        }

    }

    public int getX() { return x; } //GET X POSITION
    public float getY() { return y; } //GET Y POSITION
    public float getAngle() { return angle; } //GET ANGLE
    public String getOrder() { return order; } //GET ORDER A, B, C ,D
    public int[] getcolorlist() { return colorlist; } //GET COLOR OF TANK
    public int getHealth() { return health; } // GET TANK'S HEALTH
    public int getPower() { return power; } // GET POWER
    public int getFuel() { return fuel; } // GET FUEL
    public int getParachute() { return parent.parachutenumber().get(this.getOrder()); } // GET THE PARACHUTE
    public int getscore() { return parent.getscoreboard().get(this.getOrder()); } // GET TANK'S SCORE

    public void setParachute(int p){parent.parachutenumber().put(this.getOrder(), parent.parachutenumber().get(this.getOrder()) + p);}; //INCREASE/DECREASE TANK'S PARACHUTE
    public void setX(int x) { this.x = x; } //SET NEW X POSITION
    public void setY(float y) { this.y = y; }//SET NEW Y POSITION

    /**
     * @param value when we need to adjust y 
     */
    public void modifyY(float value){this.y += value;}
    public void setscore(int score) { parent.getscoreboard().put(this.getOrder(), parent.getscoreboard().get(this.getOrder()) + score);} ;  //SET NEW SCORE ADDING/SUBSTRACTING
                        
    public void setHealth(int health) {this.health += health; //SET NEW HEALTH ADDING/SUBSTRACTING
                                        if (this.health > 100){this.health = 100;}}  
    public void setPower(int p) { //SET NEW POWER ADDING/SUBSTRACTING
        this.power += p;
        if (power > this.health){
            this.power = this.health;
        }
    }

    public void setFuel(int f){this.fuel+=200;} //SET NEW FUEL ADDING/SUBSTRACTING
    
    /**
     * Drawing the terrain using rectangles
     * Limit the angle using constrains and matrix
     */
    public void draw() { //DRAW THE TANK, TURRET
        // Body
        int x = this.x-13;
        parent.fill(this.colorlist[0], this.colorlist[1], this.colorlist[2]);
        parent.noStroke(); // No stroke to remove black lines
   
        parent.rect(x, y, 23, 5);
        
        // Upper body
        parent.fill(this.colorlist[0], this.colorlist[1], this.colorlist[2]);
        parent.rect(x+7, y-3, 13, 3);
        
        // // Turret
        parent.fill(0);

        parent.fill(0);
        parent.pushMatrix(); // preserve the current state of transformations
        parent.translate(x+13, y); // Move to turret origin
        parent.rotate(angle);

        angle = parent.constrain(angle, parent.PI - parent.PI/2, 2*parent.PI - parent.PI/2);
        //angle = parent.constrain(angle, (float) Math.PI/2, (float) 3*Math.PI/2);
        parent.rect(0, 0, 3, 15);
        parent.popMatrix(); //restore the transformation matrix to its state before the last call 
        //DRAW PARACHUTE
    }

    /**
     * Check player action and move the tank
     * @param keyPressed A Hash Set storing the integer value of the key that player press
     * @param movingaverage list of floating value of the terrain height
     */
    public void move(Set<Integer> keyPressed, float[] movingaverage) { //CHECK USER ACTION
        if (keyPressed.contains(37)) {
            this.x -= speed;
            this.fuel -=1;
        } else if (keyPressed.contains(39)) {
            this.x += speed;
            this.fuel -=1;
        }else if (keyPressed.contains(32)) {
            keyPressed.remove(32); // Remove spacebar from the pressed set
            return;
        }else if (keyPressed.contains(87)){ //Power up turret KEY W
            this.setPower(36/FPS);
        }else if (keyPressed.contains(83)){ //Power down turret KEY S
            this.setPower(-36/FPS);
        }
        
        this.y = movingaverage[x];// Height of terrain at x coordinate on map
    }
    
       
/**
 * Check player action and moving the turret
 * @param keyPressed  A Hash Set storing the integer value of the key that player press
 */
    public void turretMove(Set<Integer> keyPressed) {
        // Adjust the angle of the turret, to be implemented on your own
        if (keyPressed.contains(38)) {
            angle += 0.2; //UP KEY
            //turret.rotate(angle);
            parent.rotate(angle);            // Rotate the turret by the angle
            parent.rect(0, 0, 3, 15);       // Draw the turret at the rotated position
        } else if (keyPressed.contains(40)) {
            angle -= 0.2; //DOWN KEY      
        }else if (keyPressed.contains(32)) {
            return;
        }}
    
    /**
    * Illustrating the explosion on the tank 
    */
    public void explosion(){ //IMPLEMENT TANK EXPLOSION
        countFrameTank++;
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
}
}

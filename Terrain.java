package Tanks;

import processing.core.PApplet;

public class Terrain {
    /**
     * x position
     */
    protected int x;
    /**
     * y position
     */
    protected int y;
    /**
     * board width
     */
    public static int WIDTH = 864; //CELLSIZE*BOARD_WIDTH;
    /**
     * board height
     */
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;
    private final PApplet parent;

    /**
     * 
     * @param parent App.java
     * @param x position x of each px of the terrain
     * @param y position y of each px of the terrain
     */
    public Terrain(App parent, int x, int y) { //INITIALIZE TERRAIN
        this.parent = parent;
        this.x = x; 
        this.y = y;
    }

    public int getX() { return x; } //GET X POSITION
    public int getY() { return y; } //GET Y POSITION

    public void setX(int x) { this.x = x; } //SET NEW X POSITION
    public void setY(int y) { this.y = y; } //SET NEW Y POSITION

    /**
     * @param movingaverage the terrain heighs stored as each pixel in type float in a list
     */
    public void draw(float[] movingaverage) { //DRAW TERRAIN
        
        for (int i = 0; i < 32; i++) { //Looping throught the float[] 
            //parent.stroke(255); //stroke color
            parent.rect(x + i, movingaverage[x+i], 1,  HEIGHT); //drawing rectangle
        }
    } 
}

package Tanks;


public class Tree {
    /**
     * x position
     */
    protected int x;
    /**
     * the width
     */
    public static int WIDTH = 864; //CELLSIZE*BOARD_WIDTH;
    /**
     * the height
     */
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;

    /**
     * @param parent App.java
     * @param x position x of the tree (x coordinate)
     */
    public Tree(App parent, int x) { //INITIALIZE TREE
        this.x = x;
    }

    public int getX() { return x; } //getx

    public void setX(int x) { this.x = x; } //set new x
}

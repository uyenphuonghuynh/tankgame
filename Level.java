package Tanks;


import processing.data.JSONArray;
import processing.data.JSONObject;



import java.util.ArrayList;

import java.io.*;
import java.util.*;

public class Level {
    /**
     * the position x help us set the x position of Level, Tank, Terrain, Tree
     */
    protected int position_x;
    /**
     * the position y help us set the x position of Level, Tank, Terrain, Tree
     */
    protected int position_y;
    /**
     * Width of the screen in pixels
     */
    public static int WIDTH = 864; //CELLSIZE*BOARD_WIDTH;
    /**
     * Heigh of the screen in pixels
     */
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;

    /**
     * Cellsize
     */
    public static final int CELLSIZE = 32;

    /**
     * Cellheight
     */
    public static final int CELLHEIGHT = 32;

    /**
     * Object storing tanks' color
     */
    public JSONObject player_colours;

    /**
     * String storing the background img
     */
    public String background;

    /**
     * String storing the terrain color
     */
    public String foreground_colour;

    /**
     * int list for when we use the list for fill() function
     */
    public int[] foreground_colour_list = new int[3];

    /**
     * Array list layout
     */
    public JSONArray layout;

    /**
     * String treeimg to know which tree we are gonna take
     */
    public String treeimg;

    /**
     * Terrain list to return terrain objects 
     */
    public ArrayList<Terrain> terrainList = new ArrayList<>();

    /**
     * tree list to return terrain objects 
     */
    public ArrayList<Tree> treeList = new ArrayList<>();

    /**
     * Tank list to return terrain objects 
     */
    public ArrayList<Tank> tankList = new ArrayList<>();
   
    /**
     * final height to storing the original heigh of terrain and duplicate it to 32 times 
     */
    public float[] finalheight =new float[WIDTH+CELLSIZE];

    /**
     * Storing the final height of terrain for each pixel
     */
    public float[] movingaverage = new float[896];

    /**
     * ConfigPath
     */
    public String configPath;

/**
 * 
 * @param parent the App.java
 * @param file the level file we will be looping through (level1/level2/level3)
 * @param background the background imgages like dessert, basic, snow
 * @param foreground_colour the color of the terrain
 * @param treeimg the trees according to the level 
 * @throws FileNotFoundException if we can not find file
 */
    public Level(App parent, File file, String background, String foreground_colour, String treeimg) throws FileNotFoundException  {

        Scanner scan = new Scanner(file); //START SCANNING THE FILE
        position_x = 0;
        position_y =0;

        this.configPath = "config.json"; 
        JSONObject conf = parent.loadJSONObject(new File(this.configPath));

        player_colours = conf.getJSONObject("player_colours");
        this.background = background;
        this.treeimg = treeimg;
        String[] colorlist = foreground_colour.split(","); //SPLITTING THE COLOR AS IT IS A STRING OF 3 ELEMENTS
        for (int i = 0; i < colorlist.length; i++) {
            this.foreground_colour_list[i] = Integer.parseInt(colorlist[i]); //string to 
        }

        while (scan.hasNextLine()) {
            String[] line = scan.nextLine().split(""); //READING THE FILE
            position_x = 0; //set x to 0 after each line
            
        for (int j = 0; j < line.length && j < 29; j++) {
            
            if (line[j].equals("X")) { //CHECK IF THE LINE HAS X WHICH IS TERRAIN 
                Terrain x = new Terrain(parent, position_x, position_y);
                terrainList.add(x);
            } else if (line[j].equals("T")){  //CHECK IF THE LINE HAS T WHICH IS TREE 
                int random = new Random().nextInt(30);
                Tree t = new Tree(parent, position_x + random);
                treeList.add(t);
    
            }else if (line[j].equals("A")){ //CHECK IF THE LINE HAS A/B/C/D WHICH IS USER 
                Tank tank = new Tank(parent, position_x, position_y, player_colours.getString("A"), "A");
                tankList.add(tank); //add to the tank list
            }
            else if (line[j].equals("B")){//CHECK IF THE LINE HAS A/B/C/D WHICH IS USER 
                Tank tank = new Tank(parent, position_x, position_y, player_colours.getString("B"), "B");
                tankList.add(tank);//add to the tank list
            }
            else if (line[j].equals("C")){//CHECK IF THE LINE HAS A/B/C/D WHICH IS USER 
                Tank tank = new Tank(parent, position_x, position_y, player_colours.getString("C"), "C");
                tankList.add(tank);//add to the tank list
            }
            else if (line[j].equals("D")){//CHECK IF THE LINE HAS A/B/C/D WHICH IS USER 
                Tank tank = new Tank(parent, position_x, position_y, player_colours.getString("D"), "D");
                tankList.add(tank);//add to the tank list
            }
            position_x += App.CELLSIZE; //increment x
            }       
            position_y += 32; //increment y
        }
        scan.close(); //close file


            for (int count = 0; count < terrainList.size(); count++){ //START SMOOTHING TERRAIN
                int y = terrainList.get(count).getY(); //get y
                int x = terrainList.get(count).getX(); //get x
            for (int k = 0; k < 32; k++){
                finalheight[x+k] = y; //generate 1d array
            }

            }
        
            for (int k = 0; k < finalheight.length; k++) { //first smoothing process
                float sum = 0;
                if (k >= finalheight.length - 32){  //not calculate the 28th column
                    movingaverage[k] = finalheight[k];  //not calculate the 28th column
                    continue; //keep going not stop yet
            }
                for (int count = 0; count < 32; count++) { //calculate the next 32px
                    sum += finalheight[k + count]; //adding the next 32 heighs
                }
                sum = sum/32; //divide by 32
                movingaverage[k] = sum;
            }
        
        
            for (int k = 0; k < finalheight.length - 32; k++) {
            float sum = 0;
            if (k == finalheight.length - 32){  //not calculate the 28th column
            break; //if we reach this stay, we done so break
            }
            for (int count = 0; count < 32; count++) {  //calculate the next 32px
            sum += movingaverage[k + count]; //add the next column
            }
            sum = sum/32; //divide by 32
            movingaverage[k] = sum; //DONE SMOOTHING
            } 
        
    
        
            for (Tank tank: tankList){ //ADJUST TANK POSITION  
            float newY = movingaverage[tank.getX()]; //use variable

            tank.setY((movingaverage[tank.getX()])); //adjust y
            }
        }
        /**
         * 
         * @return list of tank objs
         */
        public ArrayList<Tank> getTankList(){return tankList;} //get tanklist
        /**
         * 
         * @return list of terrain objs
         */
        public ArrayList<Terrain> getTerainList(){return terrainList;} //get terrainlist
        /**
         * 
         * @return the terrain height for every pixels
         */
        public float[] getMovingAvergae(){return movingaverage;} //get the movingavergae

        /**
         * 
         * @return list of tree objs
         */
        public ArrayList<Tree> getTreeList(){return treeList;} //get the tree list
        /**
         * 
         * @return the background 
         */
        public String getbackground(){return background;} //get the background
        /**
         * 
         * @return the tree img in string
         */
        public String gettreeimg(){return treeimg;}

        /**
         * 
         * @return the terrain color list
         */
        public int[] getforeground_colour(){return foreground_colour_list;}  //get foreground
    }
    
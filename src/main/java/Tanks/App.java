package Tanks;




import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.ArrayList;


import java.io.*;
import java.util.*;


public class App extends PApplet {

/**
 * Loading snow img
 */
public PImage snow;
/**
 * Loading desert img
 */
public PImage desert;
/**
 * Loading snbasicow img
 */
public PImage basic;
/**
 * Loading forest img
 */
public PImage forest;
/**
 * Loading fuel img
 */
public PImage fuel;
/**
 * Loading hills img
 */
public PImage hills;
/**
 * Loading parachute img
 */
public PImage parachute;
/**
 * Loading tree img
 */
public PImage tree1;
/**
 * Loading tree img
 */
public PImage tree2;
/**
 * Loading wind_1 img
 */
public PImage wind_1;
/**
 * Loading wind img
 */
public PImage wind;

/**
 * Creating JSONArray layout
 */
public JSONArray layout;

/**
 * current player to loop throught the turns
 */
public int current_player;
/**
 * stroring the wind value
 */
public int windValue;
/**
 * just to count the arrow poiting to the tank
 */
public int countarrow = 0;


/**
 * storing the value of the key when player press
 */
public Set<Integer> keyPressed = new HashSet<>();
/**
 * storing the level objects
 */
public ArrayList<Level> levelList = new ArrayList<Level>();
/**
 * storing the projectiles 
 */
public ArrayList<Projectile> projectileList = new ArrayList<Projectile>();

/**
 * int value to loop through the levelList
 */
public int current_level = 0;
/**
 * detect when a projectile is fired to do explosion
 */
public boolean fired = false;
/**
 * if we r eligible to nect level
 */
public boolean nextlevel = false;
/**
 * EXTENSION larger projectile
 */
public boolean increaseRadius = false;

/**
 * Creatinf haspmap stroring the values that stay 
 * the same through out the levels like scores, parachutes,
 * tank color (using for scoreboard and informing winner)
 */

public HashMap<String, Integer> scoreboard = new HashMap<String, Integer>();

/**
 * Creatinf haspmap stroring the values that stay 
 * the same through out the levels like scores, parachutes,
 * tank color (using for scoreboard and informing winner)
 */
public HashMap<String, Integer> parachutenumber = new HashMap<String, Integer>();

/**
 * Creatinf haspmap stroring the values that stay 
 * the same through out the levels like scores, parachutes,
 * tank color (using for scoreboard and informing winner)
 */
public HashMap<String, int[]> playerscolorHashMap = new HashMap<String, int[]>();

/**
 * Finding the victim to decrease the health
 */
public Tank victimTank;
/**
 * cellsize
 */
public static final int CELLSIZE = 32;
/**
 * cellheight
 */
public static final int CELLHEIGHT = 32;

/**cellavg */
public static final int CELLAVG = 32;
/**the top bar */
public static final int TOPBAR = 0;
/**the width of the sccreen */
public static int WIDTH = 864; //CELLSIZE*BOARD_WIDTH;
/**the height of the screen */
public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;
/**the board_width */
public static final int BOARD_WIDTH = 864/CELLSIZE;
/**the board height */
public static final int BOARD_HEIGHT = 20;

/**the initial parachutes */
public static final int INITIAL_PARACHUTES = 1;
/**to get the parachute PImage */
public PImage getParachutImage(){return parachute;}

/**frame per second */
public static final int FPS = 30;

/**configpath */
public String configPath;

/**generate random */
public static Random random = new Random();
// Feel free to add any additional methods or attributes you want. Please put classes in different files.

/**
 * Loading configPath
 */
public App() {
this.configPath = "config.json";
}


/**
* Initialise the setting of the window size.
*/
@Override
public void settings() {
    size(WIDTH, HEIGHT);
}


/**
* Load all resources such as images. Initialise the elements such as the player and map elements.
* Creating level object
* Putting values into Hash Map
*/
@Override
public void setup() {
    frameRate(FPS);
    // See PApplet javadoc:
    // loadJSONObject(configPath)
    // loadImage(this.getClass().getResource(filename).getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
    this.basic = loadImage(this.getClass().getResource("basic.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
    this.desert = loadImage(this.getClass().getResource("desert.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
    this.forest = loadImage(this.getClass().getResource("forest.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
    this.fuel = loadImage(this.getClass().getResource("fuel.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
    
    this.parachute = loadImage(this.getClass().getResource("parachute.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
    this.hills = loadImage(this.getClass().getResource("hills.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
    this.snow = loadImage(this.getClass().getResource("snow.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
    this.tree1 = loadImage(this.getClass().getResource("tree1.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
    this.tree2 = loadImage(this.getClass().getResource("tree2.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
    tree1.resize(32,32);
    tree2.resize(32,32);
    this.wind_1 = loadImage(this.getClass().getResource("wind-1.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
    this.wind = loadImage(this.getClass().getResource("wind.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));

    fuel.resize(22,22);
    parachute.resize(32,32);

    JSONObject conf = loadJSONObject(new File(this.configPath));
    layout = conf.getJSONArray("levels");

    //player_colours = conf.getJSONObject("player_colours");


    windValue = (int) (random(-35, 35));


   
    for (int i = 0; i < layout.size(); i++){
        JSONObject level = layout.getJSONObject(i);
        String treeimg = level.getString("trees");
        System.out.println(treeimg);
         try{
            File file = new File(level.getString("layout"));

            String background = level.getString("background");
            String foreground_colour = level.getString("foreground-colour");
            Level l = new Level(this, file, background, foreground_colour, treeimg);
            levelList.add(l);
        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
            }
         }

        parachutenumber.put("A", 3);
        parachutenumber.put("B", 3);
        parachutenumber.put("C", 3);
        parachutenumber.put("D", 3);

        scoreboard.put("A", 0);
        scoreboard.put("B", 0);
        scoreboard.put("C", 0);
        scoreboard.put("D", 0);

        // Add player colors
        playerscolorHashMap.put("A", new int[]{0, 0, 255}); // Blue
        playerscolorHashMap.put("B", new int[]{255, 0, 0}); // Red
        playerscolorHashMap.put("C", new int[]{0, 255, 255}); // Cyan
        playerscolorHashMap.put("D", new int[]{255, 255, 0}); // Yellow
    }

/**
 *Comparator to sort out the Tank order  */ 
public static final Comparator<Tank> sortByOder = new Comparator<Tank>()    {
    public int compare(Tank a, Tank b) {
        return a.getOrder().compareTo(b.getOrder());
}
};

/**
 * 
 * @return the level we are at 
 */
public int getcurrentlevel(){ return current_level;}
/**
 * 
 * @return the level list storing level objects
 */
public ArrayList<Level> getLevels(){return levelList;}
//public float[] getMovingAverage() {return levelList.get(current_level).getMovingAvergae();}

/**
 * 
 * @return return the HashMap storing the score of each player
 */
public HashMap<String, Integer> getscoreboard() {return scoreboard;}
/**
 * 
 * @return parachute number of all player in a HAshMAp
 */
public HashMap<String, Integer> parachutenumber() {return parachutenumber;}


/**
* Receive key pressed signal from the keyboard.
*/
@Override
public void keyPressed(KeyEvent e){
    int key = e.getKeyCode();
    Level l = levelList.get(current_level);
    ArrayList<Tank> tankList = l.getTankList();
    Tank player = tankList.get(current_player);
   
    if (key == 32){ //spacebar code = 32

        Double projectileX = (player.getX() + Math.cos(player.getAngle()));
        Double projectileY = (player.getY() -15.0);
        //ArrayList<Projectile> projectileList = player.getProjectiles();
        Projectile p = new Projectile(this, projectileX, projectileY, player.getPower(),player.getAngle(), player.getcolorlist(), player);
        projectileList.add(p);
        
        System.out.print(tankList.size());
        System.out.println(current_player);

        if (increaseRadius){
            p.setRadius();
        }

        if (current_player == tankList.size() - 1){
            current_player = -1;}

        int random = (int) random(-5, 5);
        windValue+= random;
        countarrow = 0;
        current_player++;

        if (tankList.size()==1) {
            current_player =0;
            current_level ++;
            delay(1000);
        }

        increaseRadius = false;

    }else if (key == 82){ //REPAIR KIT KEY R 
        if ((player.getscore() >= 20) && (player.getHealth() <81)){
        player.setscore(-20);
        player.setHealth(20);}

    }else if (key == 70){ //REPAIR KIT KEY F
        if (player.getscore() >= 10){
        player.setscore(-10);
        player.setFuel(200);}

    }else if (key == 80){  //ADDITIONAL PARACHUTE KEY P
        if (player.getscore() >= 15){
            player.setscore(-15);
            player.setParachute(1);}

    }else if (key == 88){  //INCREASE RADIUS
        if (player.getscore() >= 20){
            player.setscore(-20);
            increaseRadius = true;
        }
    }else if (key == 82){ //RESTART THE GAME
        current_level = 0;
        current_player = 0;
        setup();
        
    }
        keyPressed.add(key);
    }


/**
* Receive key released signal from the keyboard.
*/
@Override
public void keyReleased(KeyEvent e){
    int key = e.getKeyCode();
    keyPressed.remove(key);
}



/**
* Draw all elements in the game by current frame.
*/
@Override
public void draw() {
    int strokeWeight = 2;
    

        if (current_level < levelList.size()){
            JSONObject level = layout.getJSONObject(current_level);

            Level l = levelList.get(current_level);
            ArrayList<Tank> tankList = l.getTankList();
            float[] movingaverage = l.getMovingAvergae();
            ArrayList <Tree> treeList = l.getTreeList();
            ArrayList<Terrain> terrainList =l.getTerainList();
            
            String background = l.getbackground();
            String treeimg = l.gettreeimg();    

            int[] foreground_colour_list = l.getforeground_colour();

            if (background.contains("snow")){
            image(this.snow, 0, 0);}
            else if(background.contains("desert")){
                image(this.desert, 0, 0);}
            else if(background.contains("basic")){
                image(this.basic, 0, 0);}


            Tank player = tankList.get(current_player);

            tankList.sort(sortByOder);

            fill(color(50));
            text("Player " +player.getOrder()+"'s turn",CELLSIZE*1, 32);

            image(fuel, CELLSIZE*5, 10);
            text(player.getFuel(), CELLSIZE*6, CELLHEIGHT);

            image(parachute, CELLSIZE*5, CELLHEIGHT*1 +CELLHEIGHT/2);
            text(parachutenumber.get(player.getOrder()), CELLSIZE*6, CELLHEIGHT*2);



            //HEALTH BAR
            int[] colorlist = player.getcolorlist();
            stroke(0);
            fill(colorlist[0], colorlist[1], colorlist[2]);
            rect(CELLSIZE*12, 10, 100, 22);
            fill(0);
            rect( CELLSIZE*12 + player.getHealth(), 10, 3, 22);
            text("Health:", CELLSIZE*10, 32);
            text(player.getHealth(),CELLSIZE*12+5 + 100, 32);

            //SCORE BOARD
            stroke(0);
            noFill();
            strokeWeight(strokeWeight);
            rect(680, 80, CELLSIZE*5, CELLHEIGHT*4);
            line(680, 120, 680 + CELLSIZE*5, 120);
            fill(color(50));

            

            text("Scoreboard", 690, 110);

            fill(0, 0, 255);
            strokeWeight(5);
            text("Player A ", 690, 140);
            fill(255, 0, 0);
            text("Player B", 690, 160);
            fill(0,255,255);
            text("Player C", 690, 180);
            fill(255,255,0);
            text("Player D", 690, 200);

            fill(color(50));

            //TODO
            text((scoreboard.get("A")),790 ,140); //score A
            text((scoreboard.get("B")),790 ,160);
            text((scoreboard.get("C")),790 ,180);
            text((scoreboard.get("D")),790 ,200);

            // DRAW POWER
            strokeWeight(2);
            fill(color(50));
            text("Power: "+ player.getPower(), CELLSIZE*10, CELLHEIGHT*2);


            //DRAW TERRAIN 
            
            noStroke();
            strokeWeight(1);
            fill(foreground_colour_list[0], foreground_colour_list[1], foreground_colour_list[2]);
            for (Terrain block: terrainList) {
                block.draw(movingaverage);
            }

            // DRAW TREE
            if (treeimg != null){
                if (treeimg.equals("tree1.png")){
                    for (Tree tree: treeList){
                    image(tree1, tree.getX()-16, movingaverage[tree.getX()]-32);}
                }else{
                    for (Tree tree: treeList){
                        image(tree2, tree.getX()-16, movingaverage[tree.getX()]-32);
                    }
            }}

            //WIND ICON AND DISPLAY
            if (windValue>0) {image(wind, WIDTH -32*5, 10);}
            else{image(wind_1, WIDTH -32*5, 10);}
            textSize(15);
            fill(0);
            text(windValue,WIDTH - 32*2, 32+32/2);

            //DRAW TANK
            for (Tank tank: tankList){
                tank.draw();}
        

            //ARROW
            if (countarrow <= 60){
                countarrow+=2;
                fill(0);
                rect(player.getX(), player.getY() - 2*CELLSIZE, 2, 35); //MAIN BODY OF AN ARROW

                stroke(5);
                fill(0);
                line(player.getX() - 10, player.getY() - 1*CELLSIZE - 10, player.getX(), player.getY() - 1*CELLSIZE +5);
                
                stroke(5);
                fill(0);
                line(player.getX(), player.getY() - 1*CELLSIZE +5, player.getX() + 10, player.getY() - 1*CELLSIZE - 10);
            }
            
            //----------------------------------
            //----------------------------------


            //TODO: Check user action

            player.move(keyPressed, movingaverage);
            player.turretMove(keyPressed);
            
            //Tank victimTank;
            if (projectileList.size() >0){
                for (int i = 0; i < projectileList.size(); i++){
                    Projectile p = projectileList.get(i);
                    p.draw(windValue, movingaverage, tankList, parachute);

                    if ((p.getX() <= 0) || (p.getX() >= WIDTH)){
                        projectileList.remove(i);
                    }

                    if (p.getcountFrame() == 12){ //EXPLOSION ANIMATION
                        projectileList.remove(p);
                        break;}   
                    }        
                }

            for (Tank tank: tankList){
                //PARACHUTE 
                int distance = (int)( movingaverage[tank.getX()] - tank.getY());
                if ((tank.getY()) < (movingaverage[tank.getX()]) && tank.getParachute() > 0){
                    tank.modifyY(+1);
                   
                    if(tank != player) {
                        player.setscore(1);
                        scoreboard.put(player.getOrder(), player.getscore());}

                    if (distance >0){tank.setParachute(-1/distance);}
                    image(parachute, tank.getX() - 16, tank.getY() - 34);
                    parachutenumber.put(tank.getOrder(), tank.getParachute());
                }

                if (tank.getY() < movingaverage[tank.getX()] && tank.getParachute() < 0){
                    tank.modifyY(+2);
                    
                    tank.setHealth(-2);

                    if (tank!= player) {
                        player.setscore(2);
                        scoreboard.put(player.getOrder(), player.getscore());}
                }
                
            }
            for (int z = 0; z < tankList.size(); z++){
                Tank tank = tankList.get(z);
                if (tank.getHealth() < 0 || tank.getY() > HEIGHT){
                    tank.explosion();
                    tankList.remove(tank);
                    current_player--;

                    if (current_player<=0){current_player = 0;}
                    z--;
                    //current_player--;
                    }

                if (tankList.size() == 1){

                    if (current_level==2){
                        
                        
                        //textAlign(CENTER, CENTER);
                        String maxKey = null;
                        int maxValue = 0; // Initialize with the smallest possible integer value
                        for (Map.Entry<String, Integer> entry : scoreboard.entrySet()) {
                            if (entry.getValue() > maxValue) {
                                maxValue = entry.getValue();
                                maxKey = entry.getKey();
                            }
                        }
                        int[] colorlistfinal = new int[3]; 
                        colorlistfinal = playerscolorHashMap.get(maxKey);
                        fill(colorlistfinal[0], colorlistfinal[1],colorlistfinal[2]);
                        text("Player "+maxKey+" wins!", CELLSIZE*10, CELLHEIGHT*5);

                        fill(colorlistfinal[0], colorlistfinal[1],colorlistfinal[2], 100);
                        rectMode(CENTER);
                        rect(WIDTH - CELLSIZE*15, HEIGHT - CELLSIZE*10, CELLSIZE*7, CELLHEIGHT*6);
                        
                        // Convert HashMap to a list of Map.Entry objects
                        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(scoreboard.entrySet());
                        Comparator<Map.Entry<String, Integer>> valueComparator = (entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue());

                        entryList.sort(valueComparator);
                        
                        //line(WIDTH - CELLSIZE*12, HEIGHT - CELLSIZE*10, WIDTH - CELLSIZE*12 + CELLSIZE*5, HEIGHT - CELLSIZE*10);

                        int increase = 0;
                        for (Map.Entry<String, Integer> entry : entryList) {
                            increase+=30;
                            fill(playerscolorHashMap.get(entry.getKey())[0], playerscolorHashMap.get(entry.getKey())[1], playerscolorHashMap.get(entry.getKey())[2]);
                            text("Player "+entry.getKey() + " : " + entry.getValue(), CELLSIZE*10, CELLSIZE*7 + increase);
                            
                        }

                    }

                    current_player = 0;
                    current_level++;
                    break;
                }
                    
                 
                }
                //TANK EXPLOSION   
            }
            

            
            
    }

    /**
     * Main function
     * @param args args
     */
    public static void main(String[] args) {
        PApplet.main("Tanks.App"); //Runing
        }
    }






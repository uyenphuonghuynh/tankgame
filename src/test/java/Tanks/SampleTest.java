package Tanks;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processing.event.KeyEvent;
import processing.core.PApplet;

public class SampleTest {

   //private App app;

    @Test
    public void simpleTest(){
        App app = new App();
        assertEquals(app.configPath, "config.json");

        

    }

@BeforeEach
void setUp() {
    App app = new App();
    app.loop();
    PApplet.runSketch(new String[] { "App" }, app);
    app.setup();
    app.delay(1000); 

    app.main(new String[] { "App" });
    
    app.keyPressed(new KeyEvent(app, 0, 0, 0, (char) PApplet.LEFT, 37));
    app.keyPressed(new KeyEvent(app, 0, 0, 0, (char) PApplet.RIGHT, 39));
    app.keyReleased(new KeyEvent(app, 0, 0, 0, (char) PApplet.RIGHT, 39));
    app.keyPressed(new KeyEvent(app, 0, 0, 0, (char) PApplet.DOWN, 40));
    app.keyPressed(new KeyEvent(app, 0, 0, 0, (char) PApplet.UP, 38));
    app.keyPressed(new KeyEvent(app, 0, 0, 0, (char) PApplet.X,88));
    
    // app.keyPressed(new KeyEvent(app, 0, 0, 0, 'R',82));
    // app.keyPressed(new KeyEvent(app, 0, 0, 0, 'W',87));
    // app.keyPressed(new KeyEvent(app, 0, 0, 0, 'P',80));

    
    app.keyReleased(new KeyEvent(app, 0, 0, 0, (char) PApplet.DOWN, 40));
    app.keyReleased(new KeyEvent(app, 0, 0, 0, (char) PApplet.UP, 38));
    app.keyReleased(new KeyEvent(app, 0, 0, 0, (char) PApplet.X, 88));
    app.keyReleased(new KeyEvent(app, 0, 0, 0, (char) PApplet.LEFT, 37));
    app.keyPressed(new KeyEvent(app, 0, 0, 0, ' ',32));
    
    // app.keyReleased(new KeyEvent(app, 0, 0, 0, 'P',80));
    // app.keyReleased(new KeyEvent(app, 0, 0, 0, 'R',82));
    // app.keyReleased(new KeyEvent(app, 0, 0, 0, 'W',87));
    

    

   
    Tank player = new Tank(app, 100, 200, "0,0,255", "A");

    Tree tree = new Tree(app, 244);
    tree.setX(345);
    Terrain terrain = new Terrain(app, 0, 0);
    terrain.getX();
    terrain.getY();
    terrain.setX(56);
    terrain.setY(567);


    Projectile p = new Projectile(app, 0, 0, 0, 0, null, null);
        double initialX = p.getX();
        double initialY = p.getY();
        //p.draw(0, new float[App.BOARD_WIDTH], null, null);
        p.setRadius();
        p.getcountFrame();
        p.getX();
        p.getY();
        p.getvictimTank();
        p.getxcollision();
        p.getycollision();
    
        player.getFuel();
        player.getAngle();
        player.getHealth();
        player.getOrder();
        player.getParachute();
        player.getPower();
        player.getcolorlist();
        player.getscore();
        player.getX();
        player.getY();
        player.setFuel(0);
        player.setHealth(10);
        player.setParachute(2);
        player.setPower(23);
        player.setX(267);
        player.setY(345);
        player.setscore(234);
        player.explosion();
        
        // Projectile position should change after drawing
       
        //p.draw(0, null, null, null);
        assertEquals(0, p.getX());
        assertEquals(0, p.getY());

        assertEquals(60, p.getRadius());
        assertEquals(0, player.getcolorlist()[0]);

        app.getLevels();
        app.getParachutImage();
        app.getcurrentlevel();
        app.getscoreboard();
       

        
    }

    



    @Test
    void testInitialScoreboard() {
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);

        assertEquals(0, app.getscoreboard().get("A"));
        assertEquals(0, app.getscoreboard().get("B"));
        assertEquals(0, app.getscoreboard().get("C"));
        assertEquals(0, app.getscoreboard().get("D"));
    }

    @Test
    void testInitialParachuteNumber() {
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);
        assertEquals(3, app.parachutenumber().get("A"));
        assertEquals(3, app.parachutenumber().get("B"));
        assertEquals(3, app.parachutenumber().get("C"));
        assertEquals(3, app.parachutenumber().get("D"));
    }

    @Test
    void testPlayerColors() {
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);
        assertArrayEquals(new int[]{0, 0, 255}, app.playerscolorHashMap.get("A"));
        assertArrayEquals(new int[]{255, 0, 0}, app.playerscolorHashMap.get("B"));
        assertArrayEquals(new int[]{0, 255, 255}, app.playerscolorHashMap.get("C"));
        assertArrayEquals(new int[]{255, 255, 0}, app.playerscolorHashMap.get("D"));

    }

    @Test
    void testInitialLevel() {
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);
        assertEquals(0, app.getcurrentlevel());
    }

    @Test
    void testGetLevels() {
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);
        assertNotNull(app.getLevels());
        }
    }

//gradle test jacocoTestReport
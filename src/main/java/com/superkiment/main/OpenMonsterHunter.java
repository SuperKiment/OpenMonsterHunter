package com.superkiment.main;

import com.superkiment.globals.Scale;
import com.superkiment.globals.Time;
import com.superkiment.main.GameManager.GameState;
import com.superkiment.world.ConnectionToWorld;
import processing.core.PApplet;
import processing.data.JSONObject;

// import java.io.FileOutputStream;
// import java.io.PrintStream;

/**
 * Main class to launch the application. Is the PApplet everything the client
 * sees is drawn on.
 */
public class OpenMonsterHunter extends PApplet {

    /**
     * Stores the current state of the application
     */
    public static GameManager gameManager;

    /**
     * The game created and played when the connection with a world is established
     */
    public static Game game;

    /**
     * The player's username, passed to the world and seen by others.
     */
    public String playerName = "Player" + (int) random(10000);

    /**
     * The connection to the world, used to communicate with the server (world).
     */
    public ConnectionToWorld connectionToWorld;

    /**
     * Handles the UI : Buttons, Text inputs, etc.
     */
    UI ui;

    /**
     * Boolean to define whether the game is in test mode or not, default is false.
     */
    private boolean testMode = false;

    public static void main(String[] args) {
        try {
            PApplet.main("com.superkiment.main.OpenMonsterHunter");
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    @Override
    public void settings() {
        size(800, 800, P3D);
        smooth();
    }

    @Override
    public void setup() {
        frameRate(500);
        // surface.setResizable(true);
        surface.setTitle(com.superkiment.res.Texts.getOneRandomSplashText());
        /*
         * 
         * PrintStream outStream = null;
         * PrintStream errStream = null;
         * try {
         * outStream = new PrintStream(new FileOutputStream("outputConsole.log"));
         * errStream = new PrintStream(new FileOutputStream("errorConsole.log"));
         * } catch (Exception e) {
         * println("sheeesh");
         * }
         * println("lez gooo");
         * System.setOut(outStream);
         * System.setErr(errStream);
         */

        setupVariables();
    }

    public void setupVariables() {
        gameManager = new GameManager();
        ui = new UI(gameManager, this);
        println(playerName);
    }

    @Override
    public void draw() {

        Time.Update(this);

        if (connectionToWorld != null && connectionToWorld.client.ip() == null) {
            connectionToWorld = null;
            println("Déconnecté");
        }

        if (gameManager.gameState == GameState.GAME) {
            Time.Stop(this);
            if (!this.testMode)
                game.Render();
            Time.UpdateDraw(this);
            game.Update();
        } else {
            if (!this.testMode)
                background(0);
        }

        if (!this.testMode)
            ui.Render(this);

    }

    @Override
    public void mousePressed() {
        ui.Click(mouseX, mouseY);
    }

    @Override
    public void keyPressed() {
        pressKey(key);
    }

    /**
     * Intermediate funtion to test key pressing
     * 
     * @param key
     */
    public void pressKey(char key) {
        if (key != CODED)
            ui.Key(key);

        if (game != null) {
            game.keyPressed(key);

        }
    }

    @Override
    public void keyReleased() {
        releaseKey(key);
    }

    /**
     * Intermediate funtion to test key releasing
     * 
     * @param key
     */
    public void releaseKey(char key) {
        if (game != null) {
            game.keyReleased(key);
        }
    }

    /**
     * Creates a local world by launching a PApplet from the
     * com.superkiment.world.World class.
     * 
     * @param name the name of the window
     */
    public void CreateWorld(String name, boolean render) {
        PApplet.runSketch(new String[] { "MondeServerLocal" }, new com.superkiment.world.World(name, render));
        delay(500);
        ConnectToWorld("127.0.0.1");
    }

    /**
     * Connect the connectionToWorld to a world with a certain IP address.
     * 
     * @param ip the ip as '000.000.000.000'
     */
    public void ConnectToWorld(String ip) {
        game = new Game(this, null);
        connectionToWorld = new ConnectionToWorld(this, ip, game);
    }

    /**
     * Sets the game's controllable player
     * 
     * @param player
     */
    public void setControllablePlayer(JSONObject player) {
        game.setControllablePlayer(player);
    }

    public void windowResized() {
        Scale.UpdateGameScale(this);
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

}

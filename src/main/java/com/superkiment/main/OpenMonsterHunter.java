package com.superkiment.main;

import com.superkiment.globals.Scale;
import com.superkiment.globals.Time;
import com.superkiment.main.GameManager.GameState;
import com.superkiment.world.ConnectionToWorld;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.io.FileOutputStream;
import java.io.PrintStream;

public class OpenMonsterHunter extends PApplet {

    public static GameManager gameManager;
    public static Game game;
    public static float drawTime = 0;
    public String playerName = "Player" + (int) random(10000);
    ConnectionToWorld connectionToWorld;
    UI ui;

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
        frameRate(800);
        // surface.setResizable(true);
        surface.setTitle(com.superkiment.res.Texts.getOneRandomSplashText());

        PrintStream outStream = null;
        PrintStream errStream = null;
        try {
            outStream = new PrintStream(new FileOutputStream("outputConsole.log"));
            errStream = new PrintStream(new FileOutputStream("errorConsole.log"));
        } catch (Exception e) {
            println("sheeesh");
        }
        println("lez gooo");
        System.setOut(outStream);
        System.setErr(errStream);

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
            game.Render();
            game.Update();
        } else {
            background(0);
        }

        ui.Render(this);
        if (Time.lastTime > 0) {
            drawTime = millis() - Time.lastTime;
        }

    }

    @Override
    public void mousePressed() {
        ui.Click(mouseX, mouseY);
    }

    @Override
    public void keyPressed() {
        if (key != CODED)
            ui.Key(key);

        if (game != null) {
            game.keyPressed(key);

        }
    }

    @Override
    public void keyReleased() {
        if (game != null) {
            game.keyReleased(key);
        }
    }

    public void CreateWorld(String name) {
        PApplet.runSketch(new String[] { "MondeServerLocal" }, new com.superkiment.world.World(name, true));
        delay(500);
        game = new Game(this, null);
        connectionToWorld = new ConnectionToWorld(this, "127.0.0.1", game);
    }

    public void ConnectToWorld(String ip) {
        game = new Game(this, null);
        connectionToWorld = new ConnectionToWorld(this, ip, game);
    }

    public void setControllablePlayer(JSONObject player) {
        game.setControllablePlayer(player);
    }

    public void windowResized() {
        Scale.UpdateGameScale(this);
    }

}

package main;

import java.io.FileOutputStream;
import java.io.PrintStream;

import logic.Player;
import main.GameManager.GameState;
import processing.core.*;
import processing.data.JSONObject;
import world.ConnectionToWorld;

public class OpenMonsterHunter extends PApplet {

	ConnectionToWorld connectionToWorld;
	UI ui;
	public static GameManager gameManager;
	public static Game game;
	public String playerName = "Player" + (int) random(10000);
	public static float deltaTime = 0, lastTime = -1, drawTime = 0;

	public static void main(String[] args) {
		try {
			PApplet.main("main.OpenMonsterHunter");
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	public void settings() {
		size(800, 800, P3D);
		smooth();
	}

	public void setup() {
		PrintStream outStream = null;
		PrintStream errStream = null;
		try {
			outStream = new PrintStream(new FileOutputStream("output.log"));
			errStream = new PrintStream(new FileOutputStream("error.log"));
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

	public void draw() {

		if (lastTime > 0) {
			deltaTime = millis() - lastTime;
			lastTime = millis();
		} else {
			lastTime = millis();
		}

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
		if (lastTime > 0) {
			drawTime = millis() - lastTime;
		}

	}

	public void mousePressed() {
		ui.Click(mouseX, mouseY);
	}

	public void keyPressed() {
		if (key != CODED)
			ui.Key(key);

		if (game != null) {
			game.keyPressed(key);

			
		}
	}

	public void keyReleased() {
		if (game != null) {
			game.keyReleased(key);
		}
	}

	public void CreateWorld(String name) {
		PApplet.runSketch(new String[] { "MondeServerLocal" }, new world.World(name, true));
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

}

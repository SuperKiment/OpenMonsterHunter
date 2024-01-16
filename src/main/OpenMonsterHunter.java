package main;

import main.GameManager.GameState;
import processing.core.*;
import world.ConnectionToWorld;

public class OpenMonsterHunter extends PApplet {

	ConnectionToWorld connectionToWorld;
	UI ui;

	public static GameManager gameManager;

	public static Game game;

	public String playerName = "Player" + (int) random(10000);

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
		gameManager = new GameManager();
		ui = new UI(gameManager, this);
		println(playerName);
	}

	public void draw() {

		if (connectionToWorld != null && connectionToWorld.client.ip() == null) {
			connectionToWorld = null;
			println("bruh");
		}

		background(0);
		if (gameManager.gameState == GameState.GAME) {
			game.Render();
		}
		ui.Render(this);
	}

	public void mousePressed() {
		ui.Click(mouseX, mouseY);
	}

	public void keyPressed() {
		if (key != CODED)
			ui.Key(key);
	}

	public void CreateWorld(String name) {
		PApplet.runSketch(new String[] { "MondeServerLocal" }, new world.World(name, true));
		delay(500);
		connectionToWorld = new ConnectionToWorld(this, "127.0.0.1");
		game = new Game(this, connectionToWorld);
	}

	public void ConnectToWorld(String ip) {
		connectionToWorld = new ConnectionToWorld(this, ip);
		game = new Game(this, connectionToWorld);
	}

}

package main;

import logic.Player;
import processing.core.*;
import world.EntityManager;

public class Game {

	private PApplet pap;
	public world.ConnectionToWorld connexion;
	public Player controlledPlayer;
	public EntityManager entityManager;

	Game(PApplet p, world.ConnectionToWorld connexion) {
		pap = p;
		this.connexion = connexion;
		entityManager = new EntityManager();
	}

	public void Update() {
		if (connexion != null && connexion.isConnected()) {
			connexion.Update();
		}

	}

	void keyPressed(char key) {
		if (key == 'd') {
			controlledPlayer.pos.x++;
		}
	}

	public void Render() {
		if (connexion != null && connexion.isConnected()) {
			pap.background(0);
			pap.push();
			pap.fill(255);
			pap.text("Connexion : " + connexion.client.ip(), 50, 100);
			pap.pop();

			DisplayGame();
		} else {
			pap.background(0);
			pap.push();
			pap.fill(255);
			pap.text("Pas de connexion au monde", 50, 100);
			pap.pop();
		}
	}

	private void DisplayGame() {
		// Background

		// Entities
		pap.circle(controlledPlayer.pos.x, controlledPlayer.pos.y, 10);

		// UI
	}
}

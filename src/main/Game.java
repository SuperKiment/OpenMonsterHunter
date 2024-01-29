package main;

import logic.Player;
import processing.core.*;

public class Game {

	private PApplet papplet;
	private world.ConnectionToWorld connexion;
	public Player controlledPlayer;

	Game(PApplet p, world.ConnectionToWorld connexion) {
		papplet = p;
		this.connexion = connexion;
	}

	public void Update() {
		if (connexion != null && connexion.isConnected()) {
			connexion.Update();
		}

	}

	public void Render() {
		if (connexion != null && connexion.isConnected()) {
			papplet.background(0);
			papplet.push();
			papplet.fill(255);
			papplet.text("Connexion : " + connexion.client.ip(), 50, 100);
			papplet.pop();
		} else {
			papplet.background(0);
			papplet.push();
			papplet.fill(255);
			papplet.text("Pas de connexion au monde", 50, 100);
			papplet.pop();
		}
	}
}

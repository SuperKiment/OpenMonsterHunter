package main;

import processing.core.*;

public class Game {
	
	PApplet papplet;
	world.ConnectionToWorld connexion;

	Game(PApplet p, world.ConnectionToWorld connexion) {
		papplet = p;
		this.connexion = connexion;
	}

	public void Update() {
		if (connexion != null && connexion.isConnected()) {

		}

	}

	public void Render() {
		if (connexion != null && connexion.isConnected()) {
			papplet.background(0);
			papplet.push();
			papplet.fill(255);
			papplet.text("Connexion : "+connexion.client.ip(), 50, 50);
			papplet.pop();
		}else {
			papplet.background(0);
			papplet.push();
			papplet.fill(255);
			papplet.text("Pas de connexion au monde", 50, 50);
			papplet.pop();
		}
	}
}

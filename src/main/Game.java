package main;

import logic.Entity;
import logic.Player;
import processing.core.*;
import processing.data.JSONObject;
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
		try {
			// Background

			// Entities
			pap.fill(255);
			pap.circle(controlledPlayer.pos.x, controlledPlayer.pos.y, 10);

			pap.fill(255, 0, 0);
			for (Player p : entityManager.getPlayers()) {
				pap.circle(p.pos.x, p.pos.y, 10);

			}

			pap.fill(0, 255, 0);
			for (Entity e : entityManager.getEntities()) {
				pap.circle(e.pos.x, e.pos.y, 10);

			}
			// UI
		} catch (Exception e) {

		}
	}

	public void TraiterData(JSONObject data) {

		entityManager.addIfInexistant(data);
		
		entityManager.updatePositions(data);
		
		for (int i = 0; i < data.getJSONArray("logic.Player").size(); i++) {
			JSONObject playerJSON = data.getJSONArray("logic.Player").getJSONObject(i);
			
			

		}
	}
}

package main;

import java.util.function.Function;

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
	public Entity focusedEntity;

	Game(PApplet p, world.ConnectionToWorld connexion) {
		pap = p;
		this.connexion = connexion;
		entityManager = new EntityManager();
	}

	public void Update() {
		if (connexion != null && connexion.isConnected()) {
			connexion.Update();
		}

		controlledPlayer.Update();
	}

	void keyPressed(char key) {
		if (!Console.console.actif) {
			if ((key == 'd' || key == 'q' || key == 's' || key == 'z') && !Console.console.actif) {
				controlledPlayer.keyPressed(key);
			}

			// Tests d'entités
			if (key == 'l') {
				logic.Dog e = new logic.Dog();
				e.pos.set(200, 200);
				// Envoyer données nouvelle entité

				connexion.EnvoiDonneesNouvelleEntite(e.getJSON());
			}

			if (key == '\n') {
				Console.console.Toggle();
			}
		} else {

			if (key == '\n') {
				Console.console.Enter();
			}
		}

	}

	void keyReleased(char key) {
		if (key == 'd' || key == 'q' || key == 's' || key == 'z') {
			controlledPlayer.keyReleased(key);
		}
	}

	public void Render() {
		if (connexion != null && connexion.isConnected()) {
			pap.background(0, 50, 10);
			pap.push();
			pap.fill(255);
			pap.text("Connexion : " + connexion.client.ip(), 50, 100);
			pap.pop();

			pap.push();

			pap.translate(-controlledPlayer.pos.x + pap.width / 2, -controlledPlayer.pos.y + pap.height / 2);

			DisplayGame();

			pap.pop();
		} else {
			pap.background(0);
			pap.push();
			pap.fill(255);
			pap.text("Pas de connexion au monde", 50, 100);
			pap.pop();
		}
	}

	private void DisplayGame() {
		// try {

		// Background
		DisplayGrid();

		// Entities
		// TODO un hashmap class,function qui affiche en fonction de la classe
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
		// TODO Utiliser les Renders
		// UI

		// } catch (Exception e) {
		// System.out.println(e);
		// }
	}

	private void DisplayGrid() {
		pap.push();
		pap.stroke(125, 125);
		int tailleGrid = 50;
		float posEcranGauche = (controlledPlayer.pos.x - pap.width / 2)
				- (controlledPlayer.pos.x - pap.width / 2) % tailleGrid;
		float posEcranDroite = (controlledPlayer.pos.x + pap.width / 2)
				- (controlledPlayer.pos.x + pap.width / 2) % tailleGrid;

		float posEcranHaut = (controlledPlayer.pos.y - pap.height / 2)
				- (controlledPlayer.pos.y - pap.height / 2) % tailleGrid;
		float posEcranBas = (controlledPlayer.pos.y + pap.height / 2)
				- (controlledPlayer.pos.y + pap.height / 2) % tailleGrid;

		for (float x = posEcranGauche; x < posEcranDroite; x += tailleGrid) {
			pap.line(x, posEcranHaut, x, posEcranBas);
		}

		for (float y = posEcranHaut; y < posEcranBas; y += tailleGrid) {
			pap.line(posEcranGauche, y, posEcranDroite, y);
		}

		pap.pop();
	}

	public void TraiterData(JSONObject data) {

		entityManager.addIfInexistant(data);

		entityManager.updatePositions(data);

		for (int i = 0; i < data.getJSONArray("logic.Player").size(); i++) {
			JSONObject playerJSON = data.getJSONArray("logic.Player").getJSONObject(i);

		}
	}

	public void setControllablePlayer(JSONObject player) {
		controlledPlayer = entityManager.addControllablePlayer(player);
		setFocusedEntity(controlledPlayer);
		System.out.println("controlled player : " + controlledPlayer.name);
	}

	public void setFocusedEntity(Entity e) {
		focusedEntity = e;
		System.out.println("Focused switched to : " + e.ID + " / " + e.getClass().getName());

	}
}

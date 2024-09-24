package com.superkiment.main;

import com.superkiment.globals.Scale;
import com.superkiment.world.ConnectionToWorld;
import com.superkiment.entities.Player;
import com.superkiment.entities.logic.Entity;
import com.superkiment.entities.logic.EntityManager;
import com.superkiment.entities.logic.Hitbox;

import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONObject;

/**
 * Main class for the running game
 */
public class Game {

    /**
     * Connection to the world server
     */
    public ConnectionToWorld connexion;

    /**
     * The Player controlled by the player
     */
    public Player controlledPlayer;

    /**
     * The library of entities to be updated and interact with one another
     */
    public EntityManager entityManager;

    /**
     * The entity on which the camera is focused
     */
    public Entity focusedEntity;

    /**
     * The distance of the camera from the focused entity
     */
    public float distanceCamera = 3.0f;

    /**
     * The PApplet to be drawn on
     */
    private final PApplet pap;

    /**
     * True to make the scene 3D
     */
    private boolean is3D = false;

    Game(PApplet p, ConnectionToWorld connexion) {
        pap = p;
        this.connexion = connexion;
        entityManager = new EntityManager();
        RenderManager.renderManager = new RenderManager(p);
    }

    /**
     * Main method to update the connection to the world and the controlled player's
     * actions.
     */
    public void Update() {
        if (connexion != null && connexion.isConnected()) {
            connexion.Update();
        }

        controlledPlayer.Update();
    }

    public void keyPressed(char key) {

        // Partir si on est pas en jeu
        if (GameManager.GameState.GAME != OpenMonsterHunter.gameManager.gameState)
            return;

        // Récup tout input si la console est active
        if (Console.console.actif) {
            if (key == '\n') {
                Console.console.Enter();
            }
            return;
        }

        // Déplacement du joueur
        if (key == 'd' || key == 'q' || key == 's' || key == 'z' || key == 'f') {
            controlledPlayer.keyPressed(key);
        }

        // Tests d'entités
        if (key == 'l') {
            com.superkiment.entities.Dog e = new com.superkiment.entities.Dog();
            e.pos.set(200, 200);
            // Envoyer données nouvelle entité

            connexion.EnvoiDonneesNouvelleEntite(e.getJSON());
        }

        // Activer la console
        if (key == '\n') {
            Console.console.Toggle();
        }

    }

    public void keyReleased(char key) {
        if (key == 'd' || key == 'q' || key == 's' || key == 'z') {
            controlledPlayer.keyReleased(key);
        }
    }

    /**
     * Checks connection and displays information. Translates the scene on place.
     */
    public void Render() {
        if (connexion != null && connexion.isConnected()) {
            pap.background(0, 50, 10);
            pap.pushStyle();
            pap.pushMatrix();
            pap.fill(255);
            pap.text("Connexion : " + connexion.client.ip(), 50, 100);
            pap.popMatrix();
            pap.popStyle();

            pap.pushStyle();
            pap.pushMatrix();

            pap.translate(-controlledPlayer.pos.x + pap.width / 2, -controlledPlayer.pos.y + pap.height / 2);

            DisplayGame();

            pap.popMatrix();
            pap.popStyle();
        } else {
            pap.background(0);
            pap.pushStyle();
            pap.pushMatrix();
            pap.fill(255);
            pap.text("Pas de connexion au monde", 50, 100);
            pap.popMatrix();
            pap.popStyle();
        }
    }

    /**
     * Renders the scene
     */
    private void DisplayGame() {
        // try {

        this.SetCamera();
        pap.scale(Scale.getGameScale());

        // Background
        DisplayGrid();

        for (Entity e : entityManager.getEntities()) {
            RenderManager.renderManager.Render(e);

            Hitbox.PushStyle(pap);
            for (Hitbox h : e.hitboxes) {
                pap.pushStyle();
                pap.pushMatrix();
                pap.translate(e.pos.x, e.pos.y);
                h.Render(pap);
                pap.popMatrix();
                pap.popStyle();
            }
            pap.popMatrix();
            pap.popStyle();
        }

        RenderManager.renderManager.Render(controlledPlayer);
        Hitbox.PushStyle(pap);
        for (Hitbox h : controlledPlayer.hitboxes) {
            pap.pushStyle();
            pap.pushMatrix();
            pap.translate(controlledPlayer.pos.x, controlledPlayer.pos.y);
            h.Render(pap);
            pap.popMatrix();
            pap.popStyle();
        }
        pap.popMatrix();
        pap.popStyle();
    }

    /**
     * Displays a grid in the background
     */
    private void DisplayGrid() {
        pap.pushStyle();
        pap.pushMatrix();
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

        pap.popMatrix();
        pap.popStyle();
    }

    /**
     * Sets the camera on place (3D/2D)
     */
    private void SetCamera() {
        PVector eyeCam = new PVector();
        PVector centerCam = new PVector();

        if (is3D) {

            eyeCam.set((pap.width / distanceCamera + controlledPlayer.pos.x * Scale.getGameScale()),
                    (pap.height / distanceCamera + controlledPlayer.pos.y * Scale.getGameScale()),
                    (pap.height / distanceCamera) / PApplet.tan((PApplet.PI * 30.0f / 180.0f)));

            centerCam.set(controlledPlayer.pos.x * Scale.getGameScale(), controlledPlayer.pos.y * Scale.getGameScale(),
                    0);
        } else {
            eyeCam.set((controlledPlayer.pos.x * Scale.getGameScale()),
                    (controlledPlayer.pos.y * Scale.getGameScale() + 1), (pap.height));

            centerCam.set(controlledPlayer.pos.x * Scale.getGameScale(), controlledPlayer.pos.y * Scale.getGameScale(),
                    0);
        }

        pap.camera(eyeCam.x, eyeCam.y, eyeCam.z, centerCam.x, centerCam.y, centerCam.z, 0, 0, -1);

    }

    /**
     * Gets JSON data of all entities and :
     * <ul>
     * <li>Adds new entities</li>
     * <li>Removes inexistant entities</li>
     * <li>Updates positions</li>
     * </ul>
     * 
     * @param data as JSON
     */
    public void TraiterData(JSONObject data) {

        entityManager.addOrRemoveEntityFromJSONData(data);

        entityManager.updatePositions(data);

        /*
         * for (int i = 0; i < data.getJSONArray("logic.Player").size(); i++) {
         * JSONObject playerJSON = data.getJSONArray("logic.Player").getJSONObject(i);
         *
         * }
         */
    }

    /**
     * Gets a JSON object, creates a players and sets it as controllable player
     * 
     * @param player as JSON
     */
    public void setControllablePlayer(JSONObject player) {
        controlledPlayer = entityManager.addControllablePlayer(player);
        setFocusedEntity(controlledPlayer);
        System.out.println("controlled player : " + controlledPlayer.name);
    }

    /**
     * Sets the entity the camera has to focus on
     * 
     * @param e the entity
     */
    public void setFocusedEntity(Entity e) {
        focusedEntity = e;
        System.out.println("Focused switched to : " + e.ID + " / " + e.getClass().getName());

    }
}

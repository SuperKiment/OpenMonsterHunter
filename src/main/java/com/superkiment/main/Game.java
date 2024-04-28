package com.superkiment.main;

import com.superkiment.globals.Scale;
import com.superkiment.entities.Entity;
import com.superkiment.entities.Hitbox;
import com.superkiment.entities.Player;
import com.superkiment.entities.EntityManager;
import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONObject;

public class Game {

    public com.superkiment.world.ConnectionToWorld connexion;
    public Player controlledPlayer;
    public EntityManager entityManager;
    public Entity focusedEntity;
    public float distanceCamera = 3.0f;
    private final PApplet pap;
    private final boolean is3D = false;

    Game(PApplet p, com.superkiment.world.ConnectionToWorld connexion) {
        pap = p;
        this.connexion = connexion;
        entityManager = new EntityManager();
        RenderManager.renderManager = new RenderManager(p);
    }

    public void Update() {
        if (connexion != null && connexion.isConnected()) {
            connexion.Update();
        }

        controlledPlayer.Update();
    }

    void keyPressed(char key) {
        if (GameManager.GameState.GAME == OpenMonsterHunter.gameManager.gameState) {
            if (!Console.console.actif) {
                if ((key == 'd' || key == 'q' || key == 's' || key == 'z') && !Console.console.actif) {
                    controlledPlayer.keyPressed(key);
                }

                // Tests d'entités
                if (key == 'l') {
                    com.superkiment.entities.Dog e = new com.superkiment.entities.Dog();
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

    }

    void keyReleased(char key) {
        if (key == 'd' || key == 'q' || key == 's' || key == 'z') {
            controlledPlayer.keyReleased(key);
        }
    }

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

    public void TraiterData(JSONObject data) {

        entityManager.addIfInexistant(data);

        entityManager.updatePositions(data);

        /*
         * for (int i = 0; i < data.getJSONArray("logic.Player").size(); i++) {
         * JSONObject playerJSON = data.getJSONArray("logic.Player").getJSONObject(i);
         *
         * }
         */
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

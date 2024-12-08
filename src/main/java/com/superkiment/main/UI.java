package com.superkiment.main;

import com.superkiment.globals.Time;
import com.superkiment.main.GameManager.GameState;
import com.superkiment.main.components.Boutton;
import com.superkiment.main.components.TextInput;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

/**
 * A class that has all the buttons and textinputs for the user to interact and
 * send information
 */
public class UI {
    /**
     * A list of all the buttons in the UI
     */
    public ArrayList<Boutton> allBouttons;

    /**
     * The PApplet the user is interacting with
     */
    public OpenMonsterHunter omh;

    /**
     * Has the state of the application
     */
    public final GameManager gameManager;

    UI(GameManager gameManager, OpenMonsterHunter p) {
        this.omh = p;
        this.gameManager = gameManager;
        allBouttons = new ArrayList<Boutton>();

        Console.console = new Console(this);

        // Retour Title
        allBouttons.add(new Boutton(gameManager, 10, 10, 20, 20,
                GameManager.allPagesExcept(new GameManager.GameState[] { GameManager.GameState.TITLE }), "<") {
            public void Action() {
                ChangePage(GameManager.GameState.TITLE);
            }
        });

        // Credits
        allBouttons.add(new Boutton(gameManager, p.width / 2 - 100, p.height * 3 / 4, 200, 40,
                new GameManager.GameState[] { GameManager.GameState.TITLE }, "Credits") {
            public void Action() {
                ChangePage(GameManager.GameState.CREDITS);
            }
        });

        // Options
        allBouttons.add(new Boutton(gameManager, p.width / 2 - 100, p.height * 3 / 4 - 80, 200, 40,
                new GameManager.GameState[] { GameManager.GameState.TITLE }, "Options") {
            public void Action() {
                ChangePage(GameManager.GameState.OPTIONS);
            }
        });

        // Jouer Solo
        allBouttons.add(new Boutton(gameManager, p.width / 2 - 200, p.height * 3 / 4 - 320, 400, 80,
                new GameManager.GameState[] { GameManager.GameState.TITLE }, "Jouer Solo") {
            public void Action() {
                ChangePage(GameManager.GameState.CHOOSE_SOLO);
            }

            public boolean NotActiveIf() {
                return p.connectionToWorld != null && p.connectionToWorld.isConnected();
            }
        });

        // Jouer Multi
        allBouttons.add(new Boutton(gameManager, p.width / 2 - 200, p.height * 3 / 4 - 200, 400, 80,
                new GameManager.GameState[] { GameManager.GameState.TITLE }, "Jouer Multi") {
            public void Action() {
                ChangePage(GameManager.GameState.CHOOSE_MULTI);
            }

            public boolean NotActiveIf() {
                return p.connectionToWorld != null && p.connectionToWorld.isConnected();
            }
        });

        // Deconnexion
        allBouttons.add(new Boutton(gameManager, p.width / 2 - 200, p.height * 3 / 4 - 200, 400, 80,
                new GameManager.GameState[] { GameManager.GameState.TITLE }, "Deconnexion") {
            public void Action() {
                System.out.println("Deconnexion");
                p.connectionToWorld.client.stop();
                UpdateBouttons();
            }

            public boolean NotActiveIf() {
                return !(p.connectionToWorld != null && p.connectionToWorld.isConnected());
            }
        });

        // Entrer IP
        TextInput entrerIP = new TextInput(gameManager, p.width / 2 - 200, p.height * 3 / 4 - 200, 400, 80,
                new GameManager.GameState[] { GameManager.GameState.CHOOSE_MULTI });
        entrerIP.text = "127.0.0.1";
        allBouttons.add(entrerIP);

        // Entrer Jeu Multi
        allBouttons.add(new Boutton(gameManager, p.width / 2 - 200, p.height * 3 / 4, 400, 80,
                new GameManager.GameState[] { GameManager.GameState.CHOOSE_MULTI }, "Go !",
                new TextInput[] { (TextInput) allBouttons.get(allBouttons.size() - 1) }) {
            public void Action() {
                System.out.println(inputs[0].text);
                p.ConnectToWorld(inputs[0].text);
                ChangePage(GameManager.GameState.GAME);
            }
        });

        // Entrer Jeu Solo
        allBouttons.add(new Boutton(gameManager, p.width / 2 - 200, p.height * 3 / 4, 400, 80,
                new GameManager.GameState[] { GameManager.GameState.CHOOSE_SOLO }, "Go !") {
            public void Action() {

                p.CreateWorld("Monde1", true);
                ChangePage(GameManager.GameState.GAME);
            }
        });

        // Jouer Multi
        allBouttons.add(new TextInput(gameManager, p.width / 2 - 200, p.height / 4, 200, 40,
                new GameManager.GameState[] { GameManager.GameState.TITLE }, p.playerName) {
            public void ActionOnKeyboard() {
                p.playerName = text;
            }
        });

        UpdateBouttons();

        Console.console.setActif(false);
    }

    /**
     * Renders all buttons and inputs on the right screen
     * 
     * @param p the PApplet to be drawn on
     */
    public void Render(PApplet p) {
        p.cursor(0);
        p.pushStyle();
        p.pushMatrix();
        p.fill(255);
        p.textSize(30);
        p.text(gameManager.gameState.toString(), 50, 50);
        p.popStyle();
        p.popMatrix();

        switch (gameManager.gameState) {
            case CREDITS:
                p.pushStyle();
                p.pushMatrix();
                p.fill(255);
                p.textAlign(PApplet.CENTER);
                p.text("SuperKiment.", p.width / 2, p.height / 2);
                p.popStyle();
                p.popMatrix();
                break;
            case GAME:
                break;
            case OPTIONS:
                break;
            case TITLE:
                break;
            case CHOOSE_MULTI:
                break;
            case CHOOSE_SOLO:
                break;
            default:
                break;
        }

        for (Boutton b : allBouttons) {
            if (b.actif)
                b.Render(p);
        }

        Console.console.Update(p);

        p.pushStyle();
        p.pushMatrix();
        p.textSize(12);
        p.textAlign(OpenMonsterHunter.RIGHT);
        p.text(Time.moyenneTime + "ms : Delta Time", p.width, 50);
        p.text(Time.moyenneDrawTime + "ms, " + (int) (Time.moyenneDrawTime * 100 / Time.moyenneTime) + "% : Draw Time",
                p.width, 65);
        p.text((int) omh.frameRate + " : frameRate", p.width, 80);
        p.popStyle();
        p.popMatrix();
    }

    /**
     * Changes the state of the application and updates the buttons
     * 
     * @param state the GameState replacing the current one
     */
    public void ChangePage(GameManager.GameState state) {
        gameManager.gameState = state;
        UpdateBouttons();
    }

    private void UpdateBouttons() {
        for (Boutton b : allBouttons) {
            b.CheckActive();
        }
    }

    public void Click(int x, int y) {
        for (Boutton b : allBouttons)
            if (b.actif)
                b.CheckClick(x, y);
    }

    public void Key(char c) {
        for (Boutton b : allBouttons)
            if (b.actif)
                b.CheckKeyboard(c);
    }

    // ================================================================

    
}

package com.superkiment.main.components;

import com.superkiment.main.GameManager;

import processing.core.PApplet;
import processing.core.PVector;

public class Boutton {
    public PVector pos, taille;
    public GameManager.GameState[] liaisons;
    public String text = "";
    public TextInput[] inputs;
    public boolean actif = false;
    protected GameManager gameManager;

    /**
     * Always active button
     * 
     * @param posx
     * @param posy
     * @param taillex
     * @param tailley
     */
    public Boutton(float posx, float posy, float taillex, float tailley) {
        pos = new PVector(posx, posy);
        taille = new PVector(taillex, tailley);
        liaisons = null;
        gameManager = null;
    }

    /**
     * 
     * @param gameManager the game's actual gameManager
     * @param posx
     * @param posy
     * @param taillex
     * @param tailley
     * @param li          a GameState array where the button is active
     */
    public Boutton(GameManager gameManager, float posx, float posy, float taillex, float tailley,
            GameManager.GameState[] li) {
        this(posx, posy, taillex, tailley);
        this.liaisons = li;
        this.gameManager = gameManager;
    }

    /**
     * 
     * @param gameManager the game's actual gameManager
     * @param posx
     * @param posy
     * @param taillex
     * @param tailley
     * @param li          a GameState array where the button is active
     * @param text        what's to be displayed on the button
     */
    public Boutton(GameManager gameManager, float posx, float posy, float taillex, float tailley,
            GameManager.GameState[] li, String text) {
        this(gameManager, posx, posy, taillex, tailley, li);
        this.text = text;
    }

    /**
     * 
     * @param gameManager the game's actual gameManager
     * @param posx
     * @param posy
     * @param taillex
     * @param tailley
     * @param li          a GameState array where the button is active
     * @param text        what's to be displayed on the button
     * @param inputs
     */
    public Boutton(GameManager gameManager, float posx, float posy, float taillex, float tailley,
            GameManager.GameState[] li, String text,
            TextInput[] inputs) {
        this(gameManager, posx, posy, taillex, tailley, li, text);
        this.inputs = inputs;
    }

    public void Action() {
    }

    public void CheckClick(int x, int y) {
        if (CheckHover(x, y)) {
            Action();
        }
    }

    public boolean CheckHover(int x, int y) {
        return (x > pos.x && x < pos.x + taille.x && y > pos.y && y < pos.y + taille.y);
    }

    public void Render(PApplet p) {
        if (!NotActiveIf()) {
            p.pushStyle();
            p.pushMatrix();

            p.fill(50);
            p.stroke(255);

            if (CheckHover(p.mouseX, p.mouseY)) {
                p.cursor(12);
                p.fill(100);

            }

            p.rect(pos.x, pos.y, taille.x, taille.y);

            p.popStyle();
            p.popMatrix();

            p.pushStyle();
            p.pushMatrix();

            p.fill(255);
            p.textAlign(PApplet.CENTER);
            p.textSize(taille.y * 3 / 4);
            p.text(text, pos.x + taille.x / 2, pos.y + taille.y * 3 / 4);

            p.popStyle();
            p.popMatrix();
        }
    }

    public boolean NotActiveIf() {
        return false;
    }

    public void CheckActive() {
        if (liaisons == null)
            return;

        if (NotActiveIf()) {
            actif = false;
            return;
        }

        for (GameManager.GameState liaison : liaisons)
            if (gameManager.gameState == liaison) {
                actif = true;
                return;
            }
        actif = false;
    }

    public void CheckKeyboard(char c) {

    }

    public void ActionOnKeyboard() {

    }
}
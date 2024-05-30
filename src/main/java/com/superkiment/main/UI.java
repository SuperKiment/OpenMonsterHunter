package com.superkiment.main;

import com.superkiment.globals.Time;
import com.superkiment.main.GameManager.GameState;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class UI {
    public ArrayList<Boutton> allBouttons;
    public OpenMonsterHunter omh;
    private final GameManager gameManager;

    UI(GameManager gameManager, OpenMonsterHunter p) {
        this.omh = p;
        this.gameManager = gameManager;
        allBouttons = new ArrayList<Boutton>();

        Console.console = new Console(this);

        // Retour Title
        allBouttons.add(new Boutton(10, 10, 20, 20,
                GameManager.allPagesExcept(new GameManager.GameState[] { GameManager.GameState.TITLE }), "<") {
            public void Action() {
                ChangePage(GameManager.GameState.TITLE);
            }
        });

        // Credits
        allBouttons.add(new Boutton(p.width / 2 - 100, p.height * 3 / 4, 200, 40,
                new GameManager.GameState[] { GameManager.GameState.TITLE }, "Credits") {
            public void Action() {
                ChangePage(GameManager.GameState.CREDITS);
            }
        });

        // Options
        allBouttons.add(new Boutton(p.width / 2 - 100, p.height * 3 / 4 - 80, 200, 40,
                new GameManager.GameState[] { GameManager.GameState.TITLE }, "Options") {
            public void Action() {
                ChangePage(GameManager.GameState.OPTIONS);
            }
        });

        // Jouer Solo
        allBouttons.add(new Boutton(p.width / 2 - 200, p.height * 3 / 4 - 320, 400, 80,
                new GameManager.GameState[] { GameManager.GameState.TITLE }, "Jouer Solo") {
            public void Action() {
                ChangePage(GameManager.GameState.CHOOSE_SOLO);
            }

            public boolean NotActiveIf() {
                return p.connectionToWorld != null && p.connectionToWorld.isConnected();
            }
        });

        // Jouer Multi
        allBouttons.add(new Boutton(p.width / 2 - 200, p.height * 3 / 4 - 200, 400, 80,
                new GameManager.GameState[] { GameManager.GameState.TITLE }, "Jouer Multi") {
            public void Action() {
                ChangePage(GameManager.GameState.CHOOSE_MULTI);
            }

            public boolean NotActiveIf() {
                return p.connectionToWorld != null && p.connectionToWorld.isConnected();
            }
        });

        // Deconnexion
        allBouttons.add(new Boutton(p.width / 2 - 200, p.height * 3 / 4 - 200, 400, 80,
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
        TextInput entrerIP = new TextInput(p.width / 2 - 200, p.height * 3 / 4 - 200, 400, 80,
                new GameManager.GameState[] { GameManager.GameState.CHOOSE_MULTI });
        entrerIP.text = "127.0.0.1";
        allBouttons.add(entrerIP);

        // Entrer Jeu Multi
        allBouttons.add(new Boutton(p.width / 2 - 200, p.height * 3 / 4, 400, 80,
                new GameManager.GameState[] { GameManager.GameState.CHOOSE_MULTI }, "Go !",
                new TextInput[] { (TextInput) allBouttons.get(allBouttons.size() - 1) }) {
            public void Action() {
                System.out.println(inputs[0].text);
                p.ConnectToWorld(inputs[0].text);
                ChangePage(GameManager.GameState.GAME);
            }
        });

        // Entrer Jeu Solo
        allBouttons.add(new Boutton(p.width / 2 - 200, p.height * 3 / 4, 400, 80,
                new GameManager.GameState[] { GameManager.GameState.CHOOSE_SOLO }, "Go !") {
            public void Action() {

                p.CreateWorld("Monde1");
                ChangePage(GameManager.GameState.GAME);
            }
        });

        // Jouer Multi
        allBouttons.add(new TextInput(p.width / 2 - 200, p.height / 4, 200, 40,
                new GameManager.GameState[] { GameManager.GameState.TITLE }, p.playerName) {
            public void ActionOnKeyboard() {
                p.playerName = text;
            }
        });

        UpdateBouttons();

        Console.console.setActif(false);
    }

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
        p.text(OpenMonsterHunter.drawTime + "ms : Draw Time", p.width, 65);
        p.text((int) omh.frameRate + " : frameRate", p.width, 80);
        p.popStyle();
        p.popMatrix();
    }

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

    // ============================================================================

    public class Boutton {
        public PVector pos, taille;
        public GameManager.GameState[] liaisons;
        protected String text = "";
        protected TextInput[] inputs;
        boolean actif = false;

        Boutton(float posx, float posy, float taillex, float tailley, GameManager.GameState[] li) {
            pos = new PVector(posx, posy);
            taille = new PVector(taillex, tailley);
            liaisons = li;
        }

        Boutton(float posx, float posy, float taillex, float tailley, GameManager.GameState[] li, String text) {
            this(posx, posy, taillex, tailley, li);
            this.text = text;
        }

        Boutton(float posx, float posy, float taillex, float tailley, GameManager.GameState[] li, String text,
                TextInput[] inputs) {
            this(posx, posy, taillex, tailley, li);
            this.text = text;
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

    // ================================================================

    public class TextInput extends Boutton {

        boolean selectionne = false;

        TextInput(float posx, float posy, float taillex, float tailley, GameState[] li) {
            super(posx, posy, taillex, tailley, li);
        }

        TextInput(float posx, float posy, float taillex, float tailley, GameState[] li, String text) {
            super(posx, posy, taillex, tailley, li, text);
        }

        public void Action() {
        }

        public void CheckClick(int x, int y) {
            if (CheckHover(x, y)) {
                selectionne = true;
                Action();
            } else
                selectionne = false;
        }

        public void Render(PApplet p) {
            super.Render(p);
            if (selectionne && !NotActiveIf()) {
                p.pushStyle();
                p.pushMatrix();
                p.fill(0, 0);
                p.stroke(255);
                p.rect(pos.x + 5, pos.y + 5, taille.x - 10, taille.y - 10);
                p.popStyle();
                p.popMatrix();
            }
        }

        public void ActionOnKeyboard() {

        }

        public void CheckKeyboard(char c) {
            if (selectionne) {
                switch (c) {
                    case PApplet.BACKSPACE:
                        if (text.length() > 0)
                            text = text.substring(0, text.length() - 1);
                        ActionOnKeyboard();
                        break;
                    case PApplet.DELETE:
                        break;
                    case PApplet.ENTER:
                        selectionne = false;
                        ActionOnKeyboard();
                        break;
                    case PApplet.ESC:
                        selectionne = false;
                        ActionOnKeyboard();
                        break;
                    case PApplet.RETURN:
                        selectionne = false;
                        ActionOnKeyboard();
                        break;
                    case PApplet.TAB:
                        break;
                    case PApplet.UP:
                        break;
                    case PApplet.DOWN:
                        break;
                    case PApplet.RIGHT:
                        break;
                    case PApplet.LEFT:
                        break;
                    default:
                        text += c;
                        ActionOnKeyboard();
                }
            }
        }
    }
}

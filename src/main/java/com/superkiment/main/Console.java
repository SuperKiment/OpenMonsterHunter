package com.superkiment.main;

import com.superkiment.main.components.Boutton;
import com.superkiment.main.components.TextInput;
import com.superkiment.utils.Pair;
import processing.core.PApplet;
import java.util.ArrayList;

// import java.util.HashMap;
// import java.util.function.Consumer;

/**
 * A public static class that has one instance of itself console. The class is
 * used to interact with the game and send game chats.
 */
public class Console {
    /**
     * The only instance of this class
     */
    public static Console console;

    /**
     * If true, the console picks up commands and chats. Otherwise the console does
     * nothing.
     */
    public boolean actif = false;

    /**
     * The UI element to get the player input.
     */
    private final TextInput textInput;

    /**
     * The UI element to get the player input.
     */
    private final Boutton bouttonInput;
    // private final UI ui;

    /**
     * Inputs and chats to be displayed on the screen.
     */
    private final ArrayList<Pair<String, Integer>> inputs;

    /**
     * A history of all commands and chats sent from the connection.
     */
    private final ArrayList<Pair<String, Integer>> history;

    /**
     * La hauteur d'une ligne à l'affichage.
     */
    private final int hauteurLigne = 20;
    // private final HashMap<String, Consumer<String[]>> commands;

    public Console(UI ui) {
        // this.ui = ui;
        inputs = new ArrayList<Pair<String, Integer>>();
        history = new ArrayList<Pair<String, Integer>>();
        // commands = new HashMap<String, Consumer<String[]>>();

        // Texte
        textInput = new TextInput(0, ui.omh.height - 50, ui.omh.width, 50);
        textInput.actif = false;
        ui.allBouttons.add(textInput);

        // Boutton
        bouttonInput = new Boutton(ui.gameManager, ui.omh.height - 150, ui.omh.height - 50 * 2, 150, 50,
                new GameManager.GameState[] {}, "Envoyer") {
            public void Action() {
                Enter();
            }
        };

        ui.allBouttons.add(bouttonInput);
        bouttonInput.actif = false;

        /*
         * commands.put("/ping", (args) -> { write("pong !!!!!!!!!!!!!!!!!"); });
         *
         * commands.put("/birth", (args) -> { System.out.println(args.length); if
         * (args.length == 4) { try { Class arrayClass = Class.forName(args[3]); Entity
         * obj = (Entity) arrayClass.getDeclaredConstructor().newInstance();
         *
         * obj.pos.set(Float.parseFloat(args[1]), Float.parseFloat(args[2]));
         *
         * ui.omh.connectionToWorld.EnvoiDonneesNouvelleEntite(obj.getJSON());
         * write("summoned " + args[3]); } catch (Exception e) { write(e.toString()); }
         *
         * } });
         */
    }

    /**
     * The main method of the class, displays the console and removes old commands
     * from display.
     * 
     * @param p the PApplet to be drawn on
     */
    public void Update(PApplet p) {
        if (GameManager.GameState.GAME != OpenMonsterHunter.gameManager.gameState) {
            return;
        }

        ArrayList<Pair<String, Integer>> aEnlever = new ArrayList<Pair<String, Integer>>();

        int compteur = inputs.size();

        //TODO: console display is based on framerate, make it based on seconds.
        for (Pair<String, Integer> pair : inputs) {
            int value = pair.getSecond();
            pair.setSecond(value - 1);

            // System.out.println(value);
            p.pushStyle();
            p.pushMatrix();
            p.fill(0, value);
            p.noStroke();
            p.rect(0, p.height - 100 - compteur * hauteurLigne, p.width, hauteurLigne);
            p.fill(255, value);
            p.textSize(PApplet.round(hauteurLigne * 0.75f));
            p.text(pair.getFirst(), 20, p.height - 100 + PApplet.round(hauteurLigne * 0.75f) - compteur * hauteurLigne);
            p.popStyle();
            p.popMatrix();

            if (value <= 0) {
                aEnlever.add(pair);
            }

            compteur--;
        }

        for (Pair<String, Integer> pair : aEnlever) {
            inputs.remove(pair);
        }
    }

    /**
     * Toggle on and off the console
     */
    public void Toggle() {
        setActif(!actif);
    }

    /**
     * Set the console as active or not
     * 
     * @param a the boolean to set the console as
     */
    public void setActif(boolean a) {
        actif = a;
        textInput.actif = a;
        bouttonInput.actif = a;
        textInput.selectionne = a;
    }

    /**
     * Write in the console and in the game chat on all the users' screens.
     * 
     * @param str      the string to send
     * @param received true if the chat is not to be sent to all users. False if the
     *                 chat is to be sent.
     */
    public void write(String str, boolean received) {
        if (str.length() == 0)
            return;

        System.out.println("Commande : " + str);
        Pair<String, Integer> pair = new Pair<String, Integer>("> " + str, 300);
        inputs.add(pair);
        history.add(pair);

        if (!received)
            OpenMonsterHunter.game.connexion.EnvoiConsoleInput(str);

        /*
         * if (str.charAt(0) == '/') { String[] command = str.split(" ");
         *
         * commands.get(command[0]).accept(command); }
         */
    }

    /**
     * Input to send the text to the console and close it.
     */
    public void Enter() {
        write(textInput.text.trim(), false);

        textInput.text = "";

        Toggle();
    }
}
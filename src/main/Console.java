package main;

import java.util.HashMap;
import java.util.ArrayList;

import main.UI.Boutton;
import main.UI.TextInput;
import processing.core.PApplet;
import utils.Pair;

public class Console {
	public static Console console;
	public boolean actif = false;
	private TextInput textInput;
	private Boutton bouttonInput;
	private UI ui;
	private ArrayList<Pair<String, Integer>> inputs;
	private ArrayList<Pair<String, Integer>> history;
	private int hauteurLigne = 20;

	public Console(UI ui) {
		this.ui = ui;
		inputs = new ArrayList<Pair<String, Integer>>();
		history = new ArrayList<Pair<String, Integer>>();

		// Texte
		textInput = ui.new TextInput(0, ui.omh.height - 50, ui.omh.width, 50, new GameManager.GameState[] {});
		ui.allBouttons.add(textInput);
		textInput.actif = false;

		// Boutton
		bouttonInput = ui.new Boutton(ui.omh.height - 150, ui.omh.height - 50 * 2, 150, 50,
				new GameManager.GameState[] {}, "Envoyer") {
			public void Action() {
				Enter();
			}
		};

		ui.allBouttons.add(bouttonInput);
		bouttonInput.actif = false;
	}

	public void Update(PApplet p) {
		if (GameManager.GameState.GAME != OpenMonsterHunter.gameManager.gameState) {
			return;
		}

		ArrayList<Pair<String, Integer>> aEnlever = new ArrayList<Pair<String, Integer>>();

		int compteur = inputs.size();

		for (Pair<String, Integer> pair : inputs) {
			int value = pair.getSecond();
			pair.setSecond(value - 1);

			System.out.println(value);
			p.push();
			p.fill(0, value);
			p.noStroke();
			p.rect(0, p.height - 100 - compteur * hauteurLigne, p.width, hauteurLigne);
			p.fill(255, value);
			p.textSize(PApplet.round(hauteurLigne * 0.75f));
			p.text(pair.getFirst(), 20, p.height - 100 + PApplet.round(hauteurLigne * 0.75f) - compteur * hauteurLigne);
			p.pop();

			if (value <= 0) {
				aEnlever.add(pair);
			}

			compteur--;
		}

		for (Pair<String, Integer> pair : aEnlever) {
			inputs.remove(pair);
		}
	}

	public void Toggle() {
		actif = !actif;
		textInput.actif = actif;
		bouttonInput.actif = actif;
		textInput.selectionne = actif;
	}

	public void setActif(boolean a) {
		actif = a;
		textInput.actif = a;
		bouttonInput.actif = a;
		textInput.selectionne = a;
	}

	public void Enter() {
		System.out.println("Commande : " + textInput.text);
		inputs.add(
				new Pair<String, Integer>(ui.omh.millis() + " | " + ui.omh.playerName + " : " + textInput.text, 300));
		textInput.text = "";
		Toggle();
	}
}
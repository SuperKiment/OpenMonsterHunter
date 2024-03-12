package main;

import main.UI.Boutton;
import main.UI.TextInput;

public class Console {
	public static Console console;
	public boolean actif = false;
	TextInput textInput;
	Boutton bouttonInput;
	UI ui;

	public Console(UI ui) {
		// Texte
		textInput = ui.new TextInput(0, ui.omh.height - 50, ui.omh.width, 50, new GameManager.GameState[] {});
		ui.allBouttons.add(textInput);
		textInput.actif = false;

		// Boutton
		bouttonInput = ui.new Boutton(ui.omh.height-150, ui.omh.height - 50*2, 150, 50, new GameManager.GameState[] {}, "Envoyer") {
			public void Action() {
				Enter();
			}
		};
		ui.allBouttons.add(bouttonInput);
		bouttonInput.actif = false;
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
		textInput.text = "";
		Toggle();
		textInput.selectionne = false;

	}
}
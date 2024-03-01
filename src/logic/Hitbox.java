package logic;

import processing.core.PApplet;
import processing.core.PVector;

public class Hitbox {
	enum hitboxType {
		RECTANGLE, CERCLE, TRIANGLE
	}

	hitboxType type = null;
	PVector pos;
	float range = 10, largeur = 10, hauteur = 10;

	public Hitbox(PVector pos, float range) {
		type = hitboxType.CERCLE;
		this.pos = new PVector(pos.x, pos.y);
		this.range = range;
	}

	public Hitbox(PVector pos, float larg, float haut) {
		type = hitboxType.RECTANGLE;
		this.pos = new PVector(pos.x, pos.y);
		this.largeur = larg;
		this.hauteur = haut;
	}

	public static void PushStyle(PApplet p) {
		p.push();
		p.fill(0, 0);
		p.stroke(255, 0, 0, 255);
		p.strokeWeight(0.5f);
		p.rectMode(PApplet.CENTER);
	}

	public void Render(PApplet p) {
		switch (type) {
		case RECTANGLE:
			p.rect(pos.x, pos.y, largeur, hauteur);
			break;
		case CERCLE:
			p.circle(pos.x, pos.y, range);
			break;
		}
	}
}

package logic;

import java.util.function.Consumer;

import processing.core.PApplet;
import processing.core.PVector;

public class Hitbox {
	enum hitboxType {
		RECTANGLE, CERCLE, TRIANGLE
	}

	enum hitboxActionType {
		PHYSICS, TRIGGER
	}

	hitboxType type = null;
	hitboxActionType actionType = hitboxActionType.PHYSICS;
	PVector pos;
	float range = 10, largeur = 10, hauteur = 10;
	Entity parent;
	Consumer<Entity> action = null;

	private Hitbox(Entity e, PVector pos) {
		this.parent = e;
		this.pos = new PVector(pos.x, pos.y);
	}

	/**
	 * Construct hitbox as CERCLE
	 * 
	 * @param e
	 * @param pos
	 * @param range
	 */
	public Hitbox(Entity e, PVector pos, float range) {
		this(e, pos);
		type = hitboxType.CERCLE;
		this.range = range;
	}

	/**
	 * Construct hitbox as RECTANGLE
	 * 
	 * @param e
	 * @param pos
	 * @param larg
	 * @param haut
	 */
	public Hitbox(Entity e, PVector pos, float larg, float haut) {
		this(e, pos);
		type = hitboxType.RECTANGLE;
		this.largeur = larg;
		this.hauteur = haut;
	}

	public void setAction(Consumer<Entity> consumer) {
		this.action = consumer;
	}

	public void doAction(Entity entity) {
		if (action == null) {
			action.accept(entity);
		}
	}

	/**
	 * Static PApplet push() method for all hitboxes. DO NOT FORGET pop() !!
	 * 
	 * @param p
	 */
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
		default:
			p.circle(pos.x, pos.y, 10);
			break;
		}
	}

	public PVector getActualPos() {
		PVector actualPos = new PVector(parent.pos.x, parent.pos.y);
		PVector posRotated = new PVector(this.pos.x, this.pos.y);
		posRotated.rotate(parent.dir.heading());

		actualPos.add(posRotated);

		return actualPos;
	}

	public boolean isCollisionWith(Hitbox other) {
		boolean collision = false;
		PVector actualPos = this.getActualPos();
		PVector otherActualPos = other.getActualPos();

		switch (other.type) {
		case CERCLE:
			if (PVector.sub(actualPos, otherActualPos).mag() * 2 < other.range + this.range) {
				collision = true;
			}
			break;
		case RECTANGLE:
			break;
		case TRIANGLE:
			break;
		default:
			break;
		}

		return collision;
	}
}

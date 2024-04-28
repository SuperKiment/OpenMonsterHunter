package logic;

import java.util.function.Consumer;

import processing.core.PVector;

public class InteractionPoint extends Hitbox {

	public InteractionPoint(Entity e, PVector pos, float range) {
		super(e, pos, range);
		this.type = hitboxType.CERCLE;
		this.actionType = hitboxActionType.TRIGGER;
	}

	public InteractionPoint(Entity e, PVector pos) {
		this(e, pos, 100);
	}

	@Override
	public void setAction(Consumer<Entity> consumer) {
	}

}

package logic;

import processing.core.PVector;

public class Entity {
	public float speed = 1;
	public PVector pos, dir;

	public void Update() {
		Deplacement();
	}
	
	private void Deplacement() {
		dir.setMag(speed);
		pos.add(dir);
	}
	
	public void setDir(float x, float y) {
		dir.set(x, y);
	}
	
	public void setDir(PVector p) {
		dir.set(p.x, p.y);
	}
	
	public void setPos(float x, float y) {
		pos.set(x, y);
	}
	
	public void setPos(PVector p) {
		pos.set(p.x, p.y);
	}
}

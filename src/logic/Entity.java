package logic;

import java.util.ArrayList;

import processing.core.PVector;
import processing.data.JSONObject;

public class Entity {
	public float speed = 1;
	public PVector pos, dir;
	public String ID = "";

	// TODO config les hitboxes
	public ArrayList<Hitbox> hitboxes;

	public Entity() {
		String characters = "azertyuiopqsdfghjklmwxcvbn1234567890+-&éèâêô_@à=";

		for (int i = 0; i < 20; i++) {
			int rand = (int) Math.floor(Math.random() * characters.length());
			char ch = characters.charAt(rand);

			ID += ch;
		}

		pos = new PVector();
		dir = new PVector();

		hitboxes = new ArrayList<Hitbox>();
		hitboxes.add(new Hitbox(new PVector(0, 0), 20));

	}

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

	public JSONObject getJSON() {
		JSONObject obj = new JSONObject();

		obj.setFloat("pos.x", pos.x);
		obj.setFloat("pos.y", pos.y);
		obj.setFloat("dir.x", dir.x);
		obj.setFloat("dir.y", dir.y);

		obj.setString("className", this.getClass().getName());

		obj.setFloat("speed", speed);

		obj.setString("ID", ID);
		return obj;
	}

	public void UpdateFromJSON(JSONObject json) {
		try {
			pos.x = json.getFloat("pos.x");
			pos.y = json.getFloat("pos.y");
			dir.x = json.getFloat("dir.x");
			dir.y = json.getFloat("dir.y");
			speed = json.getFloat("speed");
			ID = json.getString("ID");

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

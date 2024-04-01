package logic;

import java.util.ArrayList;

import main.Time;
import processing.core.PVector;
import processing.data.JSONObject;

public class Entity {
	public float speed = 0.3f;
	public PVector pos, dir, remanantDir;
	public String ID = "";
	public ArrayList<Hitbox> hitboxes;

	public Entity() {
		String characters = "azertyuiopqsdfghjklmwxcvbnAZERTYUIOPQSDFGHJKLMWXCVBN1234567890&éèâêô@àù£";

		for (int i = 0; i < 20; i++) {
			int rand = (int) Math.floor(Math.random() * characters.length());
			char ch = characters.charAt(rand);

			ID += ch;
		}

		pos = new PVector();
		dir = new PVector();
		remanantDir = new PVector();

		hitboxes = new ArrayList<Hitbox>();
		hitboxes.add(new Hitbox(new PVector(0, 0), 20));

	}

	public void Update() {
		Deplacement();
	}

	private void Deplacement() {
		PVector dirTemp = dir.copy();
		dirTemp.setMag(speed * Time.deltaTime);
		pos.add(dirTemp);

		if (dir.mag() != 0) {
			remanantDir.set(dir);
		}
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
		obj.setFloat("dir.x", remanantDir.x);
		obj.setFloat("dir.y", remanantDir.y);

		obj.setString("className", this.getClass().getName());

		obj.setFloat("speed", speed);

		obj.setString("ID", ID);
		return obj;
	}

	public void UpdateFromJSON(JSONObject json) {
		try {
			pos.x = json.getFloat("pos.x");
			pos.y = json.getFloat("pos.y");
			remanantDir.x = json.getFloat("dir.x");
			remanantDir.y = json.getFloat("dir.y");
			speed = json.getFloat("speed");
			ID = json.getString("ID");

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

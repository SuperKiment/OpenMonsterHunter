package logic;

import processing.core.*;
import processing.data.JSONObject;

public class Player extends Entity {

	public String name = "NoName";

	public Player(String name, PVector p) {
		super();
		pos = new PVector(p.x, p.y);
		dir = new PVector(0, 1);
		this.name = name;
	}

	@Override
	public void Update() {
		super.Update();
	}

	@Override
	public JSONObject getJSON() {
		JSONObject json = super.getJSON();

		json.put("name", name);

		return json;
	}

	public void UpdateFromJSON(JSONObject json) {
		try {
			pos.x = json.getFloat("pos.x");
			pos.y = json.getFloat("pos.y");
			dir.x = json.getFloat("dir.x");
			dir.y = json.getFloat("dir.y");
			speed = json.getFloat("speed");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

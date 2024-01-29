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
}

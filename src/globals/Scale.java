package globals;

import processing.core.PApplet;

public class Scale {
	private static float gameScale = 1.0f;
	private static float baseHeight = 800;

	public static void UpdateGameScale(PApplet p) {
		gameScale = (float) p.height / baseHeight;
	}

	public static float getGameScale() {
		return gameScale;
	}
}

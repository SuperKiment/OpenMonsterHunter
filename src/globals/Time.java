package globals;

import processing.core.PApplet;

public class Time {
	static public int deltaTime, lastTime = -1;
	
	public static void Update(PApplet p) {
		if (lastTime > 0) {
			deltaTime = p.millis() - lastTime;
			lastTime = p.millis();
		} else {
			lastTime = p.millis();
		}
	}
}

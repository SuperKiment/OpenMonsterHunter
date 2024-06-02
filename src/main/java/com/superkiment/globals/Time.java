package com.superkiment.globals;

import processing.core.PApplet;

/**
 * A public static class for performance testing purposes.
 */
public class Time {
    static private int deltaTime, lastTime = -1;
    
    /**
     * One of the two averages
     * <ul>
     * <li>moyenneTime : the average time in ms between two frames</li>
     * <li>moyenneDrawTime : the average time took to draw the scene entirely</li>
     * </ul>
     */
    static public float moyenneTime = 0f, moyenneDrawTime = 0f;

    static private float addTime = 0f, addDrawTime = 0f, drawTime = 0f;
    static private int compteurTime = 0, nombreTestTime = 50, compteurDrawTime = 0;

    public static void Update(PApplet p) {
        if (Time.lastTime <= 0)
            lastTime = p.millis();

        deltaTime = p.millis() - lastTime;
        lastTime = p.millis();

        compteurTime++;
        addTime += deltaTime;

        if (compteurTime >= nombreTestTime) {
            moyenneTime = addTime / (float) nombreTestTime;
            addTime = 0;
            compteurTime = 0;
        }

    }

    public static void UpdateDraw(PApplet p) {
        if (Time.lastTime <= 0)
            return;

        drawTime = p.millis() - lastTime;

        compteurDrawTime++;
        addDrawTime += drawTime;

        if (compteurDrawTime >= nombreTestTime) {
            moyenneDrawTime = (float) addDrawTime / (float) nombreTestTime;
            addDrawTime = 0;
            compteurDrawTime = 0;
        }

    }

    /**
     * Resets the timer
     * 
     * @param p the PApplet used
     */
    public static void Stop(PApplet p) {
        lastTime = p.millis();
    }
}

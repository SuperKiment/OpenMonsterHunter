package com.superkiment.globals;

import processing.core.PApplet;

public class Time {
    static public int deltaTime, lastTime = -1;
    static public float moyenneTime = 0f, drawTime = 0f, moyenneDrawTime = 0f;

    static private float addTime = 0f, addDrawTime = 0f;
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

    public static void Stop(PApplet p) {
        lastTime = p.millis();
    }
}

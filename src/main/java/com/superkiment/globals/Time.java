package com.superkiment.globals;

import processing.core.PApplet;

public class Time {
    static public int deltaTime, lastTime = -1;
    static public float moyenneTime = 0f;

    static private float addTime = 0f;
    static private int compteurTime = 0, nombreTestTime = 50;

    public static void Update(PApplet p) {
        if (lastTime > 0) {
            deltaTime = p.millis() - lastTime;
            lastTime = p.millis();

            compteurTime++;
            addTime += deltaTime;

            if (compteurTime >= nombreTestTime) {
                moyenneTime = addTime / (float)nombreTestTime;
                addTime = 0;
                compteurTime = 0;
            }

        } else {
            lastTime = p.millis();
        }
    }
}

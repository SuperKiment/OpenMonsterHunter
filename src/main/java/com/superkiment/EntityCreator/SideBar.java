package com.superkiment.EntityCreator;

import processing.core.PApplet;

public class SideBar {
    private PApplet pApplet;
    public final int width = 200;

    public SideBar(PApplet p) {
        this.pApplet = p;
    }

    public void Render() {
        pApplet.pushMatrix();
        pApplet.resetMatrix();
        pApplet.pushStyle();

        pApplet.fill(230);
        pApplet.rect(0, 0, width, pApplet.height);
        pApplet.stroke(100);
        pApplet.strokeWeight(3);
        pApplet.line(width, 0, width, pApplet.height);
        pApplet.noStroke();

        pApplet.popStyle();
        pApplet.popMatrix();
    }
}

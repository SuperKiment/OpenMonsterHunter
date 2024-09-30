package com.superkiment.entities.logic;

import processing.core.PApplet;

public class SayingBox {
    private Entity entity;
    private String textSaying = "";

    SayingBox(Entity entity) {
        this.entity = entity;
    }

    public void Render(PApplet pApplet) {
        if (!textSaying.isBlank()) {
            pApplet.pushMatrix();
            pApplet.pushStyle();

            pApplet.translate(-25, -50);

            pApplet.fill(255);
            pApplet.rect(entity.pos.x, entity.pos.y, 50, 20);

            pApplet.textAlign(PApplet.CENTER);
            pApplet.fill(0);
            pApplet.textSize(15);
            pApplet.text(textSaying, entity.pos.x + 25, entity.pos.y + 15);

            pApplet.popStyle();
            pApplet.popMatrix();
        }
    }

    public void setSayingText(String textSaying) {
        this.textSaying = textSaying;
    }

    public String getSayingText() {
        return this.textSaying;
    }
}

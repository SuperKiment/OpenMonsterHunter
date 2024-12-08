package com.superkiment.EntityCreator;

import com.superkiment.main.RenderManager;

import processing.core.PApplet;

public class EntityCreator extends PApplet {

    private CreatorMouseMode mouseMode = CreatorMouseMode.SEL_SHAPE;
    private CreatorUI creatorUI;

    public EntityCreator() {
        super();
        RenderManager.renderManager = new RenderManager(this);
        creatorUI = new CreatorUI(this);
    }

    public static void main(String[] args) {
        try {
            PApplet.main("com.superkiment.EntityCreator.EntityCreator");
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public void settings() {
        size(800, 800);
        noSmooth();
    }

    public void setup() {
        surface.setResizable(true);
    }

    public void draw() {
        background(0);
        creatorUI.Render();
    }
}

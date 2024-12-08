package com.superkiment.EntityCreator;

import java.util.ArrayList;

import com.superkiment.main.components.Boutton;

import processing.core.PApplet;

public class CreatorUI {
    private ArrayList<Boutton> allBoutons = new ArrayList<Boutton>();
    private PApplet pApplet;
    private SideBar sidebar;

    public CreatorUI(PApplet p) {
        this.pApplet = p;
        sidebar = new SideBar(p);

        pApplet.pushMatrix();
        pApplet.translate(sidebar.width, 0);
        allBoutons.add(new Boutton(50, 50, 50, 50));
        pApplet.popMatrix();
    }

    public void Render() {
        this.sidebar.Render();

        for (Boutton b : allBoutons) {
            b.Render(pApplet);
        }
    }
}

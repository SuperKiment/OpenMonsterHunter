package com.superkiment.EntityCreator;

import java.util.ArrayList;

import com.superkiment.main.components.Boutton;

public class CreatorUI {
    private ArrayList<Boutton> allBouttons = new ArrayList<Boutton>();
    private EntityCreator entityCreator;

    public CreatorUI(EntityCreator ec) {
        this.entityCreator = ec;

        Boutton b = new Boutton(50, 50, 50, 50) {

            @Override
            public void Action() {
                System.out.println("coucou");
            }
        };
        b.actif = true;

        allBouttons.add(b);
    }

    public void Render() {

        entityCreator.rect(0, 0, entityCreator.width/5, entityCreator.height);
        
        for (Boutton b : allBouttons) {
            if (b.actif)
                b.Render(entityCreator);
        }
    }

    public void mousePressed(int x, int y) {
        for (Boutton b : this.allBouttons)
            if (b.actif)
                b.CheckClick(x, y);
    }
}

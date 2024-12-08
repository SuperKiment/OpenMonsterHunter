package com.superkiment.main.components;

import com.superkiment.main.GameManager;

import processing.core.PApplet;

public class TextInput extends Boutton {

    public boolean selectionne = false;

    public TextInput(float posx, float posy, float taillex, float tailley) {
        super(posx, posy, taillex, tailley);
    }

    public TextInput(GameManager gameManager, float posx, float posy, float taillex, float tailley,
            GameManager.GameState[] li) {
        super(gameManager, posx, posy, taillex, tailley, li);
    }

    public TextInput(GameManager gameManager, float posx, float posy, float taillex, float tailley,
            GameManager.GameState[] li,
            String text) {
        super(gameManager, posx, posy, taillex, tailley, li, text);
    }

    public void Action() {
    }

    public void CheckClick(int x, int y) {
        if (CheckHover(x, y)) {
            selectionne = true;
            Action();
        } else
            selectionne = false;
    }

    public void Render(PApplet p) {
        super.Render(p);
        if (selectionne && !NotActiveIf()) {
            p.pushStyle();
            p.pushMatrix();
            p.fill(0, 0);
            p.stroke(255);
            p.rect(pos.x + 5, pos.y + 5, taille.x - 10, taille.y - 10);
            p.popStyle();
            p.popMatrix();
        }
    }

    public void ActionOnKeyboard() {

    }

    public void CheckKeyboard(char c) {
        if (selectionne) {
            switch (c) {
                case PApplet.BACKSPACE:
                    if (text.length() > 0)
                        text = text.substring(0, text.length() - 1);
                    ActionOnKeyboard();
                    break;
                case PApplet.DELETE:
                    break;
                case PApplet.ENTER:
                    selectionne = false;
                    ActionOnKeyboard();
                    break;
                case PApplet.ESC:
                    selectionne = false;
                    ActionOnKeyboard();
                    break;
                case PApplet.RETURN:
                    selectionne = false;
                    ActionOnKeyboard();
                    break;
                case PApplet.TAB:
                    break;
                case PApplet.UP:
                    break;
                case PApplet.DOWN:
                    break;
                case PApplet.RIGHT:
                    break;
                case PApplet.LEFT:
                    break;
                default:
                    text += c;
                    ActionOnKeyboard();
            }
        }
    }
}
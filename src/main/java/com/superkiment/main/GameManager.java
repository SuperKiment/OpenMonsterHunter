package com.superkiment.main;

public class GameManager {
    public GameState gameState = GameState.TITLE;

    GameManager() {
    }

    public static GameState[] allPagesExcept(GameState[] excepts) {
        GameState[] res = new GameState[GameState.values().length - excepts.length];

        int compt = 0;
        for (GameState g : GameState.values()) {
            boolean ajout = true;
            for (GameState except : excepts) {
                if (except == g) {
                    ajout = false;
                    break;
                }
            }

            if (ajout)
                res[compt++] = g;
        }

        return res;
    }

    public enum GameState {
        TITLE, GAME, CREDITS, OPTIONS, CHOOSE_SOLO, CHOOSE_MULTI
    }

}

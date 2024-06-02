package com.superkiment.main;

/**
 * The public static class that stores the state of the application : Title,
 * Options, Game, etc.
 */
public class GameManager {
    /**
     * The current state of the application
     */
    public GameState gameState = GameState.TITLE;

    GameManager() {
    }

    /**
     * Get all the gamestates but the ones specified. Used to assign a button to all
     * pages but the Title screen for example.
     * 
     * @param excepts an array of GameStates.
     * @return an array of GameStates
     */
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

    /**
     * TITLE, GAME, CREDITS, OPTIONS, CHOOSE_SOLO, CHOOSE_MULTI
     */
    public enum GameState {
        TITLE, GAME, CREDITS, OPTIONS, CHOOSE_SOLO, CHOOSE_MULTI
    }

}

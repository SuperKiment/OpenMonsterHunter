package main;

public class GameManager {
	public enum GameState {
		TITLE, GAME, CREDITS, OPTIONS, CHOOSE_SOLO, CHOOSE_MULTI
	}

	public static GameState[] allPagesExcept(GameState[] excepts) {
		GameState[] res = new GameState[GameState.values().length - excepts.length];

		int compt = 0;
		for (GameState g : GameState.values()) {
			boolean ajout = true;
			for (GameState except : excepts) {
				if (except == g)
					ajout = false;
			}

			if (ajout)
				res[compt++] = g;
		}

		return res;
	}

	public GameState gameState = GameState.TITLE;

	GameManager() {
	}

}

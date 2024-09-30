import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

import com.superkiment.entities.Player;
import com.superkiment.main.*;
import com.superkiment.main.GameManager.GameState;

import processing.core.PVector;

public class TemplateTest {
    static OpenMonsterHunter omh;
    static Game game;
    static Player player;

    @BeforeAll
    public static void setUp() {
        System.out.println();
        System.out.println("BeforeAll tests");
        // testWorld = new World("Test World", false);
        omh = new OpenMonsterHunter();
        omh.setupVariables();

        omh.CreateWorld("testWorld", true);

        player = OpenMonsterHunter.game.controlledPlayer;
        game = OpenMonsterHunter.game;
        OpenMonsterHunter.gameManager.setGameState(GameState.GAME);
        omh.setTestMode(true);
    }

    @Test
    @DisplayName("Creation & Connection to world")
    public void testWorldCreation() {
        assertTrue(omh.connectionToWorld.isConnected());
    }

    @Test
    @DisplayName("Player movement")
    public void testPlayerMovement() {
        PVector oldPosition = player.getPos().copy();

        omh.pressKey('d');
        for (int i = 0; i < 10; i++)
            omh.draw();
        omh.releaseKey('d');

        PVector newPosition = player.getPos().copy();
        System.out.println();
        System.out.println("moved : " + OpenMonsterHunter.abs(newPosition.x - oldPosition.x));
        System.out.println();
        assertTrue(newPosition.x != oldPosition.x);
    }

}
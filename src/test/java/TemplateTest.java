import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import com.superkiment.entities.Player;
import com.superkiment.main.*;
import com.superkiment.main.GameManager.GameState;
import com.superkiment.world.*;

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
        omh = new OpenMonsterHunter(true);
        omh.setupVariables();

        omh.CreateWorld("testWorld", true);

        player = OpenMonsterHunter.game.controlledPlayer;
        game = OpenMonsterHunter.game;
        omh.gameManager.setGameState(GameState.GAME);
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
        omh.delay(100);
        omh.draw();
        omh.draw();
        omh.draw();
        omh.draw();
        omh.delay(100);
        omh.releaseKey('d');

        PVector newPosition = player.getPos().copy();
        System.out.println();
        System.out.println("moved : " + OpenMonsterHunter.abs(newPosition.x - oldPosition.x));
        System.out.println();
        assertTrue(newPosition.x != oldPosition.x);
    }

}
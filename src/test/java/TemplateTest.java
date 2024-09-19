import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import com.superkiment.entities.Player;
import com.superkiment.main.*;
import com.superkiment.world.*;

import processing.core.PVector;

public class TemplateTest {
    static OpenMonsterHunter omh;
    static Game game;
    static Player player;

    @BeforeAll
    public static void setUp() {
        System.out.println("BeforeAll tests");
        // testWorld = new World("Test World", false);
        omh = new OpenMonsterHunter();
        omh.setupVariables();

        omh.CreateWorld("testWorld", true);

        player = OpenMonsterHunter.game.controlledPlayer;
        game = OpenMonsterHunter.game;
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
        System.out.println(oldPosition);
        omh.pressKey('d');
        System.out.println(omh.millis());
        omh.delay(1000);
        omh.releaseKey('d');
        System.out.println(omh.millis());

        PVector newPosition = player.getPos().copy();
        System.out.println(newPosition);
        assertTrue(newPosition.x > oldPosition.x);
    }

}
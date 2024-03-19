import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import processing.net.*;
import processing.core.*;
import processing.data.JSONObject;
import world.World;

class WorldTest {

	static public void skip(int t) {
		int x = 0;
		while (x < t)
			x++;
	}

	static public void connectClient() {
		skip(5000);
		try {
			p = new PApplet();
			c = new Client(p, "127.0.0.1", 5204);
			skip(5000);
		} catch (Exception e) {
		}
	}

	static World world;
	static PApplet p;
	static Client c;

	@BeforeAll
	static void initAll() {
		world = new World("Server test", false);
		world.setup();
		world.draw();
		connectClient();
	}

	@BeforeEach
	void init() {

	}

	@Test
	void Should_Client_Connect() {

		assertEquals(1, world.server.clientCount);
	}

	@Test
	void Should_Server_And_Client_Dire_Bonjour() {

		JSONObject dataPlayer = new JSONObject();
		dataPlayer.put("name", "Player Test");
		c.write(World.createRequest(World.BONJOUR_DU_CLIENT, dataPlayer, "Player test").toString());

		skip(10000);

		world.draw();
		world.draw();
		world.draw();

		skip(10000);

		String dataString = c.readString();
		JSONObject json = JSONObject.parse(dataString);
		System.out.println(json);

		assertEquals(World.BONJOUR_DU_SERVER, json.get("type"));
	}
}

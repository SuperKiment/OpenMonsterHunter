package com.superkiment.world;

import java.util.HashMap;
import java.util.function.Consumer;

import com.superkiment.logic.Entity;

public class CommandsManager {
	World world;
	HashMap<String, Consumer<String[]>> commands;

	public CommandsManager(World w) {
		world = w;
		commands = new HashMap<String, Consumer<String[]>>();

		addCommande("/ping", (args) -> {
			world.EnvoiConsoleTousClients("pong !!!!!!!!!!!!!!!!!", null);
		});

		addCommande("/birth", (args) -> {
			System.out.println(args.length);
			if (args.length == 4) {
				try {
					Class arrayClass = Class.forName(args[3]);
					Entity obj = (Entity) arrayClass.getDeclaredConstructor().newInstance();

					obj.pos.set(Float.parseFloat(args[1]), Float.parseFloat(args[2]));

					world.addEntity(obj.getJSON());

					world.EnvoiConsoleTousClients("summoned " + args[3], null);
				} catch (Exception e) {
					world.EnvoiConsoleTousClients(e.toString(), null);
				}

			}
		});
	}

	public void TraiterCommande(String str) {
		if (str.isEmpty())
			return;

		String[] command = str.split(" ");

		commands.get(command[0]).accept(command);
	}

	public void addCommande(String s, Consumer<String[]> c) {
		commands.put(s, c);
	}
}

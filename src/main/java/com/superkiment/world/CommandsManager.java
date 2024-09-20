package com.superkiment.world;

import java.util.HashMap;
import java.util.function.Consumer;

import com.superkiment.entities.logic.Entity;

/**
 * Gets commands, processes them and makes action in the world
 */
public class CommandsManager {
    /**
     * World in which actions are made from commands
     */
    World world;

    /**
     * Links string commands to actions from sending a chat to summoning an entity
     */
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
                    Class<?>arrayClass = Class.forName(args[3]);
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

    /**
     * Processes the a command as string like "/birth 10 10
     * com.superkiment.entities.Dog"
     * 
     * @param str the command to be processed
     */
    public void TraiterCommande(String str) {
        if (str.isEmpty())
            return;

        String[] command = str.split(" ");

        commands.get(command[0]).accept(command);
    }

    /**
     * Adds a command to the commands list
     * 
     * <p>
     * <strong>addCommande("/ping", (args) -> {</strong>
     * </p>
     * <p>
     * <strong>world.EnvoiConsoleTousClients("pong !!!!!!!!!!!!!!!!!",
     * null);</strong>
     * </p>
     * <p>
     * <strong>});</strong>
     * </p>
     * 
     * @param s the command to add like "/birth"
     * @param c the action to be added
     */
    public void addCommande(String s, Consumer<String[]> c) {
        commands.put(s, c);
    }
}

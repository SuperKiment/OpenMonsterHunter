package com.superkiment.world;

import com.superkiment.entities.Dog;
import com.superkiment.entities.Player;
import com.superkiment.entities.logic.Entity;
import com.superkiment.entities.logic.EntityManager;
import com.superkiment.entities.logic.Interactable;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.net.Client;
import processing.net.Server;

import java.util.HashMap;

public class World extends PApplet {

    /**
     * Délimiteur entre les blocs de data
     */
    final static String DELIMITER_ENTETE = ":::::";

    /**
     * Premier envoi des données du client au server
     */
    final static String BONJOUR_DU_CLIENT = "buongiorno toi hihi";

    /**
     * Premier envoi des données du server au client
     */
    final static String BONJOUR_DU_SERVER = "yeepii cest moi le serv de la mort qui tue";

    /**
     * Envoi des données du player du client au server
     */
    final static String UPDATE_PLAYER_DATA = "coucoujupdate lez gooooooo";

    /**
     * Envoi des données d'une entité créée par le client vers le server
     */
    final static String NEW_ENT_FROM_PLAYER = "draw her giving birth mouahahaha";

    /**
     * Envoi d'un chat ou d'une commande du client au server
     */
    final static String NEW_CONSOLE_INPUT = "A chicken burger and uuuuuuuuuuuuhhhhhh";

    /**
     * Envoi des données de toutes les entités à montrer au client du server
     */
    final static String UPDATE_WORLD_STATE_ENTITIES = "thats tim thats tom thats cthulhu";

    /**
     * Envoi d'un nouvel input de console du server à tous les clients
     */
    final static String CONSOLE_INPUT_FOR_EVERYONE = "hear me out boyyyzzz";

    /**
     * Envoi d'un nouvel input de console du server à tous les clients
     */
    final static String INTERACTION_ENTITIES = "heyyy i just wanna interact ykykykyk";

    public String name = "NoName";
    private Server server;
    private boolean render = false;

    private final CommandsManager commandsManager;

    private final EntityManager entityManager;

    public World() {
        this("Server", true);
    }

    public World(String name, boolean render) {
        super();
        this.name = name;
        this.render = render;

        commandsManager = new CommandsManager(this);
        entityManager = new EntityManager();
    }

    public static void main(String[] args) {
        try {
            PApplet.main("com.superkiment.world.World");
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    /**
     * Crée un JSONObject qui contient toutes les informations d'un envoi. Permet la
     * standardisation
     *
     * @param type   : constante de World
     * @param data   : les données
     * @param sender : l'envoyeur des données
     * @return JSONObject
     */
    public static JSONObject createRequest(String type, JSONObject data, String sender) {
        JSONObject json = new JSONObject();

        json.put("type", type);
        json.put("data", data);
        json.put("sender", sender);

        return json;

    }

    public void settings() {
        size(300, 800);
        noSmooth();

    }

    public void setup() {
        server = new Server(this, 5204);
        frameRate(1000);
    }

    public void draw() {

        // RENDER
        if (render)
            Render();

        // CLIENTS RECUP
        entityManager.RemoveDisconnectedPlayers();

        TraiterClients();

        // CLIENTS ENVOI
        for (Client c : server.clients) {
            if (c != null && entityManager.isClientAsPlayer(c)) {
                c.write(createRequest(UPDATE_WORLD_STATE_ENTITIES, getJSON(), "server") + DELIMITER_ENTETE);

            }
        }

        // UPDATE
        for (Entity entity : entityManager.getEntities()) {
            // if (entity.getClass().getName().equals(Player.class.getName()))
            entity.Update();
        }

    }

    private void Render() {
        background(0);
        fill(255);
        textSize(15);
        text("FrameRate : " + frameRate, 50, 30);
        text("Nombre de clients : " + server.clientCount, 50, 50);
        text("Nom : " + name, 50, 70);
        text("Nombre d'entités : " + entityManager.getEntities().size(), 50, 90);

        int compt = 0;
        pushStyle();
        for (Client c : server.clients) {
            if (c != null) {
                try {
                    translate(0, 200);
                    Player p = entityManager.getPlayer(c);
                    text(c.ip() + " : " + p.name + " / " + p.pos, 10, 20 * compt++);
                } catch (Exception e) {

                }
            }
        }
        popStyle();
    }

    /**
     * Etape 1 du traitement des données des clients. Passe par tous les clients et
     * exécute TraiterRequete
     */
    private void TraiterClients() {
        // Get the next available client
        Client client = server.available();

        if (client != null) {
            String clientData = client.readString();

            for (String data : clientData.split(DELIMITER_ENTETE)) {
                if (!data.equals(""))
                    TraiterRequete(data, client);
            }

        }
    }

    /**
     * Etape 2 du traitement des clients. Prend le string entier d'un client ainsi
     * que le client pour traiter la requête.
     *
     * @param fullData
     * @param client
     */
    private void TraiterRequete(String fullData, Client client) {
        // println("fullData : " + fullData);

        JSONObject requete = JSONObject.parse(fullData);

        // println("requete : " + requete);
        switch (requete.getString("type")) {
            case BONJOUR_DU_CLIENT:
                System.out.println("Recu bonjour du client");
                JSONObject dataPlayer = requete.getJSONObject("data");
                Player p = entityManager.addPlayer(dataPlayer, client);
                System.out.println("id player : " + p.ID);
                JSONObject playerDataSend = p.getJSON();
                playerDataSend.put("ID", p.ID);
                System.out.println(playerDataSend);
                client.write(createRequest(BONJOUR_DU_SERVER, playerDataSend, "server").toString());
                break;

            case UPDATE_PLAYER_DATA:
                entityManager.getPlayer(client).UpdateFromJSON(requete.getJSONObject("data"));
                break;

            case NEW_ENT_FROM_PLAYER:
                println(NEW_ENT_FROM_PLAYER, requete.getJSONObject("data"));
                entityManager.addEntity(requete.getJSONObject("data"));
                break;

            case NEW_CONSOLE_INPUT:
                println(NEW_CONSOLE_INPUT, requete.getJSONObject("data"));
                String input = requete.getJSONObject("data").getString("text");
                if (input.charAt(0) == '/') {
                    commandsManager.TraiterCommande(input);
                } else {
                    EnvoiConsoleTousClients(input, client);
                }

                break;
            case INTERACTION_ENTITIES:
                System.out.println("Interaction");
                // System.out.println(requete);
                JSONObject data = requete.getJSONObject("data");

                Interactable entityInteracted = (Interactable) this.entityManager.entityStorage
                        .getEntityFromID(data.getString("entityInteractedID"));

                Interactable entityInteracting = (Interactable) this.entityManager.entityStorage
                        .getEntityFromID(data.getString("entityInteractingID"));

                System.out.println(entityInteracted);
                System.out.println(entityInteracting);

                if (entityInteracted != null && entityInteracting != null) {
                    entityInteracting.getInteractionManager().InteractWith(entityInteracted);
                }

                break;
            default:
                println("Jsp comment traiter : " + fullData);
                break;
        }

    }

    public void keyPressed() {
        if (key == 'h') {
            for (Client client : server.clients) {
                if (client != null)
                    server.disconnect(client);
            }
            surface.setVisible(false);
            dispose();
        }

        if (key == 'd') {
            Dog d = new Dog();
            d.pos.set(200, 200);
            entityManager.addEntity(d);
        }
    }

    /**
     * @return Toutes les données du World en JSONObject
     */
    private JSONObject getJSON() {
        JSONObject json = new JSONObject();

        HashMap<String, JSONArray> entities = new HashMap<String, JSONArray>();

        for (Entity e : entityManager.getEntities()) {
            String className = e.getClass().getName();

            if (!entities.containsKey(className))
                entities.put(className, new JSONArray());

            entities.get(className).append(e.getJSON());
        }

        entities.forEach((key, value) -> {
            json.put(key, entities.get(key));
        });

        return json;
    }

    /**
     * Ajouter une entité à partir d'un JSON
     *
     * @param json l'entité à créer
     */
    public void addEntity(JSONObject json) {
        entityManager.addEntity(json);
    }

    /**
     * Envoie un String sur les consoles de tous les clients à part l'envoyeur
     *
     * @param str
     * @param sender
     */
    public void EnvoiConsoleTousClients(String str, Client sender) {
        System.out.println("From Server : " + str);

        for (Client c : server.clients) {
            if (c != null && c != sender) {
                println(c.ip());
                JSONObject json = new JSONObject();
                json.setString("text", str);
                json.setString("sender", sender == null ? "Server" : entityManager.getPlayer(sender).name);
                c.write(createRequest(CONSOLE_INPUT_FOR_EVERYONE, json, "server") + DELIMITER_ENTETE);
            }
        }
    }
}

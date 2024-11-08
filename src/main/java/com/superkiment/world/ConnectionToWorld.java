package com.superkiment.world;

import com.superkiment.entities.logic.Entity;
import com.superkiment.entities.logic.JSONFieldName;
import com.superkiment.entities.logic.Interactable;
import com.superkiment.main.Console;
import com.superkiment.main.Game;
import com.superkiment.main.OpenMonsterHunter;
import processing.data.JSONObject;
import processing.net.Client;

import java.util.ArrayList;

public class ConnectionToWorld {
    public Client client;
    OpenMonsterHunter omh;

    public ConnectionToWorld(OpenMonsterHunter omh, String address, Game game) {
        this.omh = omh;

        client = new Client(omh, address, 5204);

        JSONObject dataPlayer = new JSONObject();
        dataPlayer.put(JSONFieldName.PLAYER_NAME.getValue(), omh.playerName);
        System.out.println(OpenMonsterHunter.game.controlledPlayer);
        JSONObject bonjourJSON = World.createRequest(World.BONJOUR_DU_CLIENT, dataPlayer, omh.playerName);

        client.write(bonjourJSON + World.DELIMITER_ENTETE);
        // System.out.println("Mon player :");
        omh.delay(200);

        String dataString = client.readString();
        JSONObject reponse = JSONObject.parse(dataString);

        game.connexion = this;

        // System.out.println("reponse :");
        // System.out.println(reponse);
        // System.out.println(reponse.getString("type"));

        while (reponse.getString("type") == null) {

            dataString = client.readString();
            reponse = JSONObject.parse(dataString);

            // System.out.println("reponse :");
            // System.out.println(reponse);
            // System.out.println(reponse.getString("type"));
        }
        try {
            if (reponse.getString("type").equals(World.BONJOUR_DU_SERVER)) {
                System.out.println("Arrive du server :");
                System.out.println(reponse.getJSONObject("data"));
                omh.setControllablePlayer(reponse.getJSONObject("data"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean isConnected() {
        return client != null && client.ip() != null;
    }

    public void Update() {

        JSONObject[] datas = RecuperationDonnees();

        for (JSONObject data : datas) {
            if (data == null) {
                System.out.println();
                System.out.println("Data from ConnectionToWorld is null !");
                System.out.println();

                continue;
            }

            switch (data.getString("type")) {
                case World.UPDATE_WORLD_STATE_ENTITIES:
                    TraiterDonnees(data.getJSONObject("data"));
                    break;
                case World.CONSOLE_INPUT_FOR_EVERYONE:
                    // System.out.println(data);
                    JSONObject input = data.getJSONObject("data");
                    Console.console.write(input.getString("sender") + " : " + input.getString("text"), true);
                    break;
            }
        }

        EnvoiDonneesPlayer();

    }

    private void EnvoiDonneesPlayer() {
        if (OpenMonsterHunter.game.controlledPlayer != null)
            client.write(
                    World.createRequest(World.UPDATE_PLAYER_DATA, OpenMonsterHunter.game.controlledPlayer.getJSON(),
                            omh.playerName) + World.DELIMITER_ENTETE);
    }

    public void EnvoiDonneesNouvelleEntite(JSONObject json) {
        if (OpenMonsterHunter.game.controlledPlayer != null)
            client.write(World.createRequest(World.NEW_ENT_FROM_PLAYER, json, omh.playerName)
                    + World.DELIMITER_ENTETE);
    }

    public void EnvoiConsoleInput(String input) {
        if (input.isEmpty())
            return;

        JSONObject json = new JSONObject();
        json.setString("text", input);

        client.write(
                World.createRequest(World.NEW_CONSOLE_INPUT, json, omh.playerName) + World.DELIMITER_ENTETE);
    }

    public void EnvoiInteraction(Interactable interactedWith, Entity interacting) {
        if (interactedWith == null)
            return;

        JSONObject json = new JSONObject();

        json.setString("entityInteractingID", interacting.ID);
        json.setString("entityInteractedID", interactedWith.getEntity().ID);

        client.write(World.createRequest(World.INTERACTION_ENTITIES, json, omh.playerName) + World.DELIMITER_ENTETE);
    }

    private JSONObject[] RecuperationDonnees() {

        ArrayList<JSONObject> objects = new ArrayList<JSONObject>();

        if (client.available() > 0) {
            String fullData = client.readString();

            // Split par le délimiter
            String[] parts = fullData.split(World.DELIMITER_ENTETE);
            for (String part : parts) {

                JSONObject data = null;
                try {
                    data = JSONObject.parse(part);
                } catch (Exception e) {
                    System.out.println();
                    System.out.println("Pas réussi à parse ! " + e.getMessage());
                    System.out.println(part);
                    System.out.println();
                }

                /*
                 * if (data != null) {
                 * omh.pushStyle();
                 * omh.fill(255);
                 * omh.textSize(10);
                 * omh.text(data.toString(), 50, 150);
                 * omh.popStyle();
                 * }
                 */

                objects.add(data);
            }
        }

        JSONObject[] res = new JSONObject[objects.size()];

        for (int i = 0; i < objects.size(); i++) {
            res[i] = objects.get(i);
        }

        return res;
    }

    private void TraiterDonnees(JSONObject data) {
        if (data == null)
            return;

        OpenMonsterHunter.game.TraiterData(data);

    }
}

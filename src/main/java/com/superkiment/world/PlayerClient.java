package com.superkiment.world;

import com.superkiment.entities.Player;

import processing.net.Client;

public class PlayerClient {
    private Client client;
    private Player player;

    public PlayerClient(Client client) {
        this.client = client;
    }

    public PlayerClient(Client client, Player player) {
        this.client = client;
        this.player = player;
    }

    public Client getClient() {
        return client;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
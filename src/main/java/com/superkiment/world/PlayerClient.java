package com.superkiment.world;

import com.superkiment.entities.Player;

import processing.core.PApplet;
import processing.net.Client;

public class PlayerClient extends Client {
    Player player;

    public PlayerClient(PApplet p, String ip, int port) {
        super(p, ip, port);
    }

    public PlayerClient(PApplet p, String ip, int port, Player player) {
        super(p, ip, port);
        this.setPlayer(player);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}

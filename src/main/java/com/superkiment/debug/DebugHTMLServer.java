package com.superkiment.debug;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.data.JSONObject;
import processing.net.Client;
import processing.net.Server;

public class DebugHTMLServer extends PApplet {
    final static int PORT = 5205;

    Server server;
    ArrayList<JSONObject> requests = new ArrayList<JSONObject>();

    public static void main(String[] args) {
        try {
            PApplet.main("com.superkiment.debug.DebugHTMLServer");
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public void settings() {
        size(800, 800);
        noSmooth();
    }

    public void setup() {
        server = new Server(this, DebugHTMLServer.PORT);
        frameRate(10);
        fill(255);
    }

    public void draw() {
        background(0);
        text(frameCount, 50, 50);
        traiterClients();
        renderRequests();
    }

    public void traiterClients() {
        Client client = server.available();
        while (client != null) {
            String request = client.readString();
            try {
                JSONObject jsonRequest = JSONObject.parse(request);
                if (request != null) {
                    println(jsonRequest);
                    requests.add(jsonRequest);
                }
            } catch (Exception e) {
            }

            client = server.available();
        }
    }

    public void renderRequests() {
        String res = "";
        for (JSONObject json : requests) {
            res += json.toString() + "\n";
        }

        pushStyle();
        text(res, 50, 100);
        popStyle();

    }
}

package com.superkiment.debug;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.superkiment.entities.logic.JSONFieldName;
import com.superkiment.world.World;

import processing.core.PApplet;
import processing.data.JSONObject;
import processing.net.Client;

public class DebugHTML {
    static final String filePath = "./logs/";
    private ArrayList<DebugNetElement> debugNetElements = new ArrayList<DebugNetElement>();
    private String avant = "", apres = "";
    private Client client;
    private PApplet pApplet;

    public DebugHTML(PApplet pApplet) {
        this.pApplet = pApplet;

        client = new Client(pApplet, "localhost", 5205);
        try {
            avant = new String(Files.readAllBytes(Paths.get(filePath + "index-avant.html")),
                    StandardCharsets.UTF_8);
            apres = new String(Files.readAllBytes(Paths.get(filePath + "index-apres.html")),
                    StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Une erreur s'est produite lors de la lecture de avant et apres : " + e.getMessage());
        }

        sendRequest(World.createRequest(World.BONJOUR_DU_CLIENT, null, "player25"));
    }

    public void sendRequest(JSONObject request) {
        client.write(request.toString());
    }

    public void FileCheckAndCreate(String file) {
        Path path = Paths.get(filePath + "index" + file + ".html");

        if (Files.exists(path)) {
            System.out.println("Le fichier existe déjà.");
        } else {
            try {
                Files.createFile(path);
                System.out.println("Le fichier a été créé.");
            } catch (IOException e) {
                System.err.println("Une erreur s'est produite lors de la création du fichier : " + e.getMessage());
            }
        }
    }

    public void modifyHtmlFile(String replacement, String file) {
        FileCheckAndCreate(file);

        try {

            Files.write(Paths.get(filePath + "/index" + file + ".html"),
                    (avant + replacement + apres).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loop(String file) {
        String replacement = "";

        for (DebugNetElement debugNetElement : debugNetElements) {
            replacement += debugNetElement.getHTML();
        }

        modifyHtmlFile(replacement, file);
    }

    public void addRequest(JSONObject request) {
        String sender = request.getString(JSONFieldName.REQUEST_SENDER.getValue());

        try {
            debugNetElements.add(new DebugNetElement(sender != "server",
                    request.getString(JSONFieldName.REQUEST_TYPE.getValue()), request));

        } catch (Exception e) {
        }

        loop(sender);
    }
}

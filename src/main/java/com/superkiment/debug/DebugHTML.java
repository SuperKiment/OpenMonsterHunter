package com.superkiment.debug;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.superkiment.entities.logic.JSONFieldName;
import com.superkiment.world.World;

import processing.data.JSONObject;

public class DebugHTML {
    static final String filePath = "./logs/";
    static private ArrayList<DebugNetElement> debugNetElements = new ArrayList<DebugNetElement>();

    public static void FileCheckAndCreate() {
        Path path = Paths.get(filePath + "index.html");

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

        JSONObject data = new JSONObject();
        data.put("title", "Debug Log");
        // debugNetElements.add(new DebugNetElement(true, World.BONJOUR_DU_CLIENT,
        // data));
    }

    public static void modifyHtmlFile(String replacement) {
        try {
            String avant = new String(Files.readAllBytes(Paths.get(filePath + "index-avant.html")),
                    StandardCharsets.UTF_8);
            String apres = new String(Files.readAllBytes(Paths.get(filePath + "index-apres.html")),
                    StandardCharsets.UTF_8);

            Files.write(Paths.get(filePath + "/index.html"),
                    (avant + replacement + apres).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loop() {
        String replacement = "";

        for (DebugNetElement debugNetElement : debugNetElements) {
            replacement += debugNetElement.getHTML();
        }

        modifyHtmlFile(replacement);
    }

    public static void addRequest(JSONObject request, boolean isClientSender) {
        System.out.println("DebugHTML.addRequest");
        try {
            debugNetElements.add(new DebugNetElement(isClientSender,
                    request.getString(JSONFieldName.REQUEST_TYPE.getValue()), request));

        } catch (Exception e) {

        }

        loop();
    }
}

package com.superkiment.debug;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DebugHTML {
    static final String filePath = "./logs/history.txt";

    public static void FileCheckAndCreate() {
        Path path = Paths.get(filePath);

        // Vérifier si le fichier existe
        if (Files.exists(path)) {
            System.out.println("Le fichier existe déjà.");
        } else {
            try {
                // Créer le fichier
                Files.createFile(path);
                System.out.println("Le fichier a été créé.");
            } catch (IOException e) {
                System.err.println("Une erreur s'est produite lors de la création du fichier : " + e.getMessage());
            }
        }
    }

    public static void modifyHtmlFile(String newContent) {
        try {
            
        } catch (Exception e) {
        }
    }
}

package com.superkiment.debug;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
            // Ouvrir le fichier HTML
            File input = new File(filePath);
            Document doc = Jsoup.parse(input, "UTF-8");

            // Trouver le contenu balisé dans la classe "to-change"
            Element element = doc.selectFirst(".to-change");
            if (element != null) {
                // Modifier le contenu
                element.text(newContent);

                // Écrire les modifications dans le fichier
                try (FileWriter writer = new FileWriter(filePath)) {
                    writer.write(doc.outerHtml());
                }
                System.out.println("Le contenu a été modifié avec succès.");
            } else {
                System.out.println("Aucun élément avec la classe 'to-change' n'a été trouvé.");
            }
        } catch (IOException e) {
            System.err.println("Une erreur s'est produite : " + e.getMessage());
        }
    }
}

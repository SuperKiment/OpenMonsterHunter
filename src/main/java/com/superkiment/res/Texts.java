package com.superkiment.res;

import java.util.Random;

public class Texts {
    public static String[] splashTexts = {"Chasseurs de monstres, à vos armes !",
            "En équipe ou en solitaire, la chasse ne fait que commencer.", "Construisez, survivez, prospérez.",
            "Des aventures à vivre, des monstres à vaincre.",
            "Des maisons modestes aux forteresses, votre destin est entre vos mains.",
            "Explorez des mondes mystérieux, affrontez des créatures redoutables.",
            "La nuit est sombre et pleine de terreurs.", "Un abri sûr peut faire toute la différence.",
            "Des amis, des ennemis, des trésors cachés.", "Affrontez les éléments, survivez à la tempête.",
            "L'éclipse approche, préparez-vous au pire.", "La lune rouge se lève, la chasse est ouverte.",
            "Des secrets enfouis, des défis à relever.", "Les ténèbres s'étendent, mais la lumière l'emportera.",
            "Des artefacts anciens, des mystères à résoudre.", "L'union fait la force, coopérez pour survivre.",
            "Des monstres terrifiants, des récompenses alléchantes.",
            "Chaque décision compte, chaque action a des conséquences.",
            "Des tempêtes de sable aux orages déchaînés, la nature est implacable.",
            "La nuit éternelle approche, le combat pour la survie commence.",
            "Du bois et de la pierre, les fondations de votre aventure.",
            "Des cavernes obscures aux sommets enneigés, explorez chaque recoin.",
            "Des héros légendaires naissent dans l'adversité.", "Des potions magiques, des potions de survie.",
            "Des créatures célestes descendent sur vous.", "Rassemblez vos forces, préparez-vous à la bataille.",
            "Des ennemis redoutables, des alliés inattendus.", "La nuit des morts-vivants est arrivée.",
            "Des artefacts perdus, des pouvoirs oubliés.",
            "Dans l'ombre des monstres, la lumière de l'aventure brille toujours."};

    public static String getOneRandomSplashText() {
        Random randomNumbers = new Random();
        return splashTexts[randomNumbers.nextInt(splashTexts.length)];

    }
}

package com.superkiment.TestsRandoms;

import java.util.HashMap;
import java.util.function.Consumer;

import com.superkiment.entities.logic.Entity;
import com.superkiment.entities.logic.EntityJSONUpdater;
import com.superkiment.entities.logic.JSONFieldName;

import processing.core.PVector;
import processing.data.JSONObject;

@SuppressWarnings("unused")

public class Tester {

    public static void main(String[] args) {
        System.out.println("Début test");
        /*
         * 
         * HashMap<String, Consumer<String>> commands = new HashMap<String,
         * Consumer<String>>();
         * 
         * commands.put("a", (str) -> {
         * System.out.println(str);
         * });
         * 
         * commands.get("a").accept("coucou");
         */
        // lezgo();

        /*
         * HashMap<Class, Getmethod> hash = new HashMap<Class, Getmethod>();
         * hash.put(logic.Entity.class, new Getmethod() { public void Action() {
         * System.out.println("coucou"); } });
         *
         * logic.Entity e = new logic.Entity(); logic.Dog d = new logic.Dog();
         *
         * hash.get(e.getClass()).Action(); hash.get(d.getClass()).Action();
         *
         * 
         * HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
         * hashMap.put("a", 1);
         * 
         * String a = "a";
         * 
         * System.out.println(hashMap.get(a));
         * System.out.println();
         * System.out.println();
         * System.out.println();
         * System.out.println();
         * System.out.println();
         * System.out.println();
         */

        Entity entity = new Entity();

        EntityJSONUpdater entityJSONUpdater = new EntityJSONUpdater(entity);
        JSONObject json = new JSONObject();
        json.put(JSONFieldName.POSITION_X.getValue(), 100.5);
        json.put(JSONFieldName.POSITION_Y.getValue(), 150.5);
        json.put("pos.z", 150.5);
        json.put(JSONFieldName.SPEED.getValue(), 10.7);
        json.put(JSONFieldName.TEXT_SAYING.getValue(), "bruh");

        entityJSONUpdater.UpdateFromJSON(json);

        System.out.println("update : " + entity.getJSON());
    }

    public static void lezgo() {
        Abc abc = new Abc("a");

        if (abc instanceof InterfaceTest) {
            System.out.println("instanceof InterfaceTest");
            abc.test();
        }

    }

    public void Classes() {
        Abc abc = new Abc("a");

        try {
            Class<?> test = Class.forName("TestsRandoms.Abc");
            System.out.println("Trouvé classe " + test.getName());

            Object o = test.getDeclaredConstructor(String.class).newInstance("fez");
        } catch (Exception e) {

            System.out.println("Erreur : " + e);
        }
    }
}

package com.superkiment.entities.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

import processing.data.JSONObject;

public class EntityJSONUpdater {
    Entity entity;

    ArrayList<Consumer<Object>> consumers;

    public EntityJSONUpdater(Entity entity) {
        this.entity = entity;
        consumers = new ArrayList<Consumer<Object>>();
    }

    public void UpdateFromJSON(JSONObject jsonData) {
        Iterator<String> keys = jsonData.keys().iterator();

        while (keys.hasNext()) {
            String key = keys.next();
            System.out.println(key);

            jsonData.getFloat("pos.x");
        }
    }

}

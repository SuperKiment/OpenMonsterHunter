package com.superkiment.entities.logic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Consumer;

import processing.data.JSONObject;

public class EntityJSONUpdater {
    Entity entity;

    HashMap<String, Consumer<Object>> consumers;

    public EntityJSONUpdater(Entity entity) {
        this.entity = entity;
        consumers = new HashMap<String, Consumer<Object>>();

        consumers.put("pos.x", newData -> {
            entity.pos.x = ((int) newData);
        });

        consumers.put("pos.y", newData -> {
            entity.pos.y = (int) newData;
        });

        consumers.put("dir.x", newData -> {
            entity.dir.x = (int) newData;
        });

        consumers.put("dir.y", newData -> {
            entity.dir.y = (int) newData;
        });
    }

    public void UpdateFromJSON(JSONObject jsonData) {
        Iterator<String> keys = jsonData.keys().iterator();

        while (keys.hasNext()) {
            String key = keys.next();
            System.out.println(key);

            Consumer<Object> comportementConsumer = consumers.get(key);
            if (comportementConsumer != null) {
                comportementConsumer.accept(jsonData.get(key));
            } else {
                System.err.println("Key " + key + " for consumer from JSON data not found !");
            }
        }
    }
}

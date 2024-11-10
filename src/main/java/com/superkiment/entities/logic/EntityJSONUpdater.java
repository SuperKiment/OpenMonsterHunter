package com.superkiment.entities.logic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

import processing.data.JSONObject;

public class EntityJSONUpdater {
    Entity entity;

    HashMap<String, Consumer<Object>> consumers;

    public EntityJSONUpdater(Entity entity) {
        this.entity = entity;
        consumers = new HashMap<String, Consumer<Object>>();

        consumers.put(JSONFieldName.POSITION_X.getValue(), newData -> {
            Number number = (Number) newData;
            entity.pos.x = number.floatValue();
        });

        consumers.put(JSONFieldName.POSITION_Y.getValue(), newData -> {
            Number number = (Number) newData;
            entity.pos.y = number.floatValue();
        });

        consumers.put(JSONFieldName.DIRECTION_X.getValue(), newData -> {
            Number number = (Number) newData;
            entity.dir.x = number.floatValue();
        });

        consumers.put(JSONFieldName.DIRECTION_Y.getValue(), newData -> {
            Number number = (Number) newData;
            entity.dir.y = number.floatValue();
        });

        consumers.put(JSONFieldName.TEXT_SAYING.getValue(), newData -> {
            entity.sayingBox.setSayingText((String) newData);
        });

        consumers.put(JSONFieldName.SPEED.getValue(), newData -> {
            Number number = (Number) newData;
            entity.speed = number.floatValue();
        });

        consumers.put("name", newData -> {
        });
        consumers.put("ID", newData -> {
        });
        consumers.put("className", newData -> {
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
                System.err.println();
                System.err.println("Key " + key + " for consumer from JSON data not found !");
                System.err.println("Incoming data : " + jsonData.toString());

                System.err.print("Consumers");
                for (Map.Entry<String, Consumer<Object>> entry : consumers.entrySet()) {
                    System.err.print(" : " + entry.getKey());
                }

                System.err.println();
                System.err.println();

            }
        }
    }
}

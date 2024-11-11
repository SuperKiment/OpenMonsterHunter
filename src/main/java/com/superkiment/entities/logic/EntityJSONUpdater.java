package com.superkiment.entities.logic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

import processing.data.JSONObject;

public class EntityJSONUpdater {
    /**
     * Updated entity
     */
    private Entity entity;

    /**
     * Behaviours of the updater : link a String to a Consumer<Object>.
     * The String is the entity's property name and the Consumer is what the updater
     * has to do with the value.
     */
    private HashMap<String, Consumer<Object>> consumers;

    public JSONObject lastJSONProjection;

    public EntityJSONUpdater(Entity entity) {
        this.entity = entity;
        lastJSONProjection = entity.getJSON();

        consumers = new HashMap<String, Consumer<Object>>();

        consumers.put(JSONFieldName.POSITION_X.getValue(), newData -> {
            String number = (String) newData;
            entity.pos.x = Math.round(Float.parseFloat(number));
        });

        consumers.put(JSONFieldName.POSITION_Y.getValue(), newData -> {
            String number = (String) newData;
            entity.pos.y = Math.round(Float.parseFloat(number));
        });

        consumers.put(JSONFieldName.DIRECTION_X.getValue(), newData -> {
            String number = (String) newData;
            entity.dir.x = Math.round(Float.parseFloat(number));
        });

        consumers.put(JSONFieldName.DIRECTION_Y.getValue(), newData -> {
            String number = (String) newData;
            entity.dir.y = Math.round(Float.parseFloat(number));
        });

        consumers.put(JSONFieldName.TEXT_SAYING.getValue(), newData -> {
            entity.sayingBox.setSayingText((String) newData);
        });

        consumers.put(JSONFieldName.SPEED.getValue(), newData -> {
            String number = (String) newData;
            entity.speed = Float.parseFloat(number);
        });

        consumers.put("name", newData -> {
        });
        consumers.put("ID", newData -> {
        });
        consumers.put("className", newData -> {
        });
    }

    /**
     * Updates the entity based on incoming JSON. Example :
     * <p>
     * <code>
     * <p>{
     * <p>  "pos.x" : "50.5",
     * <p>  "pos.y" : "57.5"
     * <p>}
     * </code>
     * <p>
     * Will update the entity's position.
     * The other properties won't be touched unless they are specified as key in the
     * JSON.
     * 
     * @param jsonData
     */
    public void UpdateFromJSON(JSONObject jsonData) {
        Iterator<String> keys = jsonData.keys().iterator();

        while (keys.hasNext()) {
            String key = keys.next();
            // System.out.println("Updating : " + key);

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

    public JSONObject getWhatHasChangedJSON() {
        JSONObject whatHasChanged = new JSONObject();
        JSONObject complete = entity.getJSON();

        // System.out.println();
        for (Object keyObj : complete.keys()) {
            String key = (String) keyObj;

            Object completeVal = complete.get(key);
            Object lastJSONProjectionVal = lastJSONProjection.get(key);

            if (!completeVal.equals(lastJSONProjectionVal)) {
                whatHasChanged.put(key, completeVal);
                // System.out.println("changed : " + key + "\t -> " + completeVal);
            }
        }

        lastJSONProjection = this.entity.getJSON();
        return whatHasChanged;
    }
}

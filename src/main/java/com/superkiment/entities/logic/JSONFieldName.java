package com.superkiment.entities.logic;

public enum JSONFieldName {
    // Valeurs
    POSITION_X("pos.x"),
    POSITION_Y("pos.y"),
    DIRECTION_X("dir.x"),
    DIRECTION_Y("dir.y"),
    TEXT_SAYING("textSaying"),
    SPEED("speed"),
    ID("ID"),
    CLASS_NAME("className"),

    PLAYER_NAME("name"),
    ENTITY_INTERACTED_ID("entityInteractedID"),
    ENTITY_INTERACTING_ID("entityInteractingID"),

    REQUEST_DATA("data"),
    REQUEST_TYPE("type"),
    REQUEST_SENDER("sender"),

    CONSOLE_TEXT("text");

    // Code pour chaque valeur
    private final String value;

    JSONFieldName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
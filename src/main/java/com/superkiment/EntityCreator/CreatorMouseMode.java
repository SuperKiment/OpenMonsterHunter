package com.superkiment.EntityCreator;

public enum CreatorMouseMode {
    ADD_SHAPE("add_shape"),
    DEL_SHAPE("del_shape"),
    MOVE_SHAPE("move_shape"),
    SCALE_SHAPE("scale_shape"),
    ROTATE_SHAPE("rotate_shape"),
    SEL_SHAPE("sel_shape");

    private final String value;

    CreatorMouseMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
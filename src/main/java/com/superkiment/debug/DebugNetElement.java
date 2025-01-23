package com.superkiment.debug;

import processing.data.JSONObject;

public class DebugNetElement {

    private boolean isClientSender = false;
    private String type;
    private JSONObject data;

    public DebugNetElement(boolean isClientSender, String type, JSONObject data) {
        this.isClientSender = isClientSender;
        this.type = type;
        this.data = data;
    }

    public String getHTML() {
        return "<div class=\"net-element-"+(isClientSender ? "right" : "left")+"\"><h3>DebugNetElement</h3>"
                + "<p>From : " + (isClientSender ? "Client" : "Server") + "</p>"
                + "<p>type : " + type + "</p>"
                + "<p>data : " + data + "</p></div>";

    }
}
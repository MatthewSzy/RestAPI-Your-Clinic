package com.surgery.enumeration;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ReservationType {

    @JsonProperty(value = "Online")
    ONLINE("Online"),

    @JsonProperty(value = "Na miejscu")
    NON_ONLINE("Na miejscu");

    private final String value;

    ReservationType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

package com.kyulix.RGTestApp.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

public class ResponseMessageResource extends ResourceSupport {

    @JsonProperty("code")
    private int responseCode;

    @JsonProperty("debug_message")
    private String debugMessage;

    @JsonProperty("debug_objects")
    private List<Object> debugObjects;

    @JsonCreator
    public ResponseMessageResource(int responseCode) {
        this.responseCode = responseCode;
        this.debugObjects = new ArrayList<Object>();
    }

    public ResponseMessageResource(int responseCode, String debugMessage) {
        this(responseCode);
        this.debugMessage = debugMessage;
    }

    public void addDebugObject(Object objectToAdd) {
        this.debugObjects.add(objectToAdd);
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public String getDebugMessage() {
        return this.debugMessage;
    }
}

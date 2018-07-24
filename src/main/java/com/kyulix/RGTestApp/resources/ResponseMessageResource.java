package com.kyulix.RGTestApp.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

public class ResponseMessageResource extends ResourceSupport {

    @JsonProperty("code")
    private int responseCode;

    @JsonProperty("debug_message")
    private String debugMessage;

    @JsonCreator
    public ResponseMessageResource(int responseCode) {
        this.responseCode = responseCode;
    }

    public ResponseMessageResource(int responseCode, String debugMessage) {
        this.responseCode = responseCode;
        this.debugMessage = debugMessage;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public String getDebugMessage() {
        return this.debugMessage;
    }
}

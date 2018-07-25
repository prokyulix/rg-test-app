package com.kyulix.RGTestApp.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

public class ResponseMessageResource extends ResourceSupport {

    @JsonProperty("responseCode")
    private int responseCode;

    @JsonProperty("responseMessage")
    private String responseMessage;

    public ResponseMessageResource(int responseCode) {
        this.responseCode = responseCode;
    }

    public ResponseMessageResource(int responseCode, String responseMessage) {
        this(responseCode);
        this.responseMessage = responseMessage;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public String getResponseMessage() {
        return this.responseMessage;
    }
}

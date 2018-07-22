package com.kyulix.RGTestApp.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

/*
    В этом классе будет всего лишь два кода (состояния):
    1 -- успешно
    2 -- отклонено

    По-хорошему нужно ввести больше состояний, но это лишнее в данном случае
 */
public class ResponseMessageResource extends ResourceSupport {

    @JsonProperty("code")
    private int responseCode;

    @JsonProperty("message")
    private String responseMessage;

    @JsonCreator
    public ResponseMessageResource(int responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }
}

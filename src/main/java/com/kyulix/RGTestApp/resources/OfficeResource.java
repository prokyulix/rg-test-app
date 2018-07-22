package com.kyulix.RGTestApp.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kyulix.RGTestApp.entities.Office;
import org.springframework.hateoas.ResourceSupport;

public class OfficeResource extends ResourceSupport {

    @JsonProperty("offices")
    private Iterable<Office> offices;

    @JsonCreator
    public OfficeResource(Iterable<Office> offices) {
        this.offices = offices;
    }

    public Iterable<Office> getOffices() {
        return this.offices;
    }
}

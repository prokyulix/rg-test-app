package com.kyulix.RGTestApp.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kyulix.RGTestApp.entities.Office;
import org.springframework.hateoas.ResourceSupport;

import java.util.ArrayList;
import java.util.List;

public class OfficeResource extends ResourceSupport {

    @JsonProperty("offices")
    private List<Office> offices;

    @JsonCreator
    public OfficeResource(List<Office> offices) {
        this.offices = offices;
    }

    @JsonCreator
    public OfficeResource(Office office) {
        this.offices = new ArrayList<>();
        this.offices.add(office);
    }

    public List<Office> getOffices() {
        return this.offices;
    }
}

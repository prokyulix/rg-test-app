package com.kyulix.RGTestApp.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@ToString
public class Office {

    @Id
    @GeneratedValue
    @Getter
    @ToString.Exclude
    private int id;

    @Getter
    @Setter
    @NonNull
    @Column(unique = true)
    private String name;

    @Getter
    @Setter
    @NonNull
    @Column(unique = true)
    private String address;

    @Getter
    @Setter
    @NonNull
    private Boolean isClosed;

    public Office(String name, String address) {
        this.name = name;
        this.address = address;
        this.isClosed = false;
    }
}

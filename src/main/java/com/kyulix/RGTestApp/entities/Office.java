package com.kyulix.RGTestApp.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@ToString
@EqualsAndHashCode
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Boolean active;

    public Office(String name, String address) {
        this.name = name;
        this.address = address;
        this.active = true;
    }
}

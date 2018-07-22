package com.kyulix.RGTestApp.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@ToString
@EqualsAndHashCode
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @ToString.Exclude
    private int id;

    @Getter
    @Setter
    @NonNull
    private String firstName;

    @Getter
    @Setter
    @NonNull
    private String lastName;

    @Getter
    @Setter
    private String eMail;

    @Getter
    @Setter
    private String phoneNumber;

    @Getter
    @Setter
    @NonNull
    private String position;

    @Getter
    @Setter
    @NonNull
    private boolean active;

    @Getter
    @Setter
    @ManyToOne
    private Office workingOffice;

    public Employee(String firstName, String lastName, String position) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.active = true;
    }

}

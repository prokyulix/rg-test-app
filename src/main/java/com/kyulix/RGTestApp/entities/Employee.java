package com.kyulix.RGTestApp.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue
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
    private String isFired;

    @Getter
    @Setter
    @ManyToOne
    @NonNull
    private Office workingOffice;
}

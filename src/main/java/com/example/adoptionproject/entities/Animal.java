package com.example.adoptionproject.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Animal {

    @Id
    private String id;

    private String nom;
    private int age;
    private boolean sterilise;

    // Enums are stored as strings by default
    private Espece espece;
}
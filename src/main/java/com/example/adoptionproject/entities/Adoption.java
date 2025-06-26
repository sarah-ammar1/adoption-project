package com.example.adoptionproject.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Adoption {

    @Id
    private String id;

    private Date dateAdoption;
    private float frais;

    // You can embed the adoptant and animal, or store their IDs
    private Adoptant adoptant;
    private Animal animal;
}
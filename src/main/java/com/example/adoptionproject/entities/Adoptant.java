package com.example.adoptionproject.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "adoptants")
public class Adoptant {

    @Id
    private String id; // MongoDB uses String IDs by default

    private String nom;
    private String adresse;
    private int telephone;

    // Embed adoptions directly (or use DBRefs if needed)
    private List<Adoption> adoptions;
}
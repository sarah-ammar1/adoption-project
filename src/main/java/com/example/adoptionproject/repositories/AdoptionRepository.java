package com.example.adoptionproject.repositories;

import com.example.adoptionproject.entities.Adoption;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AdoptionRepository  extends MongoRepository<Adoption, String> {
    List<Adoption> findByAdoptant_Nom(String nom);
    List<Adoption> findByAdoptant_IdAdoptant(String AdoptantID);

}

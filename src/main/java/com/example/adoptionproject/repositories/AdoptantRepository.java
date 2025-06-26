package com.example.adoptionproject.repositories;

import com.example.adoptionproject.entities.Adoptant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdoptantRepository extends MongoRepository<Adoptant, Integer> {
}

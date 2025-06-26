package com.example.adoptionproject.repositories;

import com.example.adoptionproject.entities.Animal;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnimalRepository extends MongoRepository<Animal, Integer> {
}

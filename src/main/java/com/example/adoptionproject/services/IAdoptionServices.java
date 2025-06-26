package com.example.adoptionproject.services;

import com.example.adoptionproject.entities.*;

import java.util.List;

public interface IAdoptionServices {
    Adoptant addAdoptant(Adoptant adoptant);

    Animal addAnimal(Animal animal);

    Adoption addAdoption(Adoption adoption, String idAdoptant, String idAnimal);

    List<Adoption> getAdoptionsByAdoptant(String nom);

    float calculFraisTotalAdoptions(String idAdoptant);
}

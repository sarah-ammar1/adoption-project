package com.example.adoptionproject;

import com.example.adoptionproject.controllers.AdoptionRestController;
import com.example.adoptionproject.entities.Adoptant;
import com.example.adoptionproject.entities.Animal;
import com.example.adoptionproject.entities.Adoption;
import com.example.adoptionproject.services.IAdoptionServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdoptionRestController.class)
public class AdoptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAdoptionServices adoptionServices;

    private Adoptant adoptant;
    private Animal animal;
    private Adoption adoption;

    @BeforeEach
    void setUp() {
        adoptant = new Adoptant();
        adoptant.setId("1");  // MongoDB uses String ID
        adoptant.setNom("John Doe");
        adoptant.setAdresse("123 Street");
        adoptant.setTelephone(123456789);

        animal = new Animal();
        animal.setId("1");
        animal.setNom("Buddy");

        adoption = new Adoption();
        adoption.setId("1");
        adoption.setDateAdoption(new Date());
        adoption.setFrais(50.0f);
        adoption.setAdoptant(adoptant);
        adoption.setAnimal(animal);
    }

    @Test
    void addAdoptant_shouldReturnAdoptant() throws Exception {
        when(adoptionServices.addAdoptant(any(Adoptant.class))).thenReturn(adoptant);

        String adoptantJson = "{ \"nom\": \"John Doe\", \"adresse\": \"123 Street\", \"telephone\": 123456789 }";

        mockMvc.perform(post("/addAdoptant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(adoptantJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("John Doe"));
    }

    @Test
    void addAnimal_shouldReturnAnimal() throws Exception {
        when(adoptionServices.addAnimal(any(Animal.class))).thenReturn(animal);

        String animalJson = "{ \"nom\": \"Buddy\" }";  // Removed type if not used

        mockMvc.perform(post("/addAnimal")
                .contentType(MediaType.APPLICATION_JSON)
                .content(animalJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Buddy"));
    }

    @Test
    void getAdoptionsByAdoptant_shouldReturnListOfAdoptions() throws Exception {
        when(adoptionServices.getAdoptionsByAdoptant("John Doe")).thenReturn(Arrays.asList(adoption));

        mockMvc.perform(get("/byAdoptant/John Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].frais").value(50.0));
    }


}
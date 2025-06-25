package com.example.adoptionproject;

import com.example.adoptionproject.controllers.AdoptionRestController;
import com.example.adoptionproject.entities.Adoptant;
import com.example.adoptionproject.entities.Adoption;
import com.example.adoptionproject.services.IAdoptionServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(AdoptionRestController.class)
public class AdoptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAdoptionServices adoptionServices;

    @Test
    public void addAdoptant_shouldReturnSavedAdoptant() throws Exception {
        Adoptant mockAdoptant = new Adoptant();
        mockAdoptant.setNom("John Doe" );
        when(adoptionServices.addAdoptant(any(Adoptant.class))).thenReturn(mockAdoptant);

        mockMvc.perform(post("/addAdoptant")
                .contentType("application/json")
                .content("{\"nom\":\"John Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("John Doe"));
    }
    @Test
    void getAdoptionsByAdoptant_shouldReturnListOfAdoptions() throws Exception {
        List<Adoption> adoptions = Arrays.asList(
                new Adoption(), new Adoption()
        );

        when(adoptionServices.getAdoptionsByAdoptant("John Doe")).thenReturn(adoptions);

        mockMvc.perform(get("/byAdoptant/John Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(adoptionServices, times(1)).getAdoptionsByAdoptant("John Doe");
    }
}
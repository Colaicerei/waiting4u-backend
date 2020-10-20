package edu.osu.waiting4ubackend.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PetTest {

    @Test
    public void testPetBuilder() {
        Pet pet = new Pet.PetBuilder().setId("123abc").setPetName("Bubu").build();
        assertEquals("123abc", pet.getId());
        assertEquals("Bubu", pet.getPetName());
        assertNull(pet.getAvailability());
    }
}

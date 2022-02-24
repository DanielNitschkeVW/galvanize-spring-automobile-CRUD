package de.volkswagen.f73.galvanize;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutosController.class)
public class AutosControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AutosService autosService;

    // Get: /api/autos Response 200 Ok
    @Test
    void getAuto_noParams_exists_returnsListOfAutos() throws Exception {
        // Given
        List<Automobile> autos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            autos.add(new Automobile(2000+i, "Volkswagen", "ID.3", "TestVIN" + i));
        }
        when(autosService.getAutos()).thenReturn(new AutosList(autos));

        // When
        mockMvc.perform(get("/api/autos"))

                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));
    }
    /*
        Post: /api/autos Response 200 Ok
        Post: /api/autos Response 400 Error bad request
        Get: /api/autos Response 204 No autos found by that vin

        Get: /api/autos?color={color} Response 200 Ok successful operation
        Get: /api/autos?color={color} Response 204 No autos found by that vin

        Get: /api/autos?make={make} Response 200 successful operation
        Get: /api/autos?make={make} Response 204 No autos found by that vin

        Get: /api/autos?make={make}?color={color} Response 200 successful operation
        Get: /api/autos?make={make}?color={color} Response 204 No autos found by that vin

        Get: /api/autos/{vin} Response 200 successful operation
        Get: /api/autos/{vin} Response 204 No autos found by that vin
        Patch: /api/autos/{vin} Response 200 OK
        Patch: /api/autos/{vin} Response 204 No autos found by that vin
        Patch: /api/autos/{vin} Response 400 Bad request (no payload, no change, already done)
        Delete: /api/autos/{vin} Response 200 OK
        Delete: /api/autos/{vin} Response 202 Accepted
        Delete: /api/autos/{vin} Response 204 No autos found by that vin
     */
}
